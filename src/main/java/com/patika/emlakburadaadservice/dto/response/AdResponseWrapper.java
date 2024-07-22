package com.patika.emlakburadaadservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Set;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdResponseWrapper {

    private Set<AdResponse> ads;
    private long totalRecords;

    public static AdResponseWrapper of(Set<AdResponse> ads, long totalRecords) {
        return new AdResponseWrapper(ads, totalRecords);
    }

}
