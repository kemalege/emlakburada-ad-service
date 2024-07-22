package com.patika.emlakburadaadservice.service;

import com.patika.emlakburadaadservice.consumer.NotificationDto;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.model.enums.AdStatus;
import com.patika.emlakburadaadservice.repository.AdRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdApprovalServiceTest {

    @Mock
    private AdService adService;

    @Mock
    private AdRepository adRepository;

    @InjectMocks
    private AdApprovalService adApprovalService;

    @Test
    void shouldActivateAdStatus() {
        // given
        NotificationDto notificationDto = Instancio.of(NotificationDto.class).create();
        Ad ad = Instancio.of(Ad.class).set(field(Ad::getId), notificationDto.getId()).create();

        // Mocking
        when(adService.getById(notificationDto.getId())).thenReturn(ad);

        // when
        adApprovalService.activeAdStatus(notificationDto);

        // then
        verify(adService, times(1)).getById(notificationDto.getId());
        verify(adRepository, times(1)).save(ad);
        assertThat(ad.getAdStatus()).isEqualTo(AdStatus.ACTIVE);
    }



}
