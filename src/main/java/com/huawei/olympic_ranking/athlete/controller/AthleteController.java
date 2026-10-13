package com.huawei.olympic_ranking.athlete.controller;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.model.mapper.AthleteApplicationMapper;
import com.huawei.olympic_ranking.athlete.model.request.AthleteCreateRequest;
import com.huawei.olympic_ranking.athlete.model.response.AthleteResponse;
import com.huawei.olympic_ranking.athlete.service.AthleteUseCase;
import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/athletes")
@RequiredArgsConstructor
public class AthleteController {

    private final AthleteUseCase athleteUseCase;
    private final AthleteApplicationMapper athleteMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<AthleteResponse> create(@Valid @RequestBody final AthleteCreateRequest request) {

        final Athlete created = athleteUseCase.create(request.name(), request.countryCode());

        return BaseResponse.success(athleteMapper.toResponse(created));
    }

}
