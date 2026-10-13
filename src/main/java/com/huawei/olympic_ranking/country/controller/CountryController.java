package com.huawei.olympic_ranking.country.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.common.model.response.ErrorResponse;
import com.huawei.olympic_ranking.country.model.request.CountryCreateRequest;
import com.huawei.olympic_ranking.country.model.response.CountryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Countries", description = "Ülke kayıt işlemleri")
public interface CountryController {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ülke oluşturur", description = "Ülke kodu 3 harfli olmalıdır; büyük/küçük harf duyarsızdır ve büyük harfe normalize edilir (ör. tur → TUR).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ülke oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Eksik alan, geçersiz ülke kodu, 100 karakteri aşan ad veya bozuk istek gövdesi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ülke kodu zaten kayıtlı", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    BaseResponse<CountryResponse> create(@Valid @RequestBody CountryCreateRequest request);

}
