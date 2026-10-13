package com.huawei.olympic_ranking.country.repository;

import com.huawei.olympic_ranking.country.model.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataCountryRepository extends JpaRepository<CountryEntity, Long> {

    boolean existsByCode(String code);

    Optional<CountryEntity> findByCode(String code);

}
