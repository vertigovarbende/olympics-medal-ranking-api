package com.huawei.olympic_ranking.ranking.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sporcu sıralaması satırı")
public record AthleteRankingItem(

        @Schema(description = "Sıra (1'den başlar, listede benzersizdir)", example = "1")
        int rank,

        @Schema(description = "Sporcu kimliği", example = "1")
        Long athleteId,

        @Schema(description = "Sporcu adı", example = "Mete Gazoz")
        String athleteName,

        @Schema(description = "Toplam madalya sayısı", example = "2")
        long total,

        @Schema(description = "Altın madalya sayısı", example = "1")
        long gold,

        @Schema(description = "Gümüş madalya sayısı", example = "1")
        long silver,

        @Schema(description = "Bronz madalya sayısı", example = "0")
        long bronze

) {

}
