package com.huawei.olympic_ranking.country.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.model.mapper.CountryApplicationMapper;
import com.huawei.olympic_ranking.country.model.request.CountryCreateRequest;
import com.huawei.olympic_ranking.country.model.response.CountryResponse;
import com.huawei.olympic_ranking.country.service.CountryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryUseCase countryUseCase;
    private final CountryApplicationMapper countryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CountryResponse> create(@Valid @RequestBody final CountryCreateRequest request) {

        final Country created = countryUseCase.create(request.name(), request.code());

        return BaseResponse.success(countryMapper.toResponse(created));
    }

}
