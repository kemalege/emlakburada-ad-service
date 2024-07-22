package com.patika.emlakburadaadservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patika.emlakburadaadservice.client.adpackage.AdPackageClient;
import com.patika.emlakburadaadservice.client.adpackage.service.AdPackageService;
import com.patika.emlakburadaadservice.client.user.dto.response.UserResponse;
import com.patika.emlakburadaadservice.client.user.service.UserService;

import com.patika.emlakburadaadservice.converter.AdConverter;
import com.patika.emlakburadaadservice.dto.request.AdSaveRequest;
import com.patika.emlakburadaadservice.dto.request.AdSearchRequest;
import com.patika.emlakburadaadservice.dto.request.AdUpdateStatusRequest;
import com.patika.emlakburadaadservice.dto.request.BaseSearchRequest;
import com.patika.emlakburadaadservice.dto.response.AdResponse;
import com.patika.emlakburadaadservice.dto.response.AdResponseWrapper;
import com.patika.emlakburadaadservice.exception.EmlakBuradaException;
import com.patika.emlakburadaadservice.exception.ExceptionMessages;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.producer.NotificationProducer;
import com.patika.emlakburadaadservice.producer.dto.NotificationDto;
import com.patika.emlakburadaadservice.repository.AdRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AdServiceTest {

    @InjectMocks
    private AdService adService;

    @Mock
    private UserService userService;

    @Mock
    private AdPackageService adPackageService;

    @Mock
    private AdRepository adRepository;

    @Mock
    private AdPackageClient adPackageClient;

    @Mock
    private NotificationProducer notificationProducer;

    @Test
    void createAdSuccessfully() {
        // given
        AdSaveRequest request = Instancio.of(AdSaveRequest.class).create();
        UserResponse userResponse = Instancio.of(UserResponse.class).create();

        when(adPackageService.checkUserPackageAvailability(request.getUserId())).thenReturn(true);
        when(userService.getUserById(request.getUserId())).thenReturn(userResponse);

        // when
        adService.create(request);

        // then
        verify(adPackageService, times(1)).checkUserPackageAvailability(request.getUserId());
        verify(userService, times(1)).getUserById(request.getUserId());
        verify(adRepository, times(1)).save(Mockito.any(Ad.class));
        verify(adPackageClient, times(1)).updatePackageRights(request.getUserId());
        verify(notificationProducer, times(1)).sendNotification(Mockito.any(NotificationDto.class));
    }

    @Test
    void shouldThrowException_WhenPackageNotAvailable() {
        // given
        AdSaveRequest request = Instancio.of(AdSaveRequest.class).create();

        when(adPackageService.checkUserPackageAvailability(request.getUserId())).thenReturn(false);

        // when - then
        EmlakBuradaException emlakBuradaException = Assertions.assertThrows(EmlakBuradaException.class, () -> adService.create(request));
        assertThat(emlakBuradaException.getMessage()).isEqualTo(ExceptionMessages.AD_NOT_RIGHTS);

        verify(adPackageService, times(1)).checkUserPackageAvailability(request.getUserId());
        verifyNoInteractions(userService);
        verifyNoInteractions(adRepository);
        verifyNoInteractions(adPackageClient);
        verifyNoInteractions(notificationProducer);

    }

    @Test
    void searchAds_successfully() {
        // given
        AdSearchRequest request = Instancio.of(AdSearchRequest.class)
                .set(field(BaseSearchRequest.class, "page"), 0)
                .set(field(BaseSearchRequest.class, "size"), 10)
                .set(field(AdSearchRequest.class, "userId"), 1L)
                .set(field(AdSearchRequest.class, "sort"), "location")
                .create();

        List<Ad> ads = Instancio.of(Ad.class).stream().limit(10).collect(Collectors.toList());
        Page<Ad> adPage = new PageImpl<>(ads, PageRequest.of(0, 10), 50); // 50 total records example

        // Mock behavior for repository
        when(adRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(adPage);

        // Create expected AdResponseWrapper
        Set<AdResponse> adResponses = AdConverter.toResponse(ads);

        // when
        AdResponseWrapper result = adService.getAll(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAds()).hasSize(10);
        assertThat(result.getTotalRecords()).isEqualTo(50);

        // Verify that the repository method was called once
        verify(adRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
    }


    @Test
    void updateAdSuccessfully() {
        // given
        Long adId = 1L;
        Ad ad = Instancio.of(Ad.class)
                .set(field("id"), adId)
                .create();

        AdSaveRequest request = Instancio.of(AdSaveRequest.class).create();

        when(adRepository.findById(adId)).thenReturn(java.util.Optional.of(ad));

        // when
        adService.update(adId, request);

        // then
        verify(adRepository, times(1)).findById(adId);
        verify(adRepository, times(1)).save(Mockito.any(Ad.class));

        for (Field requestField : AdSaveRequest.class.getDeclaredFields()) {
            requestField.setAccessible(true);
            try {
                Object requestValue = requestField.get(request);
                if (requestValue != null) {
                    Field adField = Ad.class.getDeclaredField(requestField.getName());
                    adField.setAccessible(true);
                    Object adValue = adField.get(ad);
                    assertEquals(requestValue, adValue, "Field " + requestField.getName() + " was not updated correctly.");
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void getAdResponseById_successfully() {
        // given
        Long adId = 1L;

        Ad ad = Instancio.of(Ad.class)
                .set(field(Ad::getId), adId)
                .create();

        UserResponse userResponse = Instancio.of(UserResponse.class)
                .set(field(UserResponse::getId), ad.getUserId())
                .create();

        AdResponse expectedResponse = AdConverter.toResponseWithUser(ad, userResponse);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(userService.getUserById(ad.getUserId())).thenReturn(userResponse);

        // when
        AdResponse actualResponse = adService.getAdResponseById(adId);

        // then
        assertThat(actualResponse.getId()).isEqualTo(expectedResponse.getId());
        assertThat(actualResponse.getTitle()).isEqualTo(expectedResponse.getTitle());

        verify(adRepository, times(1)).findById(adId);
        verify(userService, times(1)).getUserById(ad.getUserId());
    }

    @Test
    void shouldThrowException_whenAdIsNotFound() {
        // given
        Long adId = 1L;

        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        // when
        EmlakBuradaException emlakBuradaException = assertThrows(EmlakBuradaException.class, () -> adService.delete(adId));

        // then
        assertThat(emlakBuradaException.getMessage()).isEqualTo(ExceptionMessages.AD_NOT_FOUND);
        verify(adRepository, never()).delete(any(Ad.class));
    }

    @Test
    void shouldDeleteAd_successfully() {
        // given
        Long adId = 1L;
        Ad ad = Instancio.of(Ad.class).set(field(Ad::getId), adId).create();

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        // when
        adService.delete(adId);

        // then
        verify(adRepository, times(1)).findById(adId);
        verify(adRepository, times(1)).delete(ad);
    }

    @Test
    void shouldUpdateAdStatus_successfully() {
        // given
        Long adId = 1L;
        AdUpdateStatusRequest request = Instancio.of(AdUpdateStatusRequest.class).create();
        Ad ad = Instancio.of(Ad.class).set(field(Ad::getId), adId).create();

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

        // when
        adService.updateStatus(adId, request);

        // then
        verify(adRepository, times(1)).findById(adId);
        verify(adRepository, times(1)).save(ad);
        assertThat(ad.getAdStatus()).isEqualTo(request.getAdStatus());
    }

}
