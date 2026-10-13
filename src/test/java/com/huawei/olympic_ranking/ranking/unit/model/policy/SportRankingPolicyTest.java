package com.huawei.olympic_ranking.ranking.unit.model.policy;

import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.model.policy.SportRankingPolicy;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.testsupport.SampleData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.aSport;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SportRankingPolicy - Birim Testleri")
class SportRankingPolicyTest {

    private final SportRankingPolicy policy = new SportRankingPolicy();

    @Test
    @DisplayName("order() toplam madalyasi cok olan sporu once siralar")
    void shouldRankMoreTotalFirst_WhenTotalsDiffer() {

        final Standing<Sport> busy = aSport("Busy").withMedals(1, 1, 1).build();
        final Standing<Sport> quiet = aSport("Quiet").withMedals(0, 0, 1).build();

        final List<String> sorted = sortedNames(List.of(quiet, busy));

        assertThat(sorted).containsExactly("Busy", "Quiet");

    }

    @Test
    @DisplayName("order() toplam esitse spor adini buyuk/kucuk harf duyarsiz alfabetik siralar")
    void shouldRankByNameIgnoringCase_WhenTotalsAreEqual() {

        final Standing<Sport> lowerCase = aSport("judo").withMedals(1, 0, 0).build();
        final Standing<Sport> upperCase = aSport("Archery").withMedals(0, 1, 0).build();

        final List<String> sorted = sortedNames(List.of(lowerCase, upperCase));

        assertThat(sorted).containsExactly("Archery", "judo");

    }

    @Test
    @DisplayName("order() madalya turlerinin dagilimini dikkate almaz, yalnizca toplama bakar")
    void shouldIgnoreMedalTypeDistribution_WhenTotalsAreEqual() {

        final Standing<Sport> allGold = aSport("Zeta").withMedals(3, 0, 0).build();
        final Standing<Sport> allBronze = aSport("Alpha").withMedals(0, 0, 3).build();

        final List<String> sorted = sortedNames(List.of(allGold, allBronze));

        assertThat(sorted).containsExactly("Alpha", "Zeta");

    }

    @Test
    @DisplayName("order() isterin ornek verisinde Archery, Swimming, Judo sirasini verir")
    void shouldFollowSampleDataOrder_WhenSportsHaveMedals() {

        final Standing<Sport> archery = aSport(SampleData.ARCHERY).withMedals(1, 1, 0).build();
        final Standing<Sport> swimming = aSport(SampleData.SWIMMING).withMedals(1, 0, 1).build();
        final Standing<Sport> judo = aSport(SampleData.JUDO).withMedals(1, 0, 0).build();

        final List<String> sorted = sortedNames(List.of(judo, swimming, archery));

        assertThat(sorted).containsExactly(SampleData.ARCHERY, SampleData.SWIMMING, SampleData.JUDO);

    }

    private List<String> sortedNames(final List<Standing<Sport>> standings) {
        final List<Standing<Sport>> list = new ArrayList<>(standings);
        list.sort(this.policy.order());
        return list.stream().map(Standing::name).toList();
    }

}
