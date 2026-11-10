package com.huawei.olympic_ranking.athlete.service;

import com.huawei.olympic_ranking.athlete.model.Athlete;

public interface AthleteUseCase {

    Athlete create(String name, String countryCode);

}
