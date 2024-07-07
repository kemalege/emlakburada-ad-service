package com.patika.emlakburadaadservice.dto.response;

import com.patika.emlakburadaadservice.model.enums.AdStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdResponse {

    private Long id;
    private LocalDateTime createDate;
    private AdStatus adStatus;
    private String adCode;
}
