package com.huawei.olympic_ranking.athlete.controller.impl;

import com.huawei.olympic_ranking.athlete.controller.AthleteController;
import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.model.mapper.AthleteApplicationMapper;
import com.huawei.olympic_ranking.athlete.model.request.AthleteCreateRequest;
import com.huawei.olympic_ranking.athlete.model.response.AthleteResponse;
import com.huawei.olympic_ranking.athlete.service.AthleteUseCase;
import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/athletes")
@RequiredArgsConstructor
public class AthleteControllerImpl implements AthleteController {

    private final AthleteUseCase athleteUseCase;
    private final AthleteApplicationMapper athleteMapper;

    @Override
    @PostMapping
    public BaseResponse<AthleteResponse> create(final AthleteCreateRequest request) {

        final Athlete created = athleteUseCase.create(request.name(), request.countryCode());

        return BaseResponse.success(athleteMapper.toResponse(created));
    }

}
