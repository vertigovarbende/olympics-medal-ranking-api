package com.huawei.olympic_ranking.ranking.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Spor dalı sıralaması satırı")
public record SportRankingItem(

        @Schema(description = "Sıra (1'den başlar, listede benzersizdir)", example = "1")
        int rank,

        @Schema(description = "Spor dalı kimliği", example = "1")
        Long sportId,

        @Schema(description = "Spor dalı adı", example = "Archery")
        String sportName,

        @Schema(description = "Bu sporda verilen toplam madalya sayısı", example = "2")
        long total

) {

}
