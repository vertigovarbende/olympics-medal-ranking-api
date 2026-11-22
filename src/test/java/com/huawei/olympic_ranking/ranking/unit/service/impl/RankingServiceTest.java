package com.huawei.olympic_ranking.ranking.unit.service.impl;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.Ranking;
import com.huawei.olympic_ranking.ranking.model.RankingStrategy;
import com.huawei.olympic_ranking.ranking.model.policy.CountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.policy.WeightedCountryRankingPolicy;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.port.MedalTallyRepository;
import com.huawei.olympic_ranking.ranking.service.strategy.CountryRankingPolicyRegistry;
import com.huawei.olympic_ranking.ranking.service.impl.RankingService;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.testsupport.SampleData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aCountry;
import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aSport;
import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.anAthlete;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RankingService - Birim Testleri")
class RankingServiceTest {

    @Mock
    private MedalTallyRepository medalTallyRepository;

    private RankingService rankingService;

    @BeforeEach
    void setUp() {

        final CountryRankingPolicyRegistry registry = new CountryRankingPolicyRegistry(
                List.of(new CountryRankingPolicy(), new WeightedCountryRankingPolicy()));

        this.rankingService = new RankingService(this.medalTallyRepository, registry);

    }

    @Test
    @DisplayName("rankCountries() ulkeleri resmi kurala gore siralar ve rank verir")
    void shouldRankCountriesByOfficialRules_WhenTalliesExist() {

        final List<Standing<Country>> tallies = List.of(
                aCountry(SampleData.JPN_NAME).withMedals(1, 0, 0).build(),
                aCountry(SampleData.USA_NAME).withMedals(1, 0, 1).build(),
                aCountry(SampleData.TUR_NAME).withMedals(1, 1, 0).build());
        when(this.medalTallyRepository.findCountryTallies()).thenReturn(tallies);

        final Ranking<Country> ranking = this.rankingService.rankCountries(RankingStrategy.OFFICIAL);

        assertThat(describe(ranking)).containsExactly("1:Türkiye", "2:United States", "3:Japan");

    }

    @Test
    @DisplayName("rankCountries() madalyasiz ulkeyi listeye almaz")
    void shouldExcludeCountriesWithoutMedals() {

        final List<Standing<Country>> tallies = List.of(
                aCountry(SampleData.TUR_NAME).withMedals(0, 0, 1).build(),
                aCountry(SampleData.JPN_NAME).build());
        when(this.medalTallyRepository.findCountryTallies()).thenReturn(tallies);

        final Ranking<Country> ranking = this.rankingService.rankCountries(RankingStrategy.OFFICIAL);

        assertThat(describe(ranking)).containsExactly("1:Türkiye");

    }

    @Test
    @DisplayName("rankCountries() sayim yoksa bos siralama dondurur")
    void shouldReturnEmptyRanking_WhenNoTallies() {

        when(this.medalTallyRepository.findCountryTallies()).thenReturn(List.of());

        final Ranking<Country> ranking = this.rankingService.rankCountries(RankingStrategy.OFFICIAL);

        assertThat(ranking.entries()).isEmpty();

    }

    @Test
    @DisplayName("rankCountries() WEIGHTED stratejide ulkeleri agirlikli puana gore siralar")
    void shouldRankCountriesByWeightedPoints_WhenStrategyIsWeighted() {

        final List<Standing<Country>> tallies = List.of(
                aCountry(SampleData.USA_NAME).withMedals(2, 1, 0).build(),
                aCountry(SampleData.TUR_NAME).withMedals(1, 3, 0).build());
        when(this.medalTallyRepository.findCountryTallies()).thenReturn(tallies);

        final Ranking<Country> ranking = this.rankingService.rankCountries(RankingStrategy.WEIGHTED);

        assertThat(describe(ranking)).containsExactly("1:Türkiye", "2:United States");

    }

    @Test
    @DisplayName("rankCountries() OFFICIAL stratejide ayni veride cok altinli ulkeyi one alir")
    void shouldRankCountriesByGoldFirst_WhenStrategyIsOfficial() {

        final List<Standing<Country>> tallies = List.of(
                aCountry(SampleData.TUR_NAME).withMedals(1, 3, 0).build(),
                aCountry(SampleData.USA_NAME).withMedals(2, 1, 0).build());
        when(this.medalTallyRepository.findCountryTallies()).thenReturn(tallies);

        final Ranking<Country> ranking = this.rankingService.rankCountries(RankingStrategy.OFFICIAL);

        assertThat(describe(ranking)).containsExactly("1:United States", "2:Türkiye");

    }

    @Test
    @DisplayName("rankCountries() WEIGHTED stratejide de madalyasiz ulkeyi listeye almaz")
    void shouldExcludeCountriesWithoutMedals_WhenStrategyIsWeighted() {

        final List<Standing<Country>> tallies = List.of(
                aCountry(SampleData.TUR_NAME).withMedals(0, 0, 1).build(),
                aCountry(SampleData.JPN_NAME).build());
        when(this.medalTallyRepository.findCountryTallies()).thenReturn(tallies);

        final Ranking<Country> ranking = this.rankingService.rankCountries(RankingStrategy.WEIGHTED);

        assertThat(describe(ranking)).containsExactly("1:Türkiye");

    }

    @Test
    @DisplayName("rankCountries() strateji null ise NullPointerException firlatir ve repository'ye dokunmaz")
    void shouldThrowNullPointerException_WhenStrategyIsNull() {

        assertThatThrownBy(() -> this.rankingService.rankCountries(null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(this.medalTallyRepository);

    }

    @Test
    @DisplayName("rankAthletes() sporculari toplam madalyaya gore siralar")
    void shouldRankAthletesByTotalFirst_WhenTalliesExist() {

        final List<Standing<Athlete>> tallies = List.of(
                anAthlete("C").withMedals(3, 1, 0).build(),
                anAthlete("A").withMedals(2, 2, 1).build());
        when(this.medalTallyRepository.findAthleteTallies()).thenReturn(tallies);

        final Ranking<Athlete> ranking = this.rankingService.rankAthletes();

        assertThat(describe(ranking)).containsExactly("1:A", "2:C");

    }

    @Test
    @DisplayName("rankSports() sporlari toplam madalyaya gore siralar")
    void shouldRankSportsByTotalFirst_WhenTalliesExist() {

        final List<Standing<Sport>> tallies = List.of(
                aSport(SampleData.JUDO).withMedals(1, 0, 0).build(),
                aSport(SampleData.ARCHERY).withMedals(1, 1, 0).build());
        when(this.medalTallyRepository.findSportTallies()).thenReturn(tallies);

        final Ranking<Sport> ranking = this.rankingService.rankSports();

        assertThat(describe(ranking)).containsExactly("1:Archery", "2:Judo");

    }

    private static List<String> describe(final Ranking<?> ranking) {
        return ranking.entries().stream()
                .map(entry -> entry.rank() + ":" + entry.standing().name())
                .toList();
    }

}
