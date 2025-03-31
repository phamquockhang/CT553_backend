package com.pqkhang.ct553_backend.domain.booking.order.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.request.RequestSellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.dto.response.SellingOrderStatisticsDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.mapper.SellingOrderMapper;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.OrderStatusService;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderDetailService;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderService;
import com.pqkhang.ct553_backend.domain.booking.order.utils.OrderUtils;
import com.pqkhang.ct553_backend.domain.booking.voucher.dto.UsedVoucherDTO;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.UsedVoucher;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.Voucher;
import com.pqkhang.ct553_backend.domain.booking.voucher.enums.DiscountTypeEnum;
import com.pqkhang.ct553_backend.domain.booking.voucher.repository.VoucherRepository;
import com.pqkhang.ct553_backend.domain.booking.voucher.service.UsedVoucherService;
import com.pqkhang.ct553_backend.domain.booking.voucher.service.VoucherService;
import com.pqkhang.ct553_backend.domain.notification.dto.NotificationDTO;
import com.pqkhang.ct553_backend.domain.notification.entity.Notification;
import com.pqkhang.ct553_backend.domain.notification.enums.NotificationTypeEnum;
import com.pqkhang.ct553_backend.domain.notification.repository.NotificationRepository;
import com.pqkhang.ct553_backend.domain.notification.service.EmailService;
import com.pqkhang.ct553_backend.domain.notification.service.NotificationService;
import com.pqkhang.ct553_backend.domain.user.dto.ScoreCalculator;
import com.pqkhang.ct553_backend.domain.user.dto.ScoreDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.entity.Staff;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import com.pqkhang.ct553_backend.domain.user.repository.StaffRepository;
import com.pqkhang.ct553_backend.domain.user.service.ScoreService;
import com.pqkhang.ct553_backend.infrastructure.audit.AuditAwareImpl;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import com.pqkhang.ct553_backend.infrastructure.websocket.service.WebSocketService;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Slf4j
public class SellingOrderServiceImpl implements SellingOrderService {

    CustomerRepository customerRepository;
    SellingOrderMapper sellingOrderMapper;
    SellingOrderRepository sellingOrderRepository;
    RequestParamUtils requestParamUtils;
    SellingOrderDetailService sellingOrderDetailService;
    OrderStatusService orderStatusService;
    StringUtils stringUtils;
    ScoreService scoreService;
    VoucherRepository voucherRepository;
    UsedVoucherService usedVoucherService;
    VoucherService voucherService;
    EmailService emailService;
    StaffRepository staffRepository;
    WebSocketService webSocketService;

