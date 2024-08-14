package com.serengeti.hyu.backend.rest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

