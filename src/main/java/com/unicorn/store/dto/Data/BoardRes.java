package com.unicorn.store.dto.Data;

import com.unicorn.store.model.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BoardRes {
    @Getter
    @Setter
    @Builder
    @Schema(name = "보드 정보 객체", description = "보드 정보 객체")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Base {

        @Schema(description = "유저 닉네임")
        String userNickname;

        @Schema(description = "꿈의 집")
        String house;

        @Schema(description = "현재 자금")
        String now;

        @Schema(description = "목표 년")
        String time;

        @Schema(description = "투자 성향")
        String preference;

        @Schema(description = "생활비")
        String living;

        @Schema(description = "포토폴리오 gpt 답변")
        String gpt;

        public Map<String, String> toKoreanMap() {
            Map<String, String> koreanMap = new HashMap<>();
            koreanMap.put("집 이름", house);
            koreanMap.put("현재 자금", now);
            koreanMap.put("목표 년", time);
            koreanMap.put("투자 성향", preference);
            koreanMap.put("생활비", living);
            koreanMap.put("포토폴리오 gpt 답변", gpt);
            koreanMap.put("유저 닉네임", userNickname);
            return koreanMap;
        }

        public static BoardRes.Base of(Board board) {
            return Base.builder()
                    .house(board.getHouse())
                    .now(board.getNow())
                    .time(board.getTime())
                    .preference(board.getPreference())
                    .living(board.getLiving())
                    .gpt(board.getGpt())
                    .userNickname(board.getUser() != null ? board.getUser().getNickname() : "Anonymous")
                    .build();
        }
    }
    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Schema(name = "보드 페이지 정보 객체", description = "보드 페이지 정보 객체")
    public static class Multiple {

        List<BoardRes.Base> drawingList;

        public static BoardRes.Multiple of(List<BoardRes.Base> drawingList) {
            return BoardRes.Multiple.builder()
                    .drawingList(drawingList)
                    .build();
        }
    }


}
