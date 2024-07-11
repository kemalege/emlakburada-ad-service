package com.patika.emlakburadaadservice.controller;

import com.patika.emlakburadaadservice.dto.request.AdSaveRequest;
import com.patika.emlakburadaadservice.dto.request.AdSearchRequest;
import com.patika.emlakburadaadservice.dto.request.AdUpdateStatusRequest;
import com.patika.emlakburadaadservice.dto.response.AdResponse;
import com.patika.emlakburadaadservice.dto.response.GenericResponse;
import com.patika.emlakburadaadservice.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ads")
public class AdController {

    private final AdService adService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody AdSaveRequest request){
        adService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GenericResponse<Set<AdResponse>>> getAll(@RequestBody AdSearchRequest request) {
        return new ResponseEntity<>(GenericResponse.success(adService.getAll(request)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<AdResponse>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(GenericResponse.success(adService.getAdResponseById(id)), HttpStatus.OK);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody AdUpdateStatusRequest request){
        adService.updateStatus(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody AdSaveRequest request) {
        adService.update(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}