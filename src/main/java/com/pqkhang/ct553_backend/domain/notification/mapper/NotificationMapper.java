package com.pqkhang.ct553_backend.domain.notification.mapper;

import com.pqkhang.ct553_backend.domain.notification.dto.NotificationDTO;
import com.pqkhang.ct553_backend.domain.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "sellingOrderId", source = "sellingOrder.sellingOrderId")
    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "staffId", source = "staff.staffId")
    NotificationDTO toNotificationDTO(Notification notification);

    @Mapping(target = "sellingOrder", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "staff", ignore = true)
    Notification toNotification(NotificationDTO notificationDTO);
}

