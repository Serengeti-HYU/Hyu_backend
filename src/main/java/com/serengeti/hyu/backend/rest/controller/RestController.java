package com.serengeti.hyu.backend.rest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/hue-activity")
public class RestController {

    @Value("${openApi.API_URL}")
    private String apiUrl;

    @Value("${openApi.key}")
    private String key;

    @Value("${openApi.serviceName}")
    private String serviceName;

    @Value("${openApi.dataType}")
    private String type;

    @GetMapping("/recommend")
    public ResponseEntity<String> getCulturalEventInfo(
            @RequestParam(value="start_index", defaultValue = "1") Integer startIndex,
            @RequestParam(value="end_index", defaultValue = "50") Integer endIndex,
            @RequestParam(value="codename", required = false) String codename,
            @RequestParam(value="title", required = false) String title,
            @RequestParam(value="date", required = false) String date
    ) {
        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        String result;

        try {
            // URL 생성 -> pathSegment
            String urlStr = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .pathSegment(key, type, serviceName)
                    .pathSegment(startIndex.toString(), endIndex.toString())
                    .queryParam("CODENAME", Optional.ofNullable(codename).filter(c -> !c.isEmpty()))
                    .queryParam("TITLE", Optional.ofNullable(title).filter(c -> !c.isEmpty()))
                    .queryParam("DATE", Optional.ofNullable(date))
                    .build()
                    .toUriString();

            System.out.println("Generated URL: " + urlStr);  // URL 출력해보기

            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);

            if (stream != null) stream.close();
        } catch(IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error while calling API: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        // 응답 데이터의 형식이 JSON인지 확인 후 -> Content-Type을 application/json으로 설정
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
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
