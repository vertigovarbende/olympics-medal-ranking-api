package com.huawei.olympic_ranking.ranking.unit.model;

import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.Ranking;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.model.policy.CountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.RankingPolicy;
import com.huawei.olympic_ranking.testsupport.SampleData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aCountry;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Ranking - Birim Testleri")
class RankingTest {

    private final RankingPolicy policy = new CountryRankingPolicy();

    @Test
    @DisplayName("of() siralanmamis girdiyi politikaya gore siralar ve 1'den baslayarak rank verir")
    void shouldAssignRanksFromOne_WhenStandingsAreUnsorted() {

        final Standing<Country> japan = aCountry(SampleData.JPN_NAME).withMedals(1, 0, 0).build();
        final Standing<Country> usa = aCountry(SampleData.USA_NAME).withMedals(1, 0, 1).build();
        final Standing<Country> turkey = aCountry(SampleData.TUR_NAME).withMedals(1, 1, 0).build();

        final Ranking<Country> ranking = Ranking.of(List.of(japan, usa, turkey), this.policy);

        assertThat(describe(ranking)).containsExactly("1:Türkiye", "2:United States", "3:Japan");

    }

    @Test
    @DisplayName("of() madalyasi olmayan kayitlari listeden eler")
    void shouldExcludeStandingsWithoutMedals() {

        final Standing<Country> withMedals = aCountry(SampleData.TUR_NAME).withMedals(0, 0, 1).build();
        final Standing<Country> withoutMedals = aCountry(SampleData.JPN_NAME).build();

        final Ranking<Country> ranking = Ranking.of(List.of(withoutMedals, withMedals), this.policy);

        assertThat(describe(ranking)).containsExactly("1:Türkiye");

    }

    @Test
    @DisplayName("of() tamamen esit kayitlara bile benzersiz rank verir")
    void shouldAssignUniqueRanks_WhenStandingsAreFullyTied() {

        final Standing<Country> alpha = aCountry("Alpha").withMedals(1, 0, 0).build();
        final Standing<Country> bravo = aCountry("Bravo").withMedals(1, 0, 0).build();
        final Standing<Country> charlie = aCountry("Charlie").withMedals(1, 0, 0).build();

        final Ranking<Country> ranking = Ranking.of(List.of(charlie, alpha, bravo), this.policy);

        assertThat(describe(ranking)).containsExactly("1:Alpha", "2:Bravo", "3:Charlie");

    }

    @Test
    @DisplayName("of() girdi bossa bos siralama dondurur")
    void shouldReturnEmptyRanking_WhenNoStandings() {

        final Ranking<Country> ranking = Ranking.of(List.of(), this.policy);

        assertThat(ranking.entries()).isEmpty();

    }

    private static List<String> describe(final Ranking<?> ranking) {
        return ranking.entries().stream()
                .map(entry -> entry.rank() + ":" + entry.standing().name())
                .toList();
    }

}
