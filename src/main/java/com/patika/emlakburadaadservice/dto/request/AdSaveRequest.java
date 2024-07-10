package com.patika.emlakburadaadservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdSaveRequest {

    private Long userId;
    private String title;
    private String category;
    private BigDecimal price;
    private String details;
    private String imageUrl;
    private String location;
}
