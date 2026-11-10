package com.huawei.olympic_ranking.medal.integration.port.adapter;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.athlete.port.AthleteRepository;
import com.huawei.olympic_ranking.athlete.testsupport.AthleteBuilder;
import com.huawei.olympic_ranking.country.port.CountryRepository;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.medal.model.Medal;
import com.huawei.olympic_ranking.medal.model.MedalType;
import com.huawei.olympic_ranking.medal.port.MedalRepository;
import com.huawei.olympic_ranking.medal.testsupport.MedalBuilder;
import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import com.huawei.olympic_ranking.sport.testsupport.SportBuilder;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MedalRepositoryAdapter - Entegrasyon Testleri")
class MedalRepositoryAdapterIT extends TestContainerConfiguration {

    @Autowired
    private MedalRepository medalRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private CountryRepository countryRepository;

    private Athlete athlete;

    private Sport sport;

    @BeforeEach
    void setUpReferences() {

        this.athlete = this.athleteRepository.save(new AthleteBuilder()
                .withoutId()
                .withCountry(this.countryRepository.save(new CountryBuilder().withoutId().build()))
                .build());
        this.sport = this.sportRepository.save(new SportBuilder().withoutId().build());

    }

    @Test
    @DisplayName("save() madalyayi sporcu ve spor referanslariyla kaydeder")
    void shouldSaveMedal_WhenReferencesExist() {

        // Given: kayitli sporcu ve spor ile id'siz madalya
        final Medal medal = this.medal(MedalType.GOLD);

        // When: save
        final Medal saved = this.medalRepository.save(medal);

        // Then: id atanir; sporcu, spor ve tur korunur
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isEqualTo(MedalType.GOLD);
        assertThat(saved.getAthlete().getId()).isEqualTo(this.athlete.getId());
        assertThat(saved.getAthlete().getName()).isEqualTo(SampleData.METE_GAZOZ);
        assertThat(saved.getSport().getId()).isEqualTo(this.sport.getId());

    }

    @Test
    @DisplayName("save() ayni sporcu, spor ve turde ikinci madalyada veri butunlugu hatasi firlatir")
    void shouldThrowException_WhenMedalIsDuplicated() {

        // Given: GOLD madalya kayitli
        this.medalRepository.save(this.medal(MedalType.GOLD));
        final Medal duplicate = this.medal(MedalType.GOLD);

        // When: ayni uclu ile ikinci kayit
        // Then: benzersizlik kisiti ihlal edilir
        assertThatThrownBy(() -> this.medalRepository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("save() ayni sporcu ve spor icin farkli turde madalyayi kabul eder")
    void shouldSaveMedal_WhenTypeDiffers() {

        // Given: GOLD madalya kayitli
        this.medalRepository.save(this.medal(MedalType.GOLD));

        // When: ayni sporcu ve spor icin SILVER kaydi
        final Medal saved = this.medalRepository.save(this.medal(MedalType.SILVER));

        // Then: ikinci madalya kaydedilir
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isEqualTo(MedalType.SILVER);

    }

    @Test
    @DisplayName("existsByAthleteIdAndSportIdAndType() kayitli uclu icin true doner")
    void shouldReturnTrue_WhenMedalExists() {

        // Given: GOLD madalya kayitli
        this.medalRepository.save(this.medal(MedalType.GOLD));

        // When: ayni uclu ile kontrol
        final boolean exists = this.medalRepository
                .existsByAthleteIdAndSportIdAndType(this.athlete.getId(), this.sport.getId(), MedalType.GOLD);

        // Then: true
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("existsByAthleteIdAndSportIdAndType() farkli tur icin false doner")
    void shouldReturnFalse_WhenTypeDiffers() {

        // Given: GOLD madalya kayitli
        this.medalRepository.save(this.medal(MedalType.GOLD));

        // When: SILVER ile kontrol
        final boolean exists = this.medalRepository
                .existsByAthleteIdAndSportIdAndType(this.athlete.getId(), this.sport.getId(), MedalType.SILVER);

        // Then: false
        assertThat(exists).isFalse();

    }

    @Test
    @DisplayName("existsByAthleteIdAndSportIdAndType() farkli spor icin false doner")
    void shouldReturnFalse_WhenSportDiffers() {

        // Given: Archery'de GOLD madalya kayitli ve ikinci bir spor var
        this.medalRepository.save(this.medal(MedalType.GOLD));
        final Sport otherSport = this.sportRepository.save(new SportBuilder().withoutId().withName(SampleData.JUDO).build());

        // When: diger spor ile kontrol
        final boolean exists = this.medalRepository
                .existsByAthleteIdAndSportIdAndType(this.athlete.getId(), otherSport.getId(), MedalType.GOLD);

        // Then: false
        assertThat(exists).isFalse();

    }

    @Test
    @DisplayName("existsByAthleteIdAndSportIdAndType() farkli sporcu icin false doner")
    void shouldReturnFalse_WhenAthleteDiffers() {

        // Given: GOLD madalya kayitli ve ikinci bir sporcu var
        this.medalRepository.save(this.medal(MedalType.GOLD));
        final Athlete otherAthlete = this.athleteRepository.save(new AthleteBuilder()
                .withoutId()
                .withName(SampleData.NOAH_LYLES)
                .withCountry(this.athlete.getCountry())
                .build());

        // When: diger sporcu ile kontrol
        final boolean exists = this.medalRepository
                .existsByAthleteIdAndSportIdAndType(otherAthlete.getId(), this.sport.getId(), MedalType.GOLD);

        // Then: false
        assertThat(exists).isFalse();

    }

    private Medal medal(final MedalType type) {
        return new MedalBuilder()
                .withoutId()
                .withAthlete(this.athlete)
                .withSport(this.sport)
                .withType(type)
                .build();
    }

}
