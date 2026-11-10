package com.huawei.olympic_ranking.ranking.unit.service.impl;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.ranking.model.Ranking;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.port.MedalTallyRepository;
import com.huawei.olympic_ranking.ranking.service.impl.RankingService;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.testsupport.SampleData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aCountry;
import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aSport;
import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.anAthlete;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RankingService - Birim Testleri")
class RankingServiceTest {

    @Mock
    private MedalTallyRepository medalTallyRepository;

    @InjectMocks
    private RankingService rankingService;

    @Test
    @DisplayName("rankCountries() ulkeleri resmi kurala gore siralar ve rank verir")
    void shouldRankCountriesByOfficialRules_WhenTalliesExist() {

        final List<Standing<Country>> tallies = List.of(
                aCountry(SampleData.JPN_NAME).withMedals(1, 0, 0).build(),
                aCountry(SampleData.USA_NAME).withMedals(1, 0, 1).build(),
                aCountry(SampleData.TUR_NAME).withMedals(1, 1, 0).build());
        when(this.medalTallyRepository.findCountryTallies()).thenReturn(tallies);

        final Ranking<Country> ranking = this.rankingService.rankCountries();

        assertThat(describe(ranking)).containsExactly("1:Türkiye", "2:United States", "3:Japan");

    }

    @Test
    @DisplayName("rankCountries() madalyasiz ulkeyi listeye almaz")
    void shouldExcludeCountriesWithoutMedals() {

        final List<Standing<Country>> tallies = List.of(
                aCountry(SampleData.TUR_NAME).withMedals(0, 0, 1).build(),
                aCountry(SampleData.JPN_NAME).build());
        when(this.medalTallyRepository.findCountryTallies()).thenReturn(tallies);

        final Ranking<Country> ranking = this.rankingService.rankCountries();

        assertThat(describe(ranking)).containsExactly("1:Türkiye");

    }

    @Test
    @DisplayName("rankCountries() sayim yoksa bos siralama dondurur")
    void shouldReturnEmptyRanking_WhenNoTallies() {

        when(this.medalTallyRepository.findCountryTallies()).thenReturn(List.of());

        final Ranking<Country> ranking = this.rankingService.rankCountries();

        assertThat(ranking.entries()).isEmpty();

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
