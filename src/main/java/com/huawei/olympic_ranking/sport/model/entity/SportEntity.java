package com.huawei.olympic_ranking.sport.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "sport",
        uniqueConstraints = @UniqueConstraint(name = "uk_sport_name", columnNames = "name")
)
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

}
