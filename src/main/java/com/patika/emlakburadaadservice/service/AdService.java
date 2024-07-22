package com.patika.emlakburadaadservice.service;

import com.patika.emlakburadaadservice.client.adpackage.AdPackageClient;
import com.patika.emlakburadaadservice.client.adpackage.response.AdPackageAvailabilityResponse;
import com.patika.emlakburadaadservice.client.adpackage.service.AdPackageService;
import com.patika.emlakburadaadservice.client.user.dto.response.UserResponse;
import com.patika.emlakburadaadservice.client.user.service.UserService;
import com.patika.emlakburadaadservice.converter.AdConverter;
import com.patika.emlakburadaadservice.dto.request.AdSaveRequest;
import com.patika.emlakburadaadservice.dto.request.AdSearchRequest;
import com.patika.emlakburadaadservice.dto.request.AdUpdateStatusRequest;
import com.patika.emlakburadaadservice.dto.response.AdResponse;
import com.patika.emlakburadaadservice.dto.response.AdResponseWrapper;
import com.patika.emlakburadaadservice.exception.EmlakBuradaException;
import com.patika.emlakburadaadservice.exception.ExceptionMessages;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.model.enums.AdStatus;
import com.patika.emlakburadaadservice.producer.NotificationProducer;
import com.patika.emlakburadaadservice.producer.dto.NotificationDto;
import com.patika.emlakburadaadservice.producer.dto.enums.NotificationType;
import com.patika.emlakburadaadservice.repository.AdRepository;
import com.patika.emlakburadaadservice.repository.specification.AdSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdService {

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdPackageService adPackageService;
    private final AdPackageClient adPackageClient;
    private final NotificationProducer notificationProducer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(AdSaveRequest request) {

        Boolean adPackageAvailability = adPackageService.checkUserPackageAvailability(request.getUserId());
        if (!adPackageAvailability) {
            throw new EmlakBuradaException(ExceptionMessages.AD_NOT_RIGHTS);
        }

        UserResponse userResponse = userService.getUserById(request.getUserId());

        Ad ad = AdConverter.convert(request, userResponse.getId());

        adRepository.save(ad);

        log.info("ad saved. customer id:{} : ad:{}", userResponse.getId(), ad.toString());

        adPackageClient.updatePackageRights(request.getUserId());

        notificationProducer.sendNotification(prepareNotificationDto(NotificationType.PUSH_NOTIFICATION, ad.getId()));

    }

    public void update(Long adId, AdSaveRequest request) {
        Ad ad = getById(adId);

        Field[] fields = AdSaveRequest.class.getDeclaredFields();

        for (Field requestField : fields) {
            try {
                requestField.setAccessible(true);
                Object value = requestField.get(request);
                if (value != null) {
                    Field adField = Ad.class.getDeclaredField(requestField.getName());
                    adField.setAccessible(true);
                    adField.set(ad, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("No such field or unable to access field: " + requestField.getName());
            }
        }

        adRepository.save(ad);
    }


    @Transactional(readOnly = true)
    public AdResponseWrapper  getAll(AdSearchRequest request) {

        Specification<Ad> adSpecification = AdSpecification.initAdSpecification(request);

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.ASC, "location"));

        Page<Ad> ads = adRepository.findAll(adSpecification, pageRequest);

        long totalRecords = ads.getTotalElements();

        log.info("retrived from db. ads size:{}", totalRecords);

        Set<AdResponse> adResponses = AdConverter.toResponse(ads.stream().toList());

        return AdResponseWrapper.of(adResponses, totalRecords);
    }

    public Ad getById(Long id) {
        Optional<Ad> foundAd = adRepository.findById(id);
        if (foundAd.isEmpty()) {
            log.error(ExceptionMessages.AD_NOT_FOUND);
            throw new EmlakBuradaException(ExceptionMessages.AD_NOT_FOUND);
        }
        return foundAd.get();
    }

    @Transactional(readOnly = true)
    public AdResponse getAdResponseById(Long id) {
        Ad ad = getById(id);
        UserResponse userResponse = userService.getUserById(ad.getUserId());
        return AdConverter.toResponseWithUser(ad, userResponse);
    }

    public void updateStatus(Long adId, AdUpdateStatusRequest request) {
        Ad ad = getById(adId);
        ad.setAdStatus(request.getAdStatus());
        adRepository.save(ad);
    }

    public void delete(Long adId) {
        Ad ad = getById(adId);
        adRepository.delete(ad);
    }

    private NotificationDto prepareNotificationDto(NotificationType type, Long id) {
        return NotificationDto.builder()
                .notificationType(type)
                .id(id)
                .build();
    }

}
