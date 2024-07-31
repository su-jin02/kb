package com.unicorn.store.controller;

import com.unicorn.store.dto.Data.BoardReq;
import com.unicorn.store.dto.common.ErrorRes;
import com.unicorn.store.response.BaseResponse;
import com.unicorn.store.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "user", description = "마이데이터 관련 API")
@Controller
@Slf4j
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @Operation(summary = "gpt", description = "gpt API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "gpt 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/get-data")
    public ResponseEntity<Map<String, String>> getData(@Parameter(description = "데이터 보드 요청 객체") @Valid @RequestBody BoardReq.BoardRequest BoardRequest) {
        String result = dataService.getDjangoData(BoardRequest);
        log.info(result);
        log.info("---------------------------------------");
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/write")
    public String getWelcomeMessage() {
        return "write";
    }

}
