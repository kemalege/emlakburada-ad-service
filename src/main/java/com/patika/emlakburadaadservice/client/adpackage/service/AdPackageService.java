package com.patika.emlakburadaadservice.client.adpackage.service;

import com.patika.emlakburadaadservice.client.adpackage.AdPackageClient;
import com.patika.emlakburadaadservice.client.adpackage.response.AdPackageAvailabilityResponse;
import com.patika.emlakburadaadservice.dto.response.GenericResponse;
import com.patika.emlakburadaadservice.exception.EmlakBuradaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdPackageService {

    private final AdPackageClient adPackageClient;

    public Boolean checkUserPackageAvailability(Long userId) {
        GenericResponse<Boolean> response = adPackageClient.checkPackageAvailability(userId);

        if (response == null || !HttpStatus.OK.equals(response.getHttpStatus())) {
            String errorMessage = response != null ? response.getMessage() : "Paket bulunamadı veya paket getirilirken bir sorun oluştu";
            log.error("Error Message: {}", errorMessage);
            throw new EmlakBuradaException(errorMessage);
        }

        return response.getData();
    }

}
