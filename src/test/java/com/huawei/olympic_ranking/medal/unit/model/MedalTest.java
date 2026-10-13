package com.huawei.olympic_ranking.medal.unit.model;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.testsupport.AthleteBuilder;
import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.medal.exception.MedalErrorCode;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.testsupport.SportBuilder;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Medal - Birim Testleri")
class MedalTest {

    private static final Athlete ATHLETE = new AthleteBuilder().build();
    private static final Sport SPORT = new SportBuilder().build();

    @Test
    @DisplayName("of() gecerli girdiyle id'siz madalya olusturur")
    void shouldCreateMedal_WhenValidInput() {

        final Medal medal = Medal.of(ATHLETE, SPORT, MedalType.GOLD);

        assertThat(medal.getId()).isNull();
        assertThat(medal.getAthlete()).isSameAs(ATHLETE);
        assertThat(medal.getSport()).isSameAs(SPORT);
        assertThat(medal.getType()).isEqualTo(MedalType.GOLD);

    }

    @Test
    @DisplayName("country() madalyanin ulkesini sporcunun ulkesinden turetir")
    void shouldDeriveCountryFromAthlete_WhenCountryRequested() {

        final Country japan = new CountryBuilder().withId(3L).withCode("JPN").withName("Japan").build();
        final Athlete athlete = new AthleteBuilder().withName("Hifumi Abe").withCountry(japan).build();

        final Medal medal = Medal.of(athlete, SPORT, MedalType.GOLD);

        assertThat(medal.country()).isSameAs(japan);

    }

    @Test
    @DisplayName("restore() kalici depodan gelen id'yi korur")
    void shouldKeepId_WhenRestored() {

        final Medal medal = Medal.restore(9L, ATHLETE, SPORT, MedalType.BRONZE);

        assertThat(medal.getId()).isEqualTo(9L);

    }

    @Test
    @DisplayName("of() sporcu yoksa MEDAL_ATHLETE_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenAthleteIsNull() {

        assertValidationError(() -> Medal.of(null, SPORT, MedalType.GOLD), MedalErrorCode.MEDAL_ATHLETE_REQUIRED);

    }

    @Test
    @DisplayName("of() spor yoksa MEDAL_SPORT_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenSportIsNull() {

        assertValidationError(() -> Medal.of(ATHLETE, null, MedalType.GOLD), MedalErrorCode.MEDAL_SPORT_REQUIRED);

    }

    @Test
    @DisplayName("of() madalya turu yoksa MEDAL_TYPE_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenTypeIsNull() {

        assertValidationError(() -> Medal.of(ATHLETE, SPORT, null), MedalErrorCode.MEDAL_TYPE_REQUIRED);

    }

    private static void assertValidationError(final ThrowingCallable callable, final MedalErrorCode expected) {
        assertThatThrownBy(callable)
                .isInstanceOfSatisfying(DomainValidationException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(expected));
    }

}
