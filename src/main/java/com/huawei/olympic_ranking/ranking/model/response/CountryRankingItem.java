package com.huawei.olympic_ranking.ranking.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ülke sıralaması satırı")
public record CountryRankingItem(

        @Schema(description = "Sıra (1'den başlar, listede benzersizdir)", example = "1")
        int rank,

        @Schema(description = "Ülke kodu", example = "TUR")
        String countryCode,

        @Schema(description = "Ülke adı", example = "Türkiye")
        String countryName,

        @Schema(description = "Altın madalya sayısı", example = "1")
        long gold,

        @Schema(description = "Gümüş madalya sayısı", example = "1")
        long silver,

        @Schema(description = "Bronz madalya sayısı", example = "0")
        long bronze,

        @Schema(description = "Toplam madalya sayısı", example = "2")
        long total

) {

}
