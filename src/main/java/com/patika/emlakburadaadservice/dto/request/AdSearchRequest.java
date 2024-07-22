package com.patika.emlakburadaadservice.dto.request;

import com.patika.emlakburadaadservice.model.enums.AdStatus;
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

    private Long userId;
    private AdStatus adStatus;
    private String sort;
}
