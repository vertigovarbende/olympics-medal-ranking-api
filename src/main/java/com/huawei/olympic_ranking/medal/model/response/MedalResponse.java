package com.huawei.olympic_ranking.medal.model.response;

import com.huawei.olympic_ranking.medal.model.MedalType;

public record MedalResponse(Long id, Long athleteId, Long sportId, MedalType medalType, String countryCode) {

}
