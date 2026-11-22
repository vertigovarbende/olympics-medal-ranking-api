package com.huawei.olympic_ranking.ranking.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.common.model.response.ErrorResponse;
import com.huawei.olympic_ranking.ranking.model.RankingStrategy;
import com.huawei.olympic_ranking.ranking.model.response.AthleteRankingItem;
import com.huawei.olympic_ranking.ranking.model.response.CountryRankingItem;
import com.huawei.olympic_ranking.ranking.model.response.SportRankingItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Rankings", description = "Ülke, sporcu ve spor madalya sıralamaları")
public interface RankingController {

    @Operation(summary = "Ülke sıralamasını döner", description = "OFFICIAL (varsayılan, resmi Olimpiyat kuralı): önce altın, eşitlikte gümüş, sonra bronz, sonra ülke adı (alfabetik). WEIGHTED: önce ağırlıklı puan (altın 3, gümüş 2, bronz 1), eşitlikte altın sayısı, sonra gümüş sayısı, sonra ülke adı (alfabetik). Yalnızca en az bir madalyası olan ülkeler listelenir; rank 1'den başlar ve benzersizdir.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sıralama döndü; hiç madalya yoksa liste boştur"),
            @ApiResponse(responseCode = "400", description = "Geçersiz strateji değeri (OFFICIAL veya WEIGHTED olmalı)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    BaseResponse<List<CountryRankingItem>> countries(
            @Parameter(description = "Sıralama stratejisi: OFFICIAL (varsayılan) veya WEIGHTED", example = "OFFICIAL")
            @RequestParam(name = "strategy", defaultValue = "OFFICIAL") RankingStrategy strategy
    );

    @Operation(summary = "Sporcu sıralamasını döner", description = "Önce toplam madalya sayısı, eşitlikte altın, gümüş, bronz sayısı, sonra sporcu adı (alfabetik). Yalnızca en az bir madalyası olan sporcular listelenir; rank 1'den başlar ve benzersizdir.")
    @ApiResponse(responseCode = "200", description = "Sıralama döndü; hiç madalya yoksa liste boştur")
    BaseResponse<List<AthleteRankingItem>> athletes();

    @Operation(summary = "Spor sıralamasını döner", description = "Önce o sporda verilen toplam madalya sayısı, eşitlikte spor adı (alfabetik). Yalnızca en az bir madalya verilmiş sporlar listelenir; rank 1'den başlar ve benzersizdir.")
    @ApiResponse(responseCode = "200", description = "Sıralama döndü; hiç madalya yoksa liste boştur")
    BaseResponse<List<SportRankingItem>> sports();

}
