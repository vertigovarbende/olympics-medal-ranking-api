package com.huawei.olympic_ranking.medal.service;

import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;

public interface MedalUseCase {

    Medal create(Long athleteId, Long sportId, MedalType type);

}
