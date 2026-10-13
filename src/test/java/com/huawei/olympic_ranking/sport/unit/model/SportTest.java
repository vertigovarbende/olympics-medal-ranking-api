package com.huawei.olympic_ranking.sport.unit.model;

import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.sport.exception.SportErrorCode;
import com.huawei.olympic_ranking.sport.model.Sport;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Sport - Birim Testleri")
class SportTest {

    @Test
    @DisplayName("of() gecerli girdiyle id'siz spor olusturur")
    void shouldCreateSport_WhenValidInput() {

        final Sport sport = Sport.of("Archery");

        assertThat(sport.getId()).isNull();
        assertThat(sport.getName()).isEqualTo("Archery");

    }

    @Test
    @DisplayName("of() adin basindaki ve sonundaki bosluklari atar")
    void shouldStripName_WhenNameHasSurroundingSpaces() {

        final Sport sport = Sport.of("  Swimming ");

        assertThat(sport.getName()).isEqualTo("Swimming");

    }

    @Test
    @DisplayName("restore() kalici depodan gelen id'yi korur")
    void shouldKeepId_WhenRestored() {

        final Sport sport = Sport.restore(7L, "Judo");

        assertThat(sport.getId()).isEqualTo(7L);
        assertThat(sport.getName()).isEqualTo("Judo");

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("of() ad bossa SPORT_NAME_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenNameIsBlank(final String name) {

        assertValidationError(() -> Sport.of(name), SportErrorCode.SPORT_NAME_REQUIRED);

    }

    @Test
    @DisplayName("of() ad 100 karakterden uzunsa SPORT_NAME_TOO_LONG ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenNameIsTooLong() {

        final String tooLongName = "A".repeat(Sport.NAME_MAX_LENGTH + 1);

        assertValidationError(() -> Sport.of(tooLongName), SportErrorCode.SPORT_NAME_TOO_LONG);

    }

    @Test
    @DisplayName("of() ad tam 100 karakterse kabul eder")
    void shouldCreateSport_WhenNameHasMaxLength() {

        final String maxLengthName = "A".repeat(Sport.NAME_MAX_LENGTH);

        final Sport sport = Sport.of(maxLengthName);

        assertThat(sport.getName()).hasSize(Sport.NAME_MAX_LENGTH);

    }

    private static void assertValidationError(final ThrowingCallable callable, final SportErrorCode expected) {
        assertThatThrownBy(callable)
                .isInstanceOfSatisfying(DomainValidationException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(expected));
    }

}
