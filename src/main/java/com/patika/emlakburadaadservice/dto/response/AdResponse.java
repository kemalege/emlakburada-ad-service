package com.patika.emlakburadaadservice.dto.response;

import com.patika.emlakburadaadservice.client.user.dto.response.UserResponse;
import com.patika.emlakburadaadservice.model.enums.AdStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdResponse {

    private Long id;
    private Long userId;
    private UserResponse user;
    private LocalDateTime createDate;
    private AdStatus adStatus;
    private String adCode;
    private String title;
    private String category;
    private BigDecimal price;
    private String details;
    private String imageUrl;
    private String location;
}
