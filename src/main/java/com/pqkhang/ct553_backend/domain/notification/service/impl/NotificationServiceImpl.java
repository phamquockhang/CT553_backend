package com.pqkhang.ct553_backend.domain.notification.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.notification.dto.NotificationDTO;
import com.pqkhang.ct553_backend.domain.notification.entity.Notification;
import com.pqkhang.ct553_backend.domain.notification.enums.NotificationTypeEnum;
import com.pqkhang.ct553_backend.domain.notification.mapper.NotificationMapper;
import com.pqkhang.ct553_backend.domain.notification.repository.NotificationRepository;
import com.pqkhang.ct553_backend.domain.notification.service.NotificationService;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.entity.Staff;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import com.pqkhang.ct553_backend.domain.user.repository.StaffRepository;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    RequestParamUtils requestParamUtils;
    SellingOrderRepository sellingOrderRepository;
    CustomerRepository customerRepository;
    StaffRepository staffRepository;

    static String DEFAULT_PAGE = "1";
    static String DEFAULT_PAGE_SIZE = "10";

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Notification.class);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    private Specification<Notification> buildSearchSpec(Map<String, String> params) {
        return Specification.where(buildIsReadSpec(params)).and(buildNotificationTypeSpec(params)).and(buildStaffIdSpec(params));
    }

//    private Specification<SellingOrder> buildQuerySpec_SellingOrderId(Map<String, String> params) {
//        return params.containsKey("query") ? (root, query, cb) -> {
//            String[] searchValues = params.get("query").trim().toLowerCase().split(",");
//            Predicate[] predicates = Arrays.stream(searchValues).map(stringUtils::normalizeString).map(value -> "%" + value.trim() + "%").map(pattern -> cb.like(cb.function("unaccent", String.class, cb.lower(root.get("sellingOrderId"))), pattern)).toArray(Predicate[]::new);
//            return cb.or(predicates);
//        } : null;
//    }

    private Specification<Notification> buildStaffIdSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "staffEmail").stream()
                .map(criteria -> (Specification<Notification>) (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("staff").get("email"), criteria.getValue().toString())
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Specification<Notification> buildIsReadSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "isRead").stream()
                .map(criteria -> (Specification<Notification>) (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("isRead"), Boolean.parseBoolean(criteria.getValue().toString()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Specification<Notification> buildNotificationTypeSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "notificationType")
                .stream()
                .map(criteria -> (Specification<Notification>) (root, query, cb) ->
                        cb.equal(root.get("notificationType"), NotificationTypeEnum.valueOf(criteria.getValue().toString().toUpperCase()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Page<NotificationDTO> buildOrderPage(org.springframework.data.domain.Page<Notification> notificationPage, Pageable pageable) throws ResourceNotFoundException {
        if (notificationPage.isEmpty()) {
            throw new ResourceNotFoundException("No notification found");
        }
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(notificationPage.getTotalPages())
                .total(notificationPage.getTotalElements())
                .build();
        return Page.<NotificationDTO>builder()
                .meta(meta)
                .data(notificationPage.getContent()
                        .stream()
                        .map(notificationMapper::toNotificationDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public Page<NotificationDTO> getNotifications(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        Specification<Notification> searchSpec = buildSearchSpec(params);
        return buildOrderPage(notificationRepository.findAll(searchSpec, pageable), pageable);
    }

    @Override
    @Transactional
    public void createNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationMapper.toNotification(notificationDTO);

        if (notificationDTO.getSellingOrderId() != null) {
            SellingOrder sellingOrder = sellingOrderRepository.findById(notificationDTO.getSellingOrderId())
                    .orElseThrow(() -> new RuntimeException("SellingOrder not found"));
            notification.setSellingOrder(sellingOrder);
        } else {
            notification.setSellingOrder(null);
        }

        if (notificationDTO.getCustomerId() != null) {
            UUID customerId = UUID.fromString(notificationDTO.getCustomerId());
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            notification.setCustomer(customer);
        } else {
            notification.setCustomer(null);
        }

        if (notificationDTO.getStaffId() != null) {
            UUID staffId = UUID.fromString(notificationDTO.getStaffId());
            Staff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            notification.setStaff(staff);
        } else {
            notification.setStaff(null);
        }

        notificationRepository.save(notification);
    }


    @Override
    @Transactional
    public void readNotification(UUID notificationId) throws ResourceNotFoundException {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new ResourceNotFoundException("Notification not found")
        );

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}
