package com.serengeti.hyu.backend.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serengeti.hyu.backend.character.dto.CharacterResponseDto;
import com.serengeti.hyu.backend.character.enums.ResultType;
import com.serengeti.hyu.backend.character.service.CharacterService;
import com.serengeti.hyu.backend.rest.dto.RestDto;
import com.serengeti.hyu.backend.rest.entity.Rest;
import com.serengeti.hyu.backend.rest.repository.RestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestService {

//    @Value("${openApi.API_URL}")
    private String apiUrl;

//    @Value("${openApi.key}")
    private String apiKey;

//    @Value("${openApi.serviceName}")
    private String serviceName;

//    @Value("${openApi.dataType}")
    private String dataType;

    @Autowired
    private RestRepository restRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CharacterService characterService;


    public void fetchAndSaveCulturalEventInfo(Integer startIndex, Integer endIndex, String codename, String title, String date) throws IOException {
        // API 호출
        String openAPI = fetchEvent(startIndex, endIndex, codename, title, date);

        // OpenAPI(JSON) -> DTO 변환
        List<RestDto> dtoList = parseJson(openAPI);

        // DTO -> 엔티티 저장 -> 여기서 데이터 저장이 나뉨
        saveRestData(dtoList);
    }

    private String fetchEvent(Integer startIndex, Integer endIndex, String codename, String title, String date) throws IOException {
        String urlStr = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .pathSegment(apiKey, dataType, serviceName)
                .pathSegment(startIndex.toString(), endIndex.toString())
                .queryParam("CODENAME", Optional.ofNullable(codename).filter(c -> !c.isEmpty()).orElse(null))
                .queryParam("TITLE", Optional.ofNullable(title).filter(t -> !t.isEmpty()).orElse(null))
                .queryParam("DATE", date)
                .build()
                .toUriString();

        System.out.println("Generated URL: " + urlStr);

        HttpURLConnection urlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setRequestMethod("GET");

        try (InputStream inputStream = getInputStream(urlConnection)) {
            return readStream(inputStream);
        } finally {
            urlConnection.disconnect();
        }
    }

    private InputStream getInputStream(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + connection.getResponseCode());
        }
        return connection.getInputStream();
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    private List<RestDto> parseJson(String jsonResponse) throws IOException {
        List<RestDto> dtoList = new ArrayList<>();
        JsonNode rows = objectMapper.readTree(jsonResponse)
                .path("culturalEventInfo").path("row");

        if (rows.isArray()) {
            for (JsonNode node : rows) {
                RestDto dto = new RestDto();
                dto.setRestName(node.path("TITLE").asText());
                dto.setDescription(node.path("PROGRAM").asText());
                dto.setCategory(node.path("CODENAME").asText());
                dto.setImage(node.path("MAIN_IMG").asText());
                dto.setLink(node.path("ORG_LINK").asText());
                dto.setPlace(node.path("PLACE").asText());
                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    private void saveRestData(List<RestDto> dtoList){
        List<Rest> restList = dtoList.stream()
                .filter(dto -> !restRepository.existsByRestName(dto.getRestName()))
                .map(dto -> {
            Rest rest = new Rest();
            rest.setRestName(dto.getRestName());
            rest.setDescription(dto.getDescription());
            rest.setCategory(dto.getCategory());
            rest.setImage(dto.getImage());
            rest.setLink(dto.getLink());
            rest.setPlace(dto.getPlace());
            return rest;
        }).collect(Collectors.toList());

        restRepository.saveAll(restList);
    }


    // 데이터 조회 메서드 추가
    public List<RestDto> getRestData(Long userId) {
        // 성격유형이 없어도 일단은 목록조회는 되어야 하니
        ResultType resultType = null;

        try {
            CharacterResponseDto characterResponse = characterService.getCharacterResult(userId);
            if (characterResponse != null) {
                resultType = characterResponse.getResultType();
            }
        } catch (RuntimeException e) {
            // 성격 정보가 없는경우
            resultType = null;
        }

        List<Rest> events = restRepository.findAll();

        // 성격 유형이 있는경우와 없는경우
        List<Rest> filteredEvents;
        if (resultType == null) {
            filteredEvents = events; // 성격 유형이 없으면 전체 목록
        } else {
            filteredEvents = new ArrayList<>();
            for (Rest event : events) {
                if (filterResult(event, resultType)) {
                    filteredEvents.add(event);
                }
            }
        }

        return filteredEvents.stream()
                .map(event -> {
                    RestDto dto = new RestDto();
                    dto.setRestId(event.getRestId());
                    dto.setRestName(event.getRestName());
                    dto.setDescription(event.getDescription());
                    dto.setCategory(event.getCategory());
                    dto.setImage(event.getImage());
                    dto.setLink(event.getLink());
                    dto.setPlace(event.getPlace());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 상세 조회
    public Rest getRestById(int restId) {
        return restRepository.findById(restId).orElse(null);
    }


    // 성격 유형에 따라 카테고리 필터링
    private boolean filterResult(Rest event, ResultType resultType) {
        switch (resultType) {
            case RESULT_1: // 휴일은 하우스키퍼 유형
                return event.getCategory().equals("기타") ||
                        event.getCategory().equals("독주/독창회");
            case RESULT_2: // 액티브하게 휴식 유형
                return event.getCategory().equals("교육/체험") ||
                        event.getCategory().equals("축제-기타") ||
                        event.getCategory().equals("축제-문화/예술");
            case RESULT_3: // 스트레스 OUT! 힐링 유형
                return event.getCategory().equals("클래식") ||
                        event.getCategory().equals("독주/독창회") ||
                        event.getCategory().equals("연극");
            case RESULT_4: // 지겹지 않게 항상 다른 휴식 유형
                return true; // 모든 쉼활동
            default:
                return true;
        }
    }
}
