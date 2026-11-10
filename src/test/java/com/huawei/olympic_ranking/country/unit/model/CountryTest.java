package com.huawei.olympic_ranking.country.unit.model;

import com.huawei.olympic_ranking.common.exception.DomainValidationException;
import com.huawei.olympic_ranking.country.exception.CountryErrorCode;
import com.huawei.olympic_ranking.country.model.Country;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Country - Birim Testleri")
class CountryTest {

    @Test
    @DisplayName("of() gecerli girdiyle id'siz ulke olusturur")
    void shouldCreateCountry_WhenValidInput() {

        final Country country = Country.of("TUR", "Türkiye");

        assertThat(country.getId()).isNull();
        assertThat(country.getCode()).isEqualTo("TUR");
        assertThat(country.getName()).isEqualTo("Türkiye");

    }

    @Test
    @DisplayName("of() kodu bosluklardan arindirip buyuk harfe cevirir")
    void shouldNormalizeCode_WhenCodeIsLowerCaseWithSpaces() {

        final Country country = Country.of("  tur ", "Türkiye");

        assertThat(country.getCode()).isEqualTo("TUR");

    }

    @Test
    @DisplayName("of() adin basindaki ve sonundaki bosluklari atar")
    void shouldStripName_WhenNameHasSurroundingSpaces() {

        final Country country = Country.of("USA", "  United States ");

        assertThat(country.getName()).isEqualTo("United States");

    }

    @Test
    @DisplayName("restore() kalici depodan gelen id'yi korur")
    void shouldKeepId_WhenRestored() {

        final Country country = Country.restore(5L, "JPN", "Japan");

        assertThat(country.getId()).isEqualTo(5L);
        assertThat(country.getCode()).isEqualTo("JPN");

    }

    @ParameterizedTest
    @ValueSource(strings = {"TU", "TURK", "T1R", "T-R", "T R"})
    @DisplayName("of() kod tam 3 harf degilse INVALID_COUNTRY_CODE ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenCodeIsNotThreeLetters(final String code) {

        assertValidationError(() -> Country.of(code, "Türkiye"), CountryErrorCode.INVALID_COUNTRY_CODE);

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("of() kod bossa COUNTRY_CODE_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenCodeIsBlank(final String code) {

        assertValidationError(() -> Country.of(code, "Türkiye"), CountryErrorCode.COUNTRY_CODE_REQUIRED);

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("of() ad bossa COUNTRY_NAME_REQUIRED ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenNameIsBlank(final String name) {

        assertValidationError(() -> Country.of("TUR", name), CountryErrorCode.COUNTRY_NAME_REQUIRED);

    }

    @Test
    @DisplayName("of() ad 100 karakterden uzunsa COUNTRY_NAME_TOO_LONG ile DomainValidationException firlatir")
    void shouldThrowValidationException_WhenNameIsTooLong() {

        final String tooLongName = "A".repeat(Country.NAME_MAX_LENGTH + 1);

        assertValidationError(() -> Country.of("TUR", tooLongName), CountryErrorCode.COUNTRY_NAME_TOO_LONG);

    }

    @Test
    @DisplayName("of() ad tam 100 karakterse kabul eder")
    void shouldCreateCountry_WhenNameHasMaxLength() {

        final String maxLengthName = "A".repeat(Country.NAME_MAX_LENGTH);

        final Country country = Country.of("TUR", maxLengthName);

        assertThat(country.getName()).hasSize(Country.NAME_MAX_LENGTH);

    }

    private static void assertValidationError(final ThrowingCallable callable, final CountryErrorCode expected) {
        assertThatThrownBy(callable)
                .isInstanceOfSatisfying(DomainValidationException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(expected));
    }

}
