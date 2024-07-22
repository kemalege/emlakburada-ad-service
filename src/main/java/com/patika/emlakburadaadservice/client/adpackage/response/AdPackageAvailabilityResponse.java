package com.patika.emlakburadaadservice.client.adpackage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdPackageAvailabilityResponse {

    private Boolean available;
    private String message;

}

