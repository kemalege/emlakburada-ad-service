package com.patika.emlakburadaadservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdSearchRequest extends BaseSearchRequest {

    private Long customerId;
    private String sort;
}