    static String DEFAULT_PAGE = "1";
    static String DEFAULT_PAGE_SIZE = "10";
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final AuditAwareImpl auditAwareImpl;

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, SellingOrder.class);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    private Specification<SellingOrder> buildSearchSpec(Map<String, String> params) {
        return Specification.where(buildQuerySpec_SellingOrderId(params)).and(buildPaymentStatusSpec(params)).and(buildOrderStatusSpec(params)).and(buildAssignedStaffEmailSpec(params));
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

    private Specification<SellingOrder> buildAssignedStaffEmailSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "assignedStaffEmail")
                .stream()
                .map(criteria -> (Specification<SellingOrder>) (root, query, cb) ->
                        cb.equal(root.get("assignedStaffEmail"), criteria.getValue().toString())
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

        // Tính điểm tích lũy từ tổng tiền đơn hàng (khi chưa trừ điểm và voucher, nếu có)
        int convertedScore = ScoreCalculator.convertMoneyToScores(sellingOrderDTO.getTotalAmount());
        int deductedScore = sellingOrderDTO.getUsedScore() != null ? -sellingOrderDTO.getUsedScore() : 0;
        int finalScore = convertedScore + deductedScore;

        if (finalScore != 0) {
            // only process score when payment status is success,
            //  and order status is completed
            if (orderStatus.equals(OrderStatusEnum.COMPLETED) && paymentStatus.equals(PaymentStatusEnum.SUCCESS)
            ) {
                ScoreDTO newScoreDTO = ScoreDTO.builder()
                        .changeAmount(finalScore)
                        .build();
                scoreService.createScore(customerId, newScoreDTO);
            }
        }

        sellingOrderRepository.save(sellingOrder);
    }

    private static String getLeastBusyStaff(List<Object[]> staffOrdersCount, List<String> staffEmails) {
        Map<String, Long> staffOrderMap = new HashMap<>();
        for (Object[] result : staffOrdersCount) {
            String email = (String) result[0];
            Long count = (Long) result[1];
            staffOrderMap.put(email, count);
        }

        String leastBusyStaff = null;
        long minOrders = Long.MAX_VALUE;

        for (String email : staffEmails) {
            long orders = staffOrderMap.getOrDefault(email, 0L);
            if (orders < minOrders) {
                minOrders = orders;
                leastBusyStaff = email;
            }
        }
        return leastBusyStaff;
    }

    private String assignOrder(SellingOrder sellingOrder) {
        // tìm các nhân viên hiện tại có thể xử lý đơn hàng
        List<String> staffEmails = staffRepository.findAllStaffEmailsByIsActivatedTrue();

        // tìm các nhân viên xử lý đơn hàng ít nhất trong ngày
        List<Object[]> staffOrdersCount = sellingOrderRepository.countOrdersByAssignedStaffToday();

        // tìm nhân viên có ít đơn hàng nhất trong ngày đến thời điểm hiện tại
        String leastBusyStaff = getLeastBusyStaff(staffOrdersCount, staffEmails);

        // gán đơn hàng cho nhân viên
        sellingOrder.setAssignedStaffEmail(leastBusyStaff);
        log.info("🔹 Assigned order to staff: {}", leastBusyStaff);
        return leastBusyStaff;
    }

    private void createNotification(SellingOrder sellingOrder, String leastBusyStaffEmail) throws ResourceNotFoundException {
        Staff staff = staffRepository.findStaffByEmail(leastBusyStaffEmail).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy nhân viên với email: " + leastBusyStaffEmail)
        );
        // tạo thông báo cho nhân viên
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .notificationType(NotificationTypeEnum.NEW_ORDER.name())
                .staffId(staff.getStaffId().toString())
                .sellingOrderId(sellingOrder.getSellingOrderId())
                .build();
        notificationService.createNotification(notificationDTO);
        log.info("🔹 Notification: {}", notificationDTO);

        // gửi thông báo hệ thống cho nhân viên
        webSocketService.sendSystemNotification(staff.getStaffId(), "Bạn có đơn hàng mới cần xử lý");
        log.info("🔹 Sending system notification");

        sellingOrderRepository.save(sellingOrder);
    }

    private void assignOrderAndCreateNotification(SellingOrder sellingOrder) throws ResourceNotFoundException {
        // phân công đơn hàng cho nhân viên ít đơn hàng nhất trong ngày
        String leastBusyStaffEmail = assignOrder(sellingOrder);

        createNotification(sellingOrder, leastBusyStaffEmail);
    }

    @Override
    public SellingOrderDTO createSellingOrder(RequestSellingOrderDTO requestSellingOrderDTO) throws ResourceNotFoundException {
        UUID customerId;
        if (requestSellingOrderDTO.getCustomerId() != null && !requestSellingOrderDTO.getCustomerId().isEmpty()) {
            customerId = UUID.fromString(requestSellingOrderDTO.getCustomerId());
            customerRepository.findById(customerId).orElseThrow(() ->
                    new ResourceNotFoundException("Không tìm thấy khách hàng với id: " + customerId));
        } else {
            customerId = null;
        }

        String voucherCode;
        Voucher voucher = null;
        if (requestSellingOrderDTO.getVoucherCode() != null && !requestSellingOrderDTO.getVoucherCode().isEmpty()) {
            voucherCode = requestSellingOrderDTO.getVoucherCode();
            voucher = voucherRepository.findByVoucherCode(voucherCode).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy voucher với mã: " + voucherCode));
        } else {
            voucherCode = null;
        }

        // Chuyển đổi orderStatus từ String sang Enum
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(requestSellingOrderDTO.getOrderStatus());

        SellingOrder sellingOrder = sellingOrderMapper.toSellingOrder(requestSellingOrderDTO);
        String newSellingOrderId = OrderUtils.generateOrderId();
        sellingOrder.setSellingOrderId(newSellingOrderId);
        sellingOrder.setOrderStatus(orderStatusEnum);
        sellingOrder.setCustomer(customerId != null ? Customer.builder().customerId(customerId).build() : null);
        sellingOrder.setTotalAmount(requestSellingOrderDTO.getUsedScore() != null
                ? requestSellingOrderDTO.getTotalAmount().subtract(BigDecimal.valueOf(requestSellingOrderDTO.getUsedScore()))
                : requestSellingOrderDTO.getTotalAmount());
        sellingOrder.setEarnedScore(customerId != null
                ? ScoreCalculator.convertMoneyToScores(requestSellingOrderDTO.getTotalAmount())
                : 0);

        sellingOrder.setOrderStatuses(null);
        sellingOrder.setSellingOrderDetails(null);
        sellingOrder.setUsedVoucher(null);
        sellingOrderRepository.save(sellingOrder);

        // Tạo chi tiết đơn hàng, trạng thái đơn hàng và voucher đã sử dụng
        sellingOrder.setSellingOrderDetails(sellingOrderDetailService.createSellingOrderDetail(newSellingOrderId, requestSellingOrderDTO.getSellingOrderDetails()));
        sellingOrder.setOrderStatuses(orderStatusService.createOrderStatus(newSellingOrderId, orderStatusEnum));
        if (voucherCode != null) {
            voucher = voucherRepository.findByVoucherCode(voucherCode).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy voucher với mã: " + voucherCode));

            // Tính toán giảm giá
            BigDecimal totalAmount = sellingOrder.getTotalAmount();
            BigDecimal discountAmount;

            if (voucher.getDiscountType().equals(DiscountTypeEnum.PERCENTAGE)) {
                discountAmount = totalAmount.multiply(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100));
                discountAmount = discountAmount.compareTo(voucher.getMaxDiscount()) > 0 ? voucher.getMaxDiscount() : discountAmount;
            } else {
                discountAmount = voucher.getDiscountValue();
            }
            sellingOrder.setTotalAmount(totalAmount.subtract(discountAmount));


            UsedVoucherDTO usedVoucherDTO = new UsedVoucherDTO();
            usedVoucherDTO.setVoucherCode(voucherCode);
            usedVoucherDTO.setSellingOrderId(sellingOrder.getSellingOrderId());
            usedVoucherDTO.setDiscountAmount(discountAmount);

            log.info("------------------UsedVoucherDTO: {}", usedVoucherDTO);

            usedVoucherService.createUsedVoucher(usedVoucherDTO);
            voucherService.useVoucher(voucherCode);
        }

        // Lưu lại đơn hàng sau khi cập nhật tổng tiền giảm giá
        sellingOrderRepository.save(sellingOrder);

        System.out.println("💲 SellingOrder: totalAmount " + sellingOrder.getTotalAmount());
        // Tính toán điểm tích lũy
        processCustomerScore(requestSellingOrderDTO, sellingOrder, customerId);

        if (sellingOrder.getEmail() != null && !sellingOrder.getEmail().isEmpty()) {
            if (sellingOrder.getOrderStatus().equals(OrderStatusEnum.COMPLETED) && sellingOrder.getPaymentStatus().equals(PaymentStatusEnum.SUCCESS)) {
                log.info("Sending email to customer");
                emailService.sendSellingOrderStatusEmail(sellingOrder);
            }
        }

        auditAwareImpl.getCurrentAuditor().ifPresent(auditor -> {
            log.info("🔹 Auditor: {}", auditor);

            // chỉ phân công đơn hàng tự động và tạo thông báo cho nhân viên khi người tạo đơn hàng là khách hàng
            // có thể là khách hàng đã đăng ký tài khoản hoặc khách vãng lai
            if (customerRepository.existsByEmail(auditor) || auditor.equals("anonymousUser")) {
                try {
                    assignOrderAndCreateNotification(sellingOrder);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sellingOrder.setAssignedStaffEmail(auditor);
                sellingOrderRepository.save(sellingOrder);
            }
        });

        return sellingOrderMapper.toSellingOrderDTO(sellingOrder);
    }

    @Override
    public void updateSellingOrderStatus(String sellingOrderId, OrderStatusEnum newOrderStatus, PaymentStatusEnum newPaymentStatus) throws ResourceNotFoundException {
        SellingOrder sellingOrder = sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id: " + sellingOrderId));

        OrderStatusEnum oldOrderStatus = sellingOrder.getOrderStatus();
        if (!oldOrderStatus.equals(newOrderStatus)) {
            orderStatusService.createOrderStatus(sellingOrderId, newOrderStatus);
            sellingOrder.setOrderStatus(newOrderStatus);
            if (newOrderStatus.equals(OrderStatusEnum.CANCELLED)) {
                if (sellingOrder.getUsedVoucher() != null) {
                    UsedVoucher usedVoucher = sellingOrder.getUsedVoucher();

                    sellingOrder.setUsedVoucher(null);
                    sellingOrderRepository.save(sellingOrder);

                    voucherService.returnVoucher(usedVoucher.getVoucher().getVoucherCode());
//                    usedVoucherService.deleteUsedVoucher(usedVoucher.getUsedVoucherId());
                }

                // Đánh dấu thông báo đã đọc khi hủy đơn hàng
                Notification notification = notificationRepository.findBySellingOrder_SellingOrderId(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông báo cho đơn hàng: " + sellingOrderId));
                if (!notification.getIsRead()) {
                    notificationService.readNotification(notification.getNotificationId());
                }
            } else if (newOrderStatus.equals(OrderStatusEnum.CONFIRMED)) {
                // Đánh dấu thông báo đã đọc khi xác nhận đơn hàng
                Notification notification = notificationRepository.findBySellingOrder_SellingOrderId(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông báo cho đơn hàng: " + sellingOrderId));

                if (!notification.getIsRead()) {
                    notificationService.readNotification(notification.getNotificationId());
                }
            }

            log.info("Sending email to customer");
            emailService.sendSellingOrderStatusEmail(sellingOrder);
        }

        PaymentStatusEnum oldPaymentStatus = sellingOrder.getPaymentStatus();
        if (!oldPaymentStatus.equals(newPaymentStatus)) {
            sellingOrder.setPaymentStatus(newPaymentStatus);
            sellingOrderRepository.save(sellingOrder);
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

    @Override
    public SellingOrderStatisticsDTO getSellingOrderStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<SellingOrder> orders = sellingOrderRepository.findAllByCreatedAtBetween(startDate, endDate);

        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalNewOrders = 0;
        int totalDeliveringOrders = 0;
        int totalCompletedOrders = 0;
        int totalCancelledOrders = 0;

        for (SellingOrder order : orders) {
            // Đếm số lượng đơn hàng theo trạng thái
            if (order.getOrderStatus() == OrderStatusEnum.PENDING) {
                totalNewOrders++;
            } else if (order.getOrderStatus() == OrderStatusEnum.DELIVERING) {
                totalDeliveringOrders++;
            } else if (order.getOrderStatus() == OrderStatusEnum.CANCELLED) {
                totalCancelledOrders++;
            } else if (order.getOrderStatus() == OrderStatusEnum.COMPLETED) {
                totalRevenue = totalRevenue.add(order.getTotalAmount());
                totalCompletedOrders++;
            }
        }

        return SellingOrderStatisticsDTO.builder()
                .totalRevenue(totalRevenue)
                .totalNewOrders(totalNewOrders)
                .totalDeliveringOrders(totalDeliveringOrders)
                .totalCompletedOrders(totalCompletedOrders)
                .totalCancelledOrders(totalCancelledOrders)
                .build();
    }
}

