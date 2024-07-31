package com.unicorn.store.dto.Data;

import com.unicorn.store.model.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

public class BoardReq {
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Schema(title = "데이터 보드 요청 객체")
    public static class BoardRequest {
        @NotNull
        @Schema(required = true, description = "꿈의 집")
        String house;

        @NotNull
        @Schema(required = true, description = "현재 자금")
        String now;

        @NotNull
        @Schema(required = true, description = "목표 년")
        String time;

        @NotNull
        @Schema(required = true, description = "투자 성향")
        String preference;

        @NotNull
        @Schema(required = true, description = "생활비")
        String living;

        public Board toEntity(){
            return Board.builder()
                    .house(house)
                    .now(now)
                    .time(time)
                    .preference(preference)
                    .living(living)
                    .build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Schema(title = "데이터 보드 작성 객체")
    public static class BoardWriteRequest {
        @NotNull
        @Schema(required = true, description = "꿈의 집")
        String house;

        @NotNull
        @Schema(required = true, description = "현재 자금")
        String now;

        @NotNull
        @Schema(required = true, description = "목표 년")
        String time;

        @NotNull
        @Schema(required = true, description = "투자 성향")
        String preference;

        @NotNull
        @Schema(required = true, description = "생활비")
        String living;

        @NotNull
        @Schema(required = true, description = "포토폴리오 gpt 답변")
        String gpt;

        public Board toEntity(){
            return Board.builder()
                    .house(house)
                    .now(now)
                    .time(time)
                    .preference(preference)
                    .living(living)
                    .gpt(gpt)
                    .build();
        }
    }
}
