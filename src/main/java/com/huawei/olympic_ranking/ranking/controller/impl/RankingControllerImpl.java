package com.huawei.olympic_ranking.ranking.controller.impl;

import com.huawei.olympic_ranking.common.model.response.BaseResponse;
import com.huawei.olympic_ranking.ranking.controller.RankingController;
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
public class RankingControllerImpl implements RankingController {

    private final RankingUseCase rankingUseCase;
    private final RankingApplicationMapper rankingMapper;

    @Override
    @GetMapping("/countries")
    public BaseResponse<List<CountryRankingItem>> countries() {
        return BaseResponse.success(rankingMapper.toCountryItems(rankingUseCase.rankCountries()));
    }

    @Override
    @GetMapping("/athletes")
    public BaseResponse<List<AthleteRankingItem>> athletes() {
        return BaseResponse.success(rankingMapper.toAthleteItems(rankingUseCase.rankAthletes()));
    }

    @Override
    @GetMapping("/sports")
    public BaseResponse<List<SportRankingItem>> sports() {
        return BaseResponse.success(rankingMapper.toSportItems(rankingUseCase.rankSports()));
    }

}
