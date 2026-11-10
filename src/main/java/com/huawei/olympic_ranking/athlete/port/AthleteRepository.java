package com.huawei.olympic_ranking.athlete.port;

import com.huawei.olympic_ranking.athlete.model.Athlete;

import java.util.Optional;

public interface AthleteRepository {

    Athlete save(Athlete athlete);

    Optional<Athlete> findById(Long id);

}
