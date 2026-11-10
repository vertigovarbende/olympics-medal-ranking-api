package com.huawei.olympic_ranking.medal.model.entity;

import com.huawei.olympic_ranking.athlete.model.entity.AthleteEntity;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.sport.model.entity.SportEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "medal",
        uniqueConstraints = @UniqueConstraint(name = "uk_medal_award", columnNames = {"athlete_id", "sport_id", "medal_type"})
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MedalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "athlete_id", nullable = false)
    private AthleteEntity athlete;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id", nullable = false)
    private SportEntity sport;

    @Enumerated(EnumType.STRING)
    @Column(name = "medal_type", nullable = false, length = 6)
    private MedalType type;

}
