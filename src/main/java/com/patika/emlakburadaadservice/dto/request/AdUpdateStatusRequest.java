package com.patika.emlakburadaadservice.dto.request;

import com.patika.emlakburadaadservice.model.enums.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdUpdateStatusRequest {

    private AdStatus adStatus;

}
