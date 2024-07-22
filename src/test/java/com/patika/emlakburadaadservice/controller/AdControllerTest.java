package com.patika.emlakburadaadservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patika.emlakburadaadservice.dto.request.AdSaveRequest;
import com.patika.emlakburadaadservice.dto.request.AdSearchRequest;
import com.patika.emlakburadaadservice.dto.request.AdUpdateStatusRequest;
import com.patika.emlakburadaadservice.dto.response.AdResponse;
import com.patika.emlakburadaadservice.dto.response.AdResponseWrapper;
import com.patika.emlakburadaadservice.model.Ad;
import com.patika.emlakburadaadservice.service.AdService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AdController.class)
public class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdService adService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAd_successful() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        AdSaveRequest request = Instancio.of(AdSaveRequest.class).create();

        // when - then
        ResultActions resultActions = mockMvc.perform(post("/api/v1/ads")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        //then - assertion
        resultActions.andExpect(status().isCreated());
        verify(adService, times(1)).create(Mockito.any(AdSaveRequest.class));
    }

    @Test
    void search_successful() throws Exception {
        // given
        AdSearchRequest request = Instancio.of(AdSearchRequest.class).create();
        AdResponse adResponse = Instancio.of(AdResponse.class).create();
        Set<AdResponse> adResponses = Collections.singleton(adResponse);
        long totalRecords = 1L;

        AdResponseWrapper responseWrapper = AdResponseWrapper.of(adResponses, totalRecords);

        // Mock behavior
        when(adService.getAll(any(AdSearchRequest.class))).thenReturn(responseWrapper);

        // when - then
        mockMvc.perform(get("/api/v1/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(adService, times(1)).getAll(Mockito.any(AdSearchRequest.class));
    }

    @Test
    void getAdById_successful() throws Exception {
        // given
        Long adId = 1L;
        AdResponse adResponse = Instancio.of(AdResponse.class).create();

        // Mock behavior
        when(adService.getAdResponseById(anyLong())).thenReturn(adResponse);

        // when - then
        mockMvc.perform(get("/api/v1/ads/{id}", adId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adService, times(1)).getAdResponseById(adId);
    }

    @Test
    void updateStatus() throws Exception {
        // given
        Long adId = 2L;
        AdUpdateStatusRequest request = Instancio.of(AdUpdateStatusRequest.class).create();

        // when - then
        mockMvc.perform(post("/api/v1/ads/{id}/status", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(adService, times(1)).updateStatus(Mockito.eq(adId), Mockito.any(AdUpdateStatusRequest.class));
    }

    @Test
    void updateAd_successful() throws Exception {
        // given
        Long adId = 2L;
        AdSaveRequest request = Instancio.of(AdSaveRequest.class).create();

        // when - then
        mockMvc.perform(post("/api/v1/ads/{id}/update", adId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(adService, times(1)).update(Mockito.eq(adId), Mockito.any(AdSaveRequest.class));
    }

    @Test
    void deleteAd_successful() throws Exception {
        // given
        Long adId = 2L;
        // when - then
        mockMvc.perform(delete("/api/v1/ads/{id}/delete", adId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adService, times(1)).delete(Mockito.eq(adId));
    }
}
