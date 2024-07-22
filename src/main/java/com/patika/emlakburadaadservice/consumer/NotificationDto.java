package com.patika.emlakburadaadservice.consumer;

import com.patika.emlakburadaadservice.consumer.enums.NotificationType;
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
