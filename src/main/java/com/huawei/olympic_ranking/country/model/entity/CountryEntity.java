package com.huawei.olympic_ranking.country.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "country",
        uniqueConstraints = @UniqueConstraint(name = "uk_country_code", columnNames = "code")
)
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, length = 3)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

}
