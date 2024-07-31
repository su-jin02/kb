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
@Table(name = "gpts_fundinfo")
public class GptsFundinfo {
    @Id
    private Long id;
    private String name;
    private String assetManagement;
    private String totalCost;
    private String threeMonthsReturn;
}
