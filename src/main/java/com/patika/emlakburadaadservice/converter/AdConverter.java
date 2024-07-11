package com.patika.emlakburadaadservice.converter;

import com.patika.emlakburadaadservice.client.user.dto.response.UserResponse;
import com.patika.emlakburadaadservice.dto.request.AdSaveRequest;
import com.patika.emlakburadaadservice.dto.response.AdResponse;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.model.enums.AdStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
                .title(request.getTitle())
                .price(request.getPrice())
                .category(request.getCategory())
                .location(request.getLocation())
                .adDetails(request.getDetails())
                .imageUrl(request.getImageUrl())
                .adStatus(AdStatus.IN_REVIEW)
                .adCode(adCode)
                .userId(userId)
                .build();
    }

    public static AdResponse toResponseWithUser(Ad ad, UserResponse userResponse) {
        return AdResponse.builder()
                .title(ad.getTitle())
                .price(ad.getPrice())
                .category(ad.getCategory())
                .location(ad.getLocation())
                .details(ad.getAdDetails())
                .imageUrl(ad.getImageUrl())
                .adCode(ad.getAdCode())
                .adStatus(ad.getAdStatus())
                .id(ad.getId())
                .user(userResponse)
                .createDate(ad.getCreateDate())
                .build();
    }

    public static AdResponse toResponse(Ad ad) {
        return AdResponse.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .price(ad.getPrice())
                .category(ad.getCategory())
                .location(ad.getLocation())
                .details(ad.getAdDetails())
                .imageUrl(ad.getImageUrl())
                .adCode(ad.getAdCode())
                .adStatus(ad.getAdStatus())
                .createDate(ad.getCreateDate())
                .userId(ad.getUserId())
                .build();
    }

    public static Set<AdResponse> toResponse(List<Ad> ads) {
        return ads.stream()
                .map(AdConverter::toResponse)
                .collect(Collectors.toSet());
    }

    public static Set<AdResponse> toResponseWithUser(List<Ad> ads, Map<Long, UserResponse> userResponses) {
        return ads.stream()
                .map(ad -> toResponseWithUser(ad, userResponses.get(ad.getUserId())))
                .collect(Collectors.toSet());
    }
}
