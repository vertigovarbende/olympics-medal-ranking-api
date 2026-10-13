package com.huawei.olympic_ranking.medal.port;

import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;

public interface MedalRepository {

    Medal save(Medal medal);

    boolean existsByAthleteIdAndSportIdAndType(Long athleteId, Long sportId, MedalType type);

}
