package com.huawei.olympic_ranking.athlete.unit.model;

import com.huawei.olympic_ranking.athlete.exception.AthleteErrorCode;
import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Athlete - Birim Testleri")
class AthleteTest {

    private static final Country TURKEY = new CountryBuilder().build();

    @Test
    @DisplayName("of() gecerli girdiyle id'siz sporcu olusturur")
    void shouldCreateAthlete_WhenValidInput() {

        final Athlete athlete = Athlete.of("Mete Gazoz", TURKEY);

        assertThat(athlete.getId()).isNull();
        assertThat(athlete.getName()).isEqualTo("Mete Gazoz");
        assertThat(athlete.getCountry()).isSameAs(TURKEY);

    }

    @Test
    @DisplayName("of() adin basindaki ve sonundaki bosluklari atar")
    void shouldStripName_WhenNameHasSurroundingSpaces() {

        final Athlete athlete = Athlete.of("  Noah Lyles ", TURKEY);

        assertThat(athlete.getName()).isEqualTo("Noah Lyles");

    }

    @Test
    @DisplayName("restore() kalici depodan gelen id'yi korur")
    void shouldKeepId_WhenRestored() {

        final Athlete athlete = Athlete.restore(3L, "Hifumi Abe", TURKEY);

        assertThat(athlete.getId()).isEqualTo(3L);

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("of() ad bossa ATHLETE_NAME_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenNameIsBlank(final String name) {

        assertValidationError(() -> Athlete.of(name, TURKEY), AthleteErrorCode.ATHLETE_NAME_REQUIRED);

    }

    @Test
    @DisplayName("of() ulke yoksa ATHLETE_COUNTRY_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenCountryIsNull() {

        assertValidationError(() -> Athlete.of("Mete Gazoz", null), AthleteErrorCode.ATHLETE_COUNTRY_REQUIRED);

    }

    @Test
    @DisplayName("of() ad 100 karakterden uzunsa ATHLETE_NAME_TOO_LONG ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenNameIsTooLong() {

        final String tooLongName = "A".repeat(Athlete.NAME_MAX_LENGTH + 1);

        assertValidationError(() -> Athlete.of(tooLongName, TURKEY), AthleteErrorCode.ATHLETE_NAME_TOO_LONG);

    }

    @Test
    @DisplayName("of() ad tam 100 karakterse kabul eder")
    void shouldCreateAthlete_WhenNameHasMaxLength() {

        final String maxLengthName = "A".repeat(Athlete.NAME_MAX_LENGTH);

        final Athlete athlete = Athlete.of(maxLengthName, TURKEY);

        assertThat(athlete.getName()).hasSize(Athlete.NAME_MAX_LENGTH);

    }

    private static void assertValidationError(final ThrowingCallable callable, final AthleteErrorCode expected) {
        assertThatThrownBy(callable)
                .isInstanceOfSatisfying(DomainValidationException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(expected));
    }

}
