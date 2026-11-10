package com.huawei.olympic_ranking.sport.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.common.model.response.ErrorResponse;
import com.huawei.olympic_ranking.sport.model.request.SportCreateRequest;
import com.huawei.olympic_ranking.sport.model.response.SportResponse;
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

@Tag(name = "Sports", description = "Spor dalı kayıt işlemleri")
public interface SportController {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Spor dalı oluşturur", description = "Ad baştaki/sondaki boşluklar atılarak saklanır; tekrar kontrolü büyük/küçük harf duyarsızdır (Judo ve judo aynı spordur).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Spor dalı oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Eksik ad, 100 karakteri aşan ad veya bozuk istek gövdesi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Aynı adlı spor dalı zaten kayıtlı", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    BaseResponse<SportResponse> create(@Valid @RequestBody SportCreateRequest request);

}
