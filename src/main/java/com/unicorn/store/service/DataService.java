package com.unicorn.store.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.store.data.BoardRepository;
import com.unicorn.store.data.GptsFundinfoRepository;
import com.unicorn.store.data.UserRepository;
import com.unicorn.store.dto.Data.BoardReq;
import com.unicorn.store.dto.Data.BoardRes;
import com.unicorn.store.exceptions.http.CustomNotFoundException;
import com.unicorn.store.model.Board;
import com.unicorn.store.model.GptsFundinfo;
import com.unicorn.store.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final GptsFundinfoRepository gptsFundinfoRepository;


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
        log.info(response);
        // 정규 표현식을 사용하여 포트폴리오 부분 추출
        Pattern pattern = Pattern.compile("\\{\\s*\"Domestic Equity Fund\"[\\s\\S]*?\\}");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "No portfolio found in the response.";
        }
    }

    /**
     * 펀드 추천
     */
    public Map<String, String> getRandomColumnWithValue() {
        Random random = new Random();
        Long randomId = (long) (random.nextInt(3) + 1); // assuming IDs are 1, 2, 3

        GptsFundinfo fundinfo = gptsFundinfoRepository.findById(randomId).orElse(null);
        if (fundinfo == null) {
            return null;
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("name", fundinfo.getName());
        resultMap.put("asset_management", fundinfo.getAssetManagement());
        resultMap.put("total_cost", fundinfo.getTotalCost());
        resultMap.put("three_months_return", fundinfo.getThreeMonthsReturn());

        return resultMap;
    }

    /**
     * 보드 저장
     */
    public Long writeBoard(BoardReq.BoardWriteRequest BoardRequest) {
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId).orElse(null);

        Board board = Board.builder()
                .user(user)
                .house(BoardRequest.getHouse())
                .now(BoardRequest.getNow())
                .time(BoardRequest.getTime())
                .preference(BoardRequest.getPreference())
                .living(BoardRequest.getLiving())
                .gpt(BoardRequest.getGpt())
                .build();
        boardRepository.save(board);
        return board.getBoardId();
    }

    /**
     * 보드 목록
     */
    @Transactional
    public BoardRes.Multiple listDrawing() {
        //Long userId = loginService.getLoginUserId();
        List<Board> boards = boardRepository.findAll();

        List<BoardRes.Base> userDrawingList = boards.stream()
                .map(board -> BoardRes.Base.of(board))
                .collect(Collectors.toList());

        return BoardRes.Multiple.of(userDrawingList);
    }






}
