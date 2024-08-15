package com.serengeti.hyu.backend.rest.controller;

import com.serengeti.hyu.backend.rest.dto.RestDto;
import com.serengeti.hyu.backend.rest.entity.Rest;
import com.serengeti.hyu.backend.rest.repository.RestRepository;
import com.serengeti.hyu.backend.rest.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/hue-activity")
public class RestController {

    @Autowired
    private RestService restService;

    @Autowired
    private RestRepository restRepository;

    // 쉼활동 목록 조회
    @GetMapping("/recommend")
    public ResponseEntity<List<RestDto>> getCulturalEvents(
            @RequestParam(value = "start_index", defaultValue = "1") Integer startIndex,
            @RequestParam(value = "end_index", defaultValue = "50") Integer endIndex) {

        try {
            restService.fetchAndSaveCulturalEventInfo(startIndex, endIndex, null, null, null);

            List<RestDto> response = restService.getRestData();
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }
    }

    // 쉼활동 상세 조회
    @GetMapping("/{restId}")
    public ResponseEntity<Rest> getRestById(@PathVariable int restId) {
        Rest rest = restService.getRestById(restId);
        if (rest != null) {
            return ResponseEntity.ok(rest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
