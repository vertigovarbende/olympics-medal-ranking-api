package com.huawei.olympic_ranking.athlete.model.entity;

import com.huawei.olympic_ranking.country.model.entity.CountryEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "athlete")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class AthleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;

}
