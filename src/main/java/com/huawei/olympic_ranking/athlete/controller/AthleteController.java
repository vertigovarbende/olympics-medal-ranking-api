package com.huawei.olympic_ranking.athlete.controller;

import com.huawei.olympic_ranking.athlete.model.request.AthleteCreateRequest;
import com.huawei.olympic_ranking.athlete.model.response.AthleteResponse;
import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.common.model.response.ErrorResponse;
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

@Tag(name = "Athletes", description = "Sporcu kayıt işlemleri")
public interface AthleteController {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Sporcu oluşturur", description = "Sporcu, kayıtlı bir ülkeye ülke koduyla bağlanır (büyük/küçük harf duyarsız). Aynı adlı birden fazla sporcu kaydedilebilir.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sporcu oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Eksik alan, geçersiz ülke kodu, 100 karakteri aşan ad veya bozuk istek gövdesi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ülke koduna ait ülke bulunamadı", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    BaseResponse<AthleteResponse> create(@Valid @RequestBody AthleteCreateRequest request);

}
