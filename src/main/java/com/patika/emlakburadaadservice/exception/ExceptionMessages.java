package com.patika.emlakburadaadservice.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessages {
    public static final String AD_NOT_FOUND = "İlan bulunamadı.";
    public static final String AD_NOT_RIGHTS = "İlan yayınlama hakkınız bulunmamaktadır.";
}
