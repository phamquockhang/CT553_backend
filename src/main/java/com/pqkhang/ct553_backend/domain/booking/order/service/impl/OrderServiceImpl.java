package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.Order;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.mapper.OrderMapper;
import com.pqkhang.ct553_backend.domain.booking.order.repository.OrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderDetailService;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderService;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderStatusService;
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
public class OrderServiceImpl implements OrderService {

    CustomerRepository customerRepository;
    OrderMapper orderMapper;
    OrderRepository orderRepository;
    RequestParamUtils requestParamUtils;
    OrderDetailService orderDetailService;
    OrderStatusService orderStatusService;
    StringUtils stringUtils;

    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "10";

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Order.class);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    private Specification<Order> buildSearchSpec(Map<String, String> params) {
        return Specification.where(buildQuerySpec_OrderId(params)).and(buildPaymentStatusSpec(params)).and(buildOrderStatusSpec(params));
    }

    private Specification<Order> buildQuerySpec_OrderId(Map<String, String> params) {
        return params.containsKey("query") ? (root, query, cb) -> {
            String[] searchValues = params.get("query").trim().toLowerCase().split(",");
            Predicate[] predicates = Arrays.stream(searchValues).map(stringUtils::normalizeString).map(value -> "%" + value.trim() + "%").map(pattern -> cb.like(cb.function("unaccent", String.class, cb.lower(root.get("orderId"))), pattern)).toArray(Predicate[]::new);
            return cb.or(predicates);
        } : null;
    }

    private Specification<Order> buildPaymentStatusSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "paymentStatus").stream()
                .map(criteria -> (Specification<Order>) (root, query, cb) ->
                        cb.equal(root.get("paymentStatus"), PaymentStatusEnum.valueOf(criteria.getValue().toString().toUpperCase()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Specification<Order> buildOrderStatusSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "orderStatus")
                .stream()
                .map(criteria -> (Specification<Order>) (root, query, cb) ->
                        cb.equal(root.get("orderStatus"), OrderStatusEnum.valueOf(criteria.getValue().toString().toUpperCase()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Page<OrderDTO> buildOrderPage(org.springframework.data.domain.Page<Order> orderPage, Pageable pageable) throws ResourceNotFoundException {
        if (orderPage.isEmpty()) {
            throw new ResourceNotFoundException("No order found!");
        }
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(orderPage.getTotalPages())
                .total(orderPage.getTotalElements())
                .build();
        return Page.<OrderDTO>builder()
                .meta(meta)
                .data(orderPage.getContent()
                        .stream().map(this::toOverViewOrderDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private OrderDTO toOverViewOrderDTO(Order order) {
        OrderDTO orderDTO = orderMapper.toOrderDTO(order);

        orderDTO.setCustomerId(null);
        orderDTO.setName(null);
        orderDTO.setPhone(null);
        orderDTO.setEmail(null);
        orderDTO.setAddress(null);
        orderDTO.setNote(null);
        orderDTO.setOrderDetails(null);
        orderDTO.setOrderStatuses(null);

        return orderDTO;
    }

    @Override
    public void createOrderByCustomerId(OrderDTO orderDTO) throws ResourceNotFoundException {
        UUID customerId = UUID.fromString(orderDTO.getCustomerId());

        customerRepository.findById(customerId).orElseThrow(() ->
                new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));

        Order order = orderMapper.toOrder(orderDTO);
        String newOrderId = OrderUtils.generateOrderId();
        order.setOrderId(newOrderId);
        order.setCustomer(Customer.builder().customerId(customerId).build());
        order.setOrderStatus(OrderStatusEnum.PENDING);
        order.setOrderStatuses(null);
        order.setOrderDetails(null);
        orderRepository.save(order);

        order.setOrderDetails(orderDetailService.createOrderDetail(newOrderId, orderDTO.getOrderDetails()));
        order.setOrderStatuses(orderStatusService.createOrderStatus(newOrderId, OrderStatusEnum.PENDING));
        order.setUpdatedAt(null);

        orderRepository.save(order);
    }


    @Override
    public void updateOrderStatus(String orderId, OrderStatusEnum orderStatusEnum) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));

        orderStatusService.createOrderStatus(orderId, orderStatusEnum);
        order.setOrderStatus(orderStatusEnum);

        orderRepository.save(order);
    }

    @Override
    public OrderDTO getOrder(String orderId) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + orderId));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toOverViewOrderDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrdersByCustomerId(UUID customerId) throws ResourceNotFoundException {
        customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));

        return orderRepository.findAllByCustomer_CustomerId(customerId).stream().map(this::toOverViewOrderDTO).toList();
    }

    @Override
    public Page<OrderDTO> getOrders(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        Specification<Order> spec = buildSearchSpec(params);
        return buildOrderPage(orderRepository.findAll(spec, pageable), pageable);
    }
}

