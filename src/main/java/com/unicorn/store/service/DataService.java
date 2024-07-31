package com.unicorn.store.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.store.dto.Data.BoardReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String getDjangoData(BoardReq.BoardRequest boardRequest) {
        String url = "http://35.89.200.64:8000/gpt/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            // JSON 직렬화
            String jsonBody = objectMapper.writeValueAsString(boardRequest);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            String jsonResponse = restTemplate.postForObject(url, requestEntity, String.class);
            // 응답에서 필요한 부분만 추출
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            String result = rootNode.path("result").asText();
            log.info(result);
            // result 필드에서 포트폴리오 부분만 추출
            String extractedResult = extractPortfolio(result);
            return extractedResult;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }

    private String extractPortfolio(String response) {
        // 정규 표현식을 사용하여 포트폴리오 부분 추출
        Pattern pattern = Pattern.compile("\\{\\s*\"Domestic Equity Fund\"[\\s\\S]*?\\}");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "No portfolio found in the response.";
        }
    }

    @GetMapping("/write")
    public String nextPage() {
        return "write";
    }

}
