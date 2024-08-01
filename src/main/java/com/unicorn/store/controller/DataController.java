package com.unicorn.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.store.dto.Data.BoardRes;
import com.unicorn.store.security.dto.TokenResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.unicorn.store.data.BoardRepository;
import com.unicorn.store.dto.Data.BoardReq;
import com.unicorn.store.response.BaseResponse;
import com.unicorn.store.response.BaseResponseStatus;
import com.unicorn.store.service.DataService;
import com.unicorn.store.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "MyData", description = "마이데이터 관련 API")
@Controller
@Slf4j
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @Operation(summary = "GPT API 포토폴리오 생성", description = "GPT API 포토폴리오 생성 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "gpt 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/get-data")
    public ResponseEntity<Map<String, String>> getData(@Parameter(description = "데이터 보드 요청 객체") @Valid @RequestBody BoardReq.BoardRequest BoardRequest) {
        String result = dataService.getDjangoData(BoardRequest);
        String cleanedResult = result.replace("{", "").replace("}", "");
        Map<String, String> response = new HashMap<>();
        response.put("result", cleanedResult);
        Map<String, String> randomColumn = dataService.getRandomColumnWithValue();
        for (Map.Entry<String, String> entry : randomColumn.entrySet()) {
            log.info("Key: {}, Value: {}", entry.getKey(), entry.getValue());
        }
        response.putAll(randomColumn);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "마이데이터 보드 목록", description = "마이데이터 보드 목록 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보드 목록 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/")
    @Transactional
    public String userDrawingList(Model model, @ModelAttribute("tokenResponseJson") String tokenResponseJson) throws JsonProcessingException {
        // 닉네임이 있는 경우에만 모델에 추가
        if (tokenResponseJson != null && !tokenResponseJson.isEmpty()) {
            System.out.println("Token Response JSON: " + tokenResponseJson);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponseDto tokenResponse = objectMapper.readValue(tokenResponseJson, TokenResponseDto.class);
                if (tokenResponse != null && tokenResponse.getNickname() != null) {
                    model.addAttribute("nickname", tokenResponse.getNickname());
                }
            } catch (JsonProcessingException e) {
                System.err.println("JSON 처리 중 오류 발생: " + e.getMessage());
            }
        }


        // 보드 리스트를 가져와서 모델에 추가
        BoardRes.Multiple boards = dataService.listDrawing();
        List<Map<String, String>> boardList = boards.getDrawingList().stream()
                .map(BoardRes.Base::toKoreanMap)
                .collect(Collectors.toList());
        model.addAttribute("boards", boards.getDrawingList());

        return "board"; // HTML 파일명과 일치해야 합니다.
    }

    @Operation(summary = "마이데이터 보드 작성", description = "마이데이터 보드 작성 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/write")
    public BaseResponse<Long> writeData(@Parameter(description = "데이터 보드 작성 요청 객체") @Valid @RequestBody BoardReq.BoardWriteRequest BoardRequest) {
        return BaseResponse.success(BaseResponseStatus.CREATED, dataService.writeBoard(BoardRequest));
    }

    @GetMapping("/write")
    public String getWelcomeMessage() {
        return "write";
    }
}
