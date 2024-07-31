package com.unicorn.store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long boardId;

    String house;
    String now;
    String time;
    String preference;
    String living;
    String gpt;
}
