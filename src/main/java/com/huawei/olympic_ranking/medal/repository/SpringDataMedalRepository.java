package com.huawei.olympic_ranking.medal.repository;

import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.medal.model.entity.MedalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMedalRepository extends JpaRepository<MedalEntity, Long> {

    boolean existsByAthleteIdAndSportIdAndType(Long athleteId, Long sportId, MedalType type);

}
