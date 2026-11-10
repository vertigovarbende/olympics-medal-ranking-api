package com.huawei.olympic_ranking.sport.controller.impl;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.sport.controller.SportController;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.model.mapper.SportApplicationMapper;
import com.huawei.olympic_ranking.sport.model.request.SportCreateRequest;
import com.huawei.olympic_ranking.sport.model.response.SportResponse;
import com.huawei.olympic_ranking.sport.service.SportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sports")
@RequiredArgsConstructor
public class SportControllerImpl implements SportController {

    private final SportUseCase sportUseCase;
    private final SportApplicationMapper sportMapper;

    @Override
    @PostMapping
    public BaseResponse<SportResponse> create(final SportCreateRequest request) {

        final Sport created = sportUseCase.create(request.name());

        return BaseResponse.success(sportMapper.toResponse(created));
    }

}
