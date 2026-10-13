package com.huawei.olympic_ranking.athlete.repository;

import com.huawei.olympic_ranking.athlete.model.entity.AthleteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataAthleteRepository extends JpaRepository<AthleteEntity, Long> {

}
