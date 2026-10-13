package com.huawei.olympic_ranking.sport.integration.port.adapter;

import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.sport.port.SportRepository;
import com.huawei.olympic_ranking.sport.testsupport.SportBuilder;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.TestContainerConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SportRepositoryAdapter - Entegrasyon Testleri")
class SportRepositoryAdapterIT extends TestContainerConfiguration {

    @Autowired
    private SportRepository sportRepository;

    @Test
    @DisplayName("save() yeni spor'u kaydeder ve id atar")
    void shouldSaveSport_WhenNewSport() {

        // Given: id'si olmayan spor
        final Sport sport = new SportBuilder().withoutId().build();

        // When: save
        final Sport saved = this.sportRepository.save(sport);

        // Then: id atanir, ad korunur
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(SampleData.ARCHERY);

    }

    @Test
    @DisplayName("save() ayni adli ikinci spor'da veri butunlugu hatasi firlatir")
    void shouldThrowException_WhenNameIsDuplicated() {

        // Given: Archery kayitli
        this.sportRepository.save(new SportBuilder().withoutId().build());
        final Sport duplicate = new SportBuilder().withoutId().build();

        // When: ayni adla ikinci kayit
        // Then: benzersizlik kisiti ihlal edilir
        assertThatThrownBy(() -> this.sportRepository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("findById() kayitli spor'u bulur")
    void shouldFindSport_WhenIdExists() {

        // Given: kayitli spor
        final Sport saved = this.sportRepository.save(new SportBuilder().withoutId().build());

        // When: findById
        final Optional<Sport> found = this.sportRepository.findById(saved.getId());

        // Then: ayni spor doner
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(SampleData.ARCHERY);

    }

    @Test
    @DisplayName("findById() kayit yoksa bos doner")
    void shouldReturnEmpty_WhenIdDoesNotExist() {

        // Given: bos veritabani
        // When: findById
        final Optional<Sport> found = this.sportRepository.findById(999_999L);

        // Then: sonuc bos
        assertThat(found).isEmpty();

    }

    @Test
    @DisplayName("existsByNameIgnoreCase() ayni adi bulur")
    void shouldReturnTrue_WhenNameExists() {

        // Given: Archery kayitli
        this.sportRepository.save(new SportBuilder().withoutId().build());

        // When: ayni adla kontrol
        final boolean exists = this.sportRepository.existsByNameIgnoreCase(SampleData.ARCHERY);

        // Then: true
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("existsByNameIgnoreCase() buyuk/kucuk harf farkini yok sayar")
    void shouldReturnTrue_WhenNameDiffersOnlyByCase() {

        // Given: Archery kayitli
        this.sportRepository.save(new SportBuilder().withoutId().build());

        // When: farkli harf buyuklugunde kontrol
        final boolean exists = this.sportRepository.existsByNameIgnoreCase("aRcHeRy");

        // Then: true
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("existsByNameIgnoreCase() kayitsiz ad icin false doner")
    void shouldReturnFalse_WhenNameDoesNotExist() {

        // Given: yalnizca Archery kayitli
        this.sportRepository.save(new SportBuilder().withoutId().build());

        // When: baska adla kontrol
        final boolean exists = this.sportRepository.existsByNameIgnoreCase(SampleData.JUDO);

        // Then: false
        assertThat(exists).isFalse();

    }

}
