package com.huawei.olympic_ranking.sport.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.model.mapper.SportApplicationMapper;
import com.huawei.olympic_ranking.sport.model.request.SportCreateRequest;
import com.huawei.olympic_ranking.sport.model.response.SportResponse;
import com.huawei.olympic_ranking.sport.service.SportUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportController {

    private final SportUseCase sportUseCase;
    private final SportApplicationMapper sportMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<SportResponse> create(@Valid @RequestBody final SportCreateRequest request) {

        final Sport created = sportUseCase.create(request.name());

        return BaseResponse.success(sportMapper.toResponse(created));
    }

}
