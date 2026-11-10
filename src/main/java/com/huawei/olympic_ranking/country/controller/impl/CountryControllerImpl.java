package com.huawei.olympic_ranking.country.controller.impl;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.country.controller.CountryController;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.model.mapper.CountryApplicationMapper;
import com.huawei.olympic_ranking.country.model.request.CountryCreateRequest;
import com.huawei.olympic_ranking.country.model.response.CountryResponse;
import com.huawei.olympic_ranking.country.service.CountryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryControllerImpl implements CountryController {

    private final CountryUseCase countryUseCase;
    private final CountryApplicationMapper countryMapper;

    @Override
    @PostMapping
    public BaseResponse<CountryResponse> create(final CountryCreateRequest request) {

        final Country created = countryUseCase.create(request.name(), request.code());

        return BaseResponse.success(countryMapper.toResponse(created));
    }

}
