package com.serengeti.hyu.backend.rest.controller;

import com.serengeti.hyu.backend.rest.dto.RestDto;
import com.serengeti.hyu.backend.rest.service.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/hue-activity")
public class RestController {

    private final RestService restService;

    public RestController(RestService restService) {
        this.restService = restService;
    }

    @GetMapping("/recommend")
    public ResponseEntity<String> getCulturalEventInfo(
            @RequestParam(value = "start_index", defaultValue = "1") Integer startIndex,
            @RequestParam(value = "end_index", defaultValue = "20") Integer endIndex,
            @RequestParam(value = "codename", required = false) String codename,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "date", required = false) String date
    ){
        try {
            restService.fetchAndSaveCulturalEventInfo(startIndex, endIndex, codename, title, date);
            return ResponseEntity.ok("문화이벤트 DB저장 완료");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB저장 Error");
        }
    }
}

