package com.patika.emlakburadaadservice.service;

import com.patika.emlakburadaadservice.client.user.dto.response.UserResponse;
import com.patika.emlakburadaadservice.client.user.service.UserService;
import com.patika.emlakburadaadservice.converter.AdConverter;
import com.patika.emlakburadaadservice.dto.request.AdSaveRequest;
import com.patika.emlakburadaadservice.dto.request.AdSearchRequest;
import com.patika.emlakburadaadservice.dto.response.AdResponse;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.repository.AdRepository;
import com.patika.emlakburadaadservice.repository.specification.AdSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdService {

    private final AdRepository adRepository;
    private final UserService userService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(AdSaveRequest request) {

        UserResponse userResponse = userService.getUserById(request.getUserId());

        Ad ad = AdConverter.convert(request, userResponse.getId());

        adRepository.save(ad);

        log.info("order saved. customer id:{} : order:{}", userResponse.getId(), ad.toString());

    }

    @Transactional(readOnly = true)
    public Set<AdResponse> getAll(AdSearchRequest request) {

        Specification<Ad> adSpecification = AdSpecification.initAdSpecification(request);

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.ASC, "adCode"));

        Page<Ad> ads = adRepository.findAll(adSpecification, pageRequest);

        log.info("retrived from db. ads size:{}", ads.getSize());

        return AdConverter.toResponse(ads.stream().toList());
    }

}
