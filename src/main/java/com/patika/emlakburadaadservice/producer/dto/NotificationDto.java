package com.patika.emlakburadaadservice.producer.dto;

import com.patika.emlakburadaadservice.producer.dto.enums.NotificationType;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDto {

    private NotificationType notificationType;
    private String adCode;

}
