package com.huawei.olympic_ranking.sport.repository;

import com.huawei.olympic_ranking.sport.model.entity.SportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSportRepository extends JpaRepository<SportEntity, Long> {

    boolean existsByNameIgnoreCase(String name);

}
