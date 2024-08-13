package com.serengeti.hyu.backend.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest(RestController.class)
//@AutoConfigureMockMvc(addFilters = false)  // Security 필터 비활성화
public class RestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Open API 데이터 조회 테스트")
    public void testGetCulturalEventInfo() throws Exception {
        String date = "2024-09"; // 유효한 날짜

        // 요청 생성 및 테스트 수행
        this.mvc.perform(get("/hue-activity/recommend")
                        .param("date", date))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }
}
