package com.patika.emlakburadaadservice.converter;

import com.patika.emlakburadaadservice.dto.request.AdSaveRequest;
import com.patika.emlakburadaadservice.dto.response.AdResponse;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.model.enums.AdStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdConverter {

    public static Ad convert(AdSaveRequest request, Long userId) {
        String uuid = UUID.randomUUID().toString();
        String adCode = "ad-" + uuid;
        return Ad.builder()
                .createDate(LocalDateTime.now())
                .adStatus(AdStatus.IN_REVIEW)
                .adCode(adCode)
                .userId(userId)
                .build();
    }

    public static AdResponse toResponse(Ad ad) {
        return AdResponse.builder()
                .adCode(ad.getAdCode())
                .adStatus(ad.getAdStatus())
                .id(ad.getId())
                .createDate(ad.getCreateDate())
                .build();
    }

    public static Set<AdResponse> toResponse(List<Ad> ads) {
        return ads
                .stream()
                .map(AdConverter::toResponse)
                .collect(Collectors.toSet());
    }
}
