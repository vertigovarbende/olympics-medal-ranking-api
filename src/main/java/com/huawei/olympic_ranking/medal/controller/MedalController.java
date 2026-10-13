package com.huawei.olympic_ranking.medal.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.common.model.response.ErrorResponse;
import com.huawei.olympic_ranking.medal.model.request.MedalCreateRequest;
import com.huawei.olympic_ranking.medal.model.response.MedalResponse;
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

@Tag(name = "Medals", description = "Madalya kayıt işlemleri")
public interface MedalController {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Madalya kaydeder", description = "Madalya, sporcu ve spor kimliğiyle kaydedilir; madalyanın ülkesi sporcunun ülkesinden türetilir. Aynı sporcu + spor + madalya türü ikinci kez kaydedilemez.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Madalya kaydedildi"),
            @ApiResponse(responseCode = "400", description = "Eksik veya pozitif olmayan kimlik, geçersiz madalya türü ya da bozuk istek gövdesi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sporcu veya spor bulunamadı", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Aynı sporcu, spor ve madalya türü için madalya zaten kayıtlı", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    BaseResponse<MedalResponse> create(@Valid @RequestBody MedalCreateRequest request);

}
