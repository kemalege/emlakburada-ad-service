package com.patika.emlakburadaadservice.producer.dto;

import com.patika.emlakburadaadservice.producer.dto.enums.NotificationType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDto {

    private NotificationType notificationType;
    private Long id;

}
