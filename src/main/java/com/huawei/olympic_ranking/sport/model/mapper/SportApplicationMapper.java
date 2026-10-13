package com.huawei.olympic_ranking.sport.model.mapper;

import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.model.response.SportResponse;
import org.springframework.stereotype.Component;

@Component
public class SportApplicationMapper {

    public SportResponse toResponse(final Sport sport) {
        return new SportResponse(sport.getId(), sport.getName());
    }

}
