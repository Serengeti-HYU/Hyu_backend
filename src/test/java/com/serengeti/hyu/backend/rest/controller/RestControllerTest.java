package com.serengeti.hyu.backend.rest.controller;

import com.serengeti.hyu.backend.rest.service.RestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest(RestController.class)
public class RestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestService restService;  // RestService를 Mocking

    @Test
    @DisplayName("Open API 데이터 조회 테스트")
    public void testGetCulturalEventInfo() throws Exception {
        String date = "2024-08"; // 유효한 날짜
        String mockResponse = "{ \"status\": \"ok\", \"data\": [...] }";  // 모의 응답 데이터

        // Mocking RestService의 동작 정의
        when(restService.getCulturalEventInfo(1, 50, null, null, date))
                .thenReturn(mockResponse);

        // 요청 생성 및 테스트 수행
        this.mvc.perform(get("/hue-activity/recommend")
                        .param("date", date))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(mockResponse))  // Mock 응답과 일치하는지 확인
                .andDo(print());
    }
}
