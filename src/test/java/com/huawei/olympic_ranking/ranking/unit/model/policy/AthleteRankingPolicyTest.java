package com.huawei.olympic_ranking.ranking.unit.model.policy;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.ranking.model.Standing;
import com.huawei.olympic_ranking.ranking.model.policy.AthleteRankingPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.huawei.olympic_ranking.ranking.testsupport.StandingBuilder.anAthlete;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AthleteRankingPolicy - Birim Testleri")
class AthleteRankingPolicyTest {

    private final AthleteRankingPolicy policy = new AthleteRankingPolicy();

    @Test
    @DisplayName("order() once toplam madalyaya bakar; daha cok altini olan ama toplami az sporcu geride kalir")
    void shouldRankMoreTotalFirst_EvenWhenFewerGold() {

        final Standing<Athlete> moreTotal = anAthlete("More Total").withMedals(1, 2, 2).build();
        final Standing<Athlete> moreGold = anAthlete("More Gold").withMedals(3, 0, 0).build();

        final List<String> sorted = sortedNames(List.of(moreGold, moreTotal));

        assertThat(sorted).containsExactly("More Total", "More Gold");

    }

    @Test
    @DisplayName("order() toplam esitse altin sayisi karar verir")
    void shouldRankMoreGoldFirst_WhenTotalIsEqual() {

        final Standing<Athlete> moreGold = anAthlete("More Gold").withMedals(2, 0, 1).build();
        final Standing<Athlete> lessGold = anAthlete("Less Gold").withMedals(1, 2, 0).build();

        final List<String> sorted = sortedNames(List.of(lessGold, moreGold));

        assertThat(sorted).containsExactly("More Gold", "Less Gold");

    }

    @Test
    @DisplayName("order() toplam ve altin esitse gumus sayisi karar verir")
    void shouldRankMoreSilverFirst_WhenTotalAndGoldAreEqual() {

        final Standing<Athlete> moreSilver = anAthlete("More Silver").withMedals(2, 2, 1).build();
        final Standing<Athlete> lessSilver = anAthlete("Less Silver").withMedals(2, 1, 2).build();

        final List<String> sorted = sortedNames(List.of(lessSilver, moreSilver));

        assertThat(sorted).containsExactly("More Silver", "Less Silver");

    }

    @Test
    @DisplayName("order() tum sayilar esitse sporcu adini buyuk/kucuk harf duyarsiz alfabetik siralar")
    void shouldRankByNameIgnoringCase_WhenAllMedalsAreEqual() {

        final Standing<Athlete> lowerCase = anAthlete("zeynep").withMedals(1, 1, 1).build();
        final Standing<Athlete> upperCase = anAthlete("Ali").withMedals(1, 1, 1).build();

        final List<String> sorted = sortedNames(List.of(lowerCase, upperCase));

        assertThat(sorted).containsExactly("Ali", "zeynep");

    }

    @Test
    @DisplayName("order() kabul senaryosunda A, B, C sirasini verir")
    void shouldFollowAcceptanceScenarioOrder_WhenAthletesAreABC() {

        final Standing<Athlete> a = anAthlete("A").withMedals(2, 2, 1).build();
        final Standing<Athlete> b = anAthlete("B").withMedals(2, 1, 2).build();
        final Standing<Athlete> c = anAthlete("C").withMedals(3, 1, 0).build();

        final List<String> sorted = sortedNames(List.of(c, b, a));

        assertThat(sorted).containsExactly("A", "B", "C");

    }

    private List<String> sortedNames(final List<Standing<Athlete>> standings) {
        final List<Standing<Athlete>> list = new ArrayList<>(standings);
        list.sort(this.policy.order());
        return list.stream().map(Standing::name).toList();
    }

}
