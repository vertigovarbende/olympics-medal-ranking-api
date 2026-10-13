package com.huawei.olympic_ranking.ranking.controller;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.ranking.model.mapper.RankingApplicationMapper;
import com.huawei.olympic_ranking.ranking.model.response.AthleteRankingItem;
import com.huawei.olympic_ranking.ranking.model.response.CountryRankingItem;
import com.huawei.olympic_ranking.ranking.model.response.SportRankingItem;
import com.huawei.olympic_ranking.ranking.service.RankingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingUseCase rankingUseCase;
    private final RankingApplicationMapper rankingMapper;

    @GetMapping("/countries")
    public BaseResponse<List<CountryRankingItem>> countries() {
        return BaseResponse.success(rankingMapper.toCountryItems(rankingUseCase.rankCountries()));
    }

    @GetMapping("/athletes")
    public BaseResponse<List<AthleteRankingItem>> athletes() {
        return BaseResponse.success(rankingMapper.toAthleteItems(rankingUseCase.rankAthletes()));
    }

    @GetMapping("/sports")
    public BaseResponse<List<SportRankingItem>> sports() {
        return BaseResponse.success(rankingMapper.toSportItems(rankingUseCase.rankSports()));
    }

}
