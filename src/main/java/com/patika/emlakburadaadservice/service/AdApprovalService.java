package com.patika.emlakburadaadservice.service;

import com.patika.emlakburadaadservice.client.user.service.UserService;
import com.patika.emlakburadaadservice.consumer.NotificationDto;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.model.enums.AdStatus;
import com.patika.emlakburadaadservice.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdApprovalService {

    private final AdService adService;
    private final AdRepository adRepository;

    @RabbitListener(queues = "${notification.queue}")
    public void activeAdStatus(NotificationDto notificationDto) {

        Ad ad = adService.getById(notificationDto.getId());

        ad.setAdStatus(AdStatus.ACTIVE);
        adRepository.save(ad);

        log.info("ad activated :{}", notificationDto.getId());

    }
}
