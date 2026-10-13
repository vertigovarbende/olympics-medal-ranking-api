package com.huawei.olympic_ranking.medal.model.mapper;

import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.response.MedalResponse;
import org.springframework.stereotype.Component;

@Component
public class MedalApplicationMapper {

    public MedalResponse toResponse(final Medal medal) {
        return new MedalResponse(
                medal.getId(),
                medal.getAthlete().getId(),
                medal.getSport().getId(),
                medal.getType(),
                medal.country().getCode());
    }

}
