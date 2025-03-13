package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.mapper.SellingOrderMapper;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderStatusService;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderDetailService;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderService;
import com.pqkhang.ct553_backend.domain.booking.order.utils.OrderUtils;
import com.pqkhang.ct553_backend.domain.user.dto.ScoreCalculator;
import com.pqkhang.ct553_backend.domain.user.dto.ScoreDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import com.pqkhang.ct553_backend.domain.user.service.ScoreService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class SellingOrderServiceImpl implements SellingOrderService {

    CustomerRepository customerRepository;
    SellingOrderMapper sellingOrderMapper;
    SellingOrderRepository sellingOrderRepository;
    RequestParamUtils requestParamUtils;
    SellingOrderDetailService sellingOrderDetailService;
    OrderStatusService orderStatusService;
    StringUtils stringUtils;

    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private final ScoreService scoreService;

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, SellingOrder.class);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    private Specification<SellingOrder> buildSearchSpec(Map<String, String> params) {
        return Specification.where(buildQuerySpec_SellingOrderId(params)).and(buildPaymentStatusSpec(params)).and(buildOrderStatusSpec(params));
    }

    private Specification<SellingOrder> buildQuerySpec_SellingOrderId(Map<String, String> params) {
        return params.containsKey("query") ? (root, query, cb) -> {
            String[] searchValues = params.get("query").trim().toLowerCase().split(",");
            Predicate[] predicates = Arrays.stream(searchValues).map(stringUtils::normalizeString).map(value -> "%" + value.trim() + "%").map(pattern -> cb.like(cb.function("unaccent", String.class, cb.lower(root.get("sellingOrderId"))), pattern)).toArray(Predicate[]::new);
            return cb.or(predicates);
        } : null;
    }

    private Specification<SellingOrder> buildPaymentStatusSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "paymentStatus").stream()
                .map(criteria -> (Specification<SellingOrder>) (root, query, cb) ->
                        cb.equal(root.get("paymentStatus"), PaymentStatusEnum.valueOf(criteria.getValue().toString().toUpperCase()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Specification<SellingOrder> buildOrderStatusSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "orderStatus")
                .stream()
                .map(criteria -> (Specification<SellingOrder>) (root, query, cb) ->
                        cb.equal(root.get("orderStatus"), OrderStatusEnum.valueOf(criteria.getValue().toString().toUpperCase()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Page<SellingOrderDTO> buildOrderPage(org.springframework.data.domain.Page<SellingOrder> orderPage, Pageable pageable) throws ResourceNotFoundException {
        if (orderPage.isEmpty()) {
            throw new ResourceNotFoundException("No order found!");
        }
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(orderPage.getTotalPages())
                .total(orderPage.getTotalElements())
                .build();
        return Page.<SellingOrderDTO>builder()
                .meta(meta)
                .data(orderPage.getContent()
                        .stream().map(this::toOverViewSellingOrderDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private SellingOrderDTO toOverViewSellingOrderDTO(SellingOrder sellingOrder) {
        SellingOrderDTO sellingOrderDTO = sellingOrderMapper.toSellingOrderDTO(sellingOrder);

        sellingOrderDTO.setCustomerId(null);
        sellingOrderDTO.setCustomerName(null);
        sellingOrderDTO.setPhone(null);
        sellingOrderDTO.setEmail(null);
        sellingOrderDTO.setAddress(null);
        sellingOrderDTO.setNote(null);
        sellingOrderDTO.setUsedScore(null);
        sellingOrderDTO.setEarnedScore(null);
        sellingOrderDTO.setSellingOrderDetails(null);
        sellingOrderDTO.setOrderStatuses(null);

        return sellingOrderDTO;
    }

    private void processCustomerScore(SellingOrderDTO sellingOrderDTO, SellingOrder sellingOrder, UUID customerId) {
        //only process score when customer has account
        if (customerId == null) {
            return;
        }

        OrderStatusEnum orderStatus = OrderStatusEnum.valueOf(sellingOrderDTO.getOrderStatus());
        PaymentStatusEnum paymentStatus = PaymentStatusEnum.valueOf(sellingOrderDTO.getPaymentStatus());

        int convertedScore = ScoreCalculator.convertMoneyToScores(sellingOrderDTO.getTotalAmount());
        int deductedScore = sellingOrderDTO.getUsedScore() != null ? -sellingOrderDTO.getUsedScore() : 0;
        int finalScore = convertedScore + deductedScore;

        sellingOrder.setEarnedScore(convertedScore);

        if (finalScore != 0) {
            // only process score when payment status is success,
            // or payment status is COD and order status is completed
            if (orderStatus.equals(OrderStatusEnum.COMPLETED) &&
                    (paymentStatus.equals(PaymentStatusEnum.COD) || paymentStatus.equals(PaymentStatusEnum.SUCCESS))
            ) {
                ScoreDTO newScoreDTO = ScoreDTO.builder()
                        .changeAmount(finalScore)
                        .build();
                scoreService.createScore(customerId, newScoreDTO);
            }
        }
    }

    @Override
    public SellingOrderDTO createSellingOrder(SellingOrderDTO sellingOrderDTO) throws ResourceNotFoundException {
        UUID customerId;

        if (sellingOrderDTO.getCustomerId() != null && !sellingOrderDTO.getCustomerId().isEmpty()) {
            customerId = UUID.fromString(sellingOrderDTO.getCustomerId());
            customerRepository.findById(customerId).orElseThrow(() ->
                    new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));
        } else {
            customerId = null;
        }

        // Chuyển đổi orderStatus từ String sang Enum
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(sellingOrderDTO.getOrderStatus());

        SellingOrder sellingOrder = sellingOrderMapper.toSellingOrder(sellingOrderDTO);
        String newSellingOrderId = OrderUtils.generateOrderId();
        sellingOrder.setSellingOrderId(newSellingOrderId);
        sellingOrder.setOrderStatus(orderStatusEnum);
        sellingOrder.setCustomer(customerId != null ? Customer.builder().customerId(customerId).build() : null);

        // Tính toán điểm tích lũy
        processCustomerScore(sellingOrderDTO, sellingOrder, customerId);

        sellingOrder.setOrderStatuses(null);
        sellingOrder.setSellingOrderDetails(null);
        sellingOrderRepository.save(sellingOrder);

        // Tạo chi tiết đơn hàng và trạng thái đơn hàng
        sellingOrder.setSellingOrderDetails(sellingOrderDetailService.createSellingOrderDetail(newSellingOrderId, sellingOrderDTO.getSellingOrderDetails()));
        sellingOrder.setOrderStatuses(orderStatusService.createOrderStatus(newSellingOrderId, orderStatusEnum));

        sellingOrderRepository.save(sellingOrder);

        return sellingOrderMapper.toSellingOrderDTO(sellingOrder);
    }


    @Override
    public void updateSellingOrderStatus(String sellingOrderId, OrderStatusEnum newOrderStatus, PaymentStatusEnum newPaymentStatus) throws ResourceNotFoundException {
        SellingOrder sellingOrder = sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + sellingOrderId));

        OrderStatusEnum oldOrderStatus = sellingOrder.getOrderStatus();
        if (!oldOrderStatus.equals(newOrderStatus)) {
            orderStatusService.createOrderStatus(sellingOrderId, newOrderStatus);
            sellingOrder.setOrderStatus(newOrderStatus);
        }

        PaymentStatusEnum oldPaymentStatus = sellingOrder.getPaymentStatus();
        if (!oldPaymentStatus.equals(newPaymentStatus)) {
            sellingOrder.setPaymentStatus(newPaymentStatus);
        }

        if (sellingOrder.getCustomer() != null) {
            processCustomerScore(sellingOrderMapper.toSellingOrderDTO(sellingOrder), sellingOrder, sellingOrder.getCustomer().getCustomerId());
        }

        sellingOrderRepository.save(sellingOrder);
    }

    @Override
    public SellingOrderDTO getSellingOrder(String sellingOrderId) throws ResourceNotFoundException {
        SellingOrder sellingOrder = sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + sellingOrderId));
        return sellingOrderMapper.toSellingOrderDTO(sellingOrder);
    }

    @Override
    public List<SellingOrderDTO> getAllSellingOrders() {
        return sellingOrderRepository.findAll().stream().map(this::toOverViewSellingOrderDTO).collect(Collectors.toList());
    }

    @Override
    public List<SellingOrderDTO> getAllSellingOrdersByCustomerId(UUID customerId) throws ResourceNotFoundException {
        customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));

        return sellingOrderRepository.findAllByCustomer_CustomerId(customerId).stream().map(this::toOverViewSellingOrderDTO).toList();
    }

    @Override
    public Page<SellingOrderDTO> getSellingOrders(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        Specification<SellingOrder> spec = buildSearchSpec(params);
        return buildOrderPage(sellingOrderRepository.findAll(spec, pageable), pageable);
    }
}

