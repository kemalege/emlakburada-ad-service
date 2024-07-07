package com.patika.emlakburadaadservice.client.user.service;

import com.patika.emlakburadaadservice.client.user.UserClient;
import com.patika.emlakburadaadservice.client.user.dto.response.UserResponse;
import com.patika.emlakburadaadservice.dto.response.GenericResponse;
import com.patika.emlakburadaadservice.exception.EmlakBuradaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserClient userClient;

    public UserResponse getUserById(Long userId) {
        GenericResponse<UserResponse> response = userClient.getById(userId);

        if (response == null || !HttpStatus.OK.equals(response.getHttpStatus())) {
            String errorMessage = response != null ? response.getMessage() : "Response is null";
            log.error("Error Message: {}", errorMessage);
            throw new EmlakBuradaException("User bulunamadı veya user getirilirken bir sorun oluştu.");
        }

        return response.getData();
    }

}
