package com.serengeti.hyu.backend.rest.controller;

import com.serengeti.hyu.backend.rest.service.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


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
            @RequestParam(value = "end_index", defaultValue = "50") Integer endIndex,
            @RequestParam(value = "codename", required = false) String codename,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "date", required = false) String date
    ) {
        try {
            String result = restService.getCulturalEventInfo(startIndex, endIndex, codename, title, date);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error while calling API: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

