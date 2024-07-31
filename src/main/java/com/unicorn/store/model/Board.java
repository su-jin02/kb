package com.unicorn.store.model;

import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
