package com.huawei.olympic_ranking.athlete.model.mapper;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.model.response.AthleteResponse;
import org.springframework.stereotype.Component;

@Component
public class AthleteApplicationMapper {

    public AthleteResponse toResponse(final Athlete athlete) {
        return new AthleteResponse(athlete.getId(), athlete.getName(), athlete.getCountry().getCode());
    }

}
