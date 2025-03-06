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
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
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
        sellingOrderDTO.setSellingOrderDetails(null);
        sellingOrderDTO.setOrderStatuses(null);

        return sellingOrderDTO;
    }

    @Override
    public void createSellingOrder(SellingOrderDTO sellingOrderDTO) throws ResourceNotFoundException {
        if (!sellingOrderDTO.getCustomerId().isEmpty()) {
            UUID customerId = UUID.fromString(sellingOrderDTO.getCustomerId());
            customerRepository.findById(customerId).orElseThrow(() ->
                    new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));
        }

        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(sellingOrderDTO.getOrderStatus()); // transform orderStatus from String to OrderStatusEnum
        SellingOrder sellingOrder = sellingOrderMapper.toSellingOrder(sellingOrderDTO);
        String newSellingOrderId = OrderUtils.generateOrderId();
        sellingOrder.setSellingOrderId(newSellingOrderId);
        sellingOrder.setOrderStatus(orderStatusEnum);

        if (sellingOrderDTO.getCustomerId() == null || sellingOrderDTO.getCustomerId().isEmpty()) {
            sellingOrder.setCustomer(null);
        } else {
            UUID customerId = UUID.fromString(sellingOrderDTO.getCustomerId());
            sellingOrder.setCustomer(Customer.builder().customerId(customerId).build());
        }

        sellingOrder.setOrderStatuses(null);
        sellingOrder.setSellingOrderDetails(null);
        sellingOrderRepository.save(sellingOrder);

        sellingOrder.setSellingOrderDetails(sellingOrderDetailService.createSellingOrderDetail(newSellingOrderId, sellingOrderDTO.getSellingOrderDetails()));
        sellingOrder.setOrderStatuses(orderStatusService.createOrderStatus(newSellingOrderId, orderStatusEnum));
//        sellingOrder.setUpdatedAt(null);

        sellingOrderRepository.save(sellingOrder);
    }


    @Override
    public void updateSellingOrderStatus(String sellingOrderId, OrderStatusEnum orderStatusEnum) throws ResourceNotFoundException {
        SellingOrder sellingOrder = sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + sellingOrderId));

        orderStatusService.createOrderStatus(sellingOrderId, orderStatusEnum);
        sellingOrder.setOrderStatus(orderStatusEnum);

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

