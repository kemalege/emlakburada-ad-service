package com.patika.emlakburadaadservice.client.adpackage;

import com.patika.emlakburadaadservice.dto.response.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "package-service", url = "localhost:8098/api/v1/packages")
public interface AdPackageClient {

    @GetMapping("/{userId}/check-availability")
    GenericResponse<Boolean> checkPackageAvailability(@PathVariable("userId") Long userId);

    @PostMapping("{userId}/update-rights")
    ResponseEntity<Void> updatePackageRights(@PathVariable("userId") Long userId);

}
