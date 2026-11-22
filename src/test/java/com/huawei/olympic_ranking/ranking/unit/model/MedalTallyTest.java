package com.huawei.olympic_ranking.ranking.unit.model;

import com.huawei.olympic_ranking.ranking.model.MedalTally;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MedalTally - Birim Testleri")
class MedalTallyTest {

    @Test
    @DisplayName("total() altin, gumus ve bronz sayilarinin toplamini verir")
    void shouldCalculateTotal_WhenMedalsExist() {

        final MedalTally tally = new MedalTally(2, 3, 4);

        assertThat(tally.total()).isEqualTo(9);

    }

    @Test
    @DisplayName("weightedPoints() altin 3, gumus 2, bronz 1 puanla hesaplar")
    void shouldCalculateWeightedPoints_WhenMedalsExist() {

        final MedalTally tally = new MedalTally(2, 3, 4);

        assertThat(tally.weightedPoints()).isEqualTo(16);

    }

    @Test
    @DisplayName("weightedPoints() madalya yoksa sifir verir")
    void shouldReturnZeroPoints_WhenNoMedals() {

        final MedalTally tally = new MedalTally(0, 0, 0);

        assertThat(tally.weightedPoints()).isZero();

    }

    @ParameterizedTest
    @CsvSource({"1, 0, 0", "0, 1, 0", "0, 0, 1"})
    @DisplayName("hasMedals() en az bir madalya varsa true doner")
    void shouldHaveMedals_WhenAtLeastOneMedalExists(final long gold, final long silver, final long bronze) {

        final MedalTally tally = new MedalTally(gold, silver, bronze);

        assertThat(tally.hasMedals()).isTrue();

    }

    @Test
    @DisplayName("hasMedals() hic madalya yoksa false doner")
    void shouldNotHaveMedals_WhenAllCountsAreZero() {

        final MedalTally tally = new MedalTally(0, 0, 0);

        assertThat(tally.hasMedals()).isFalse();

    }

    @ParameterizedTest
    @CsvSource({"-1, 0, 0", "0, -1, 0", "0, 0, -1"})
    @DisplayName("constructor negatif sayida IllegalArgumentException firlatir")
    void shouldThrowIllegalArgumentException_WhenAnyCountIsNegative(final long gold, final long silver, final long bronze) {

        assertThatThrownBy(() -> new MedalTally(gold, silver, bronze))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
