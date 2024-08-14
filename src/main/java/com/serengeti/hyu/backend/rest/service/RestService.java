package com.serengeti.hyu.backend.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
public class RestService {

    @Value("${openApi.API_URL}")
    private String apiUrl;

    @Value("${openApi.key}")
    private String key;

    @Value("${openApi.serviceName}")
    private String serviceName;

    @Value("${openApi.dataType}")
    private String type;

    @Autowired
    private RestRepository restRepository;// 주입된 Repository

    public void fetchAndSaveCulturalEventInfo(Integer startIndex, Integer endIndex, String codename, String title, String date) throws IOException {
        // API 호출
        String jsonResponse = getCulturalEventInfo(startIndex, endIndex, codename, title, date);

        // JSON -> DTO 변환
        List<RestDto> dtoList = parseJsonToDTO(jsonResponse);

        // DTO -> 엔티티 저장
        saveDTOToEntity(dtoList);
    }

    public String getCulturalEventInfo(Integer startIndex, Integer endIndex, String codename, String title, String date) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result;

        try {
            String urlStr = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .pathSegment(key, type, serviceName)
                    .pathSegment(startIndex.toString(), endIndex.toString())
                    .queryParam("CODENAME", Optional.ofNullable(codename).filter(c -> !c.isEmpty()))
                    .queryParam("TITLE", Optional.ofNullable(title).filter(c -> !c.isEmpty()))
                    .queryParam("DATE", Optional.ofNullable(date))
                    .build()
                    .toUriString();

            System.out.println("Generated URL: " + urlStr);

            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);

            if (stream != null) stream.close();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }

    //JSON -> DTO저장
    private List<RestDto> parseJsonToDTO(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<RestDto> dtoList = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode rowArray = rootNode.path("culturalEventInfo").path("row");

            if (rowArray.isArray()) {
                for (JsonNode node : rowArray) {
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
        } catch (IOException e) {
            e.printStackTrace();
            //에러처리
        }

        return dtoList;
    }

    private void saveDTOToEntity(List<RestDto> dtoList) {
        List<Rest> restList = new ArrayList<>();

        for (RestDto dto : dtoList) {
            Rest rest = new Rest();
            rest.setRestName(dto.getRestName());
            rest.setDescription(dto.getDescription());
            rest.setCategory(dto.getCategory());
            rest.setImage(dto.getImage());
            rest.setLink(dto.getLink());
            rest.setPlace(dto.getPlace());

            restList.add(rest);
        }

        restRepository.saveAll(restList);
    }


    private InputStream getNetworkConnection(HttpURLConnection urlConnection) throws IOException {
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);

        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code : " + urlConnection.getResponseCode());
        }

        return urlConnection.getInputStream();
    }

    private String readStreamToString(InputStream stream) throws IOException {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                result.append(readLine).append("\n");
            }
        }

        return result.toString();
    }
}

