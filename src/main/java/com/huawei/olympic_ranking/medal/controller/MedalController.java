package com.huawei.olympic_ranking.medal.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.mapper.MedalApplicationMapper;
import com.huawei.olympic_ranking.medal.model.request.MedalCreateRequest;
import com.huawei.olympic_ranking.medal.model.response.MedalResponse;
import com.huawei.olympic_ranking.medal.service.MedalUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medals")
@RequiredArgsConstructor
public class MedalController {

    private final MedalUseCase medalUseCase;
    private final MedalApplicationMapper medalMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<MedalResponse> create(@Valid @RequestBody final MedalCreateRequest request) {

        final Medal created = medalUseCase.create(request.athleteId(), request.sportId(), request.medalType());

        return BaseResponse.success(medalMapper.toResponse(created));
    }

}
