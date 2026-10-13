package com.huawei.olympic_ranking.testsupport;

import com.huawei.olympic_ranking.athlete.service.AthleteUseCase;
import com.huawei.olympic_ranking.country.service.CountryUseCase;
import com.huawei.olympic_ranking.medal.service.MedalUseCase;
import com.huawei.olympic_ranking.sport.service.SportUseCase;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;


public class SampleDataLoader {

    private final CountryUseCase countryUseCase;
    private final SportUseCase sportUseCase;
    private final AthleteUseCase athleteUseCase;
    private final MedalUseCase medalUseCase;

    private final Map<String, Long> sportIds = new HashMap<>();
    private final Map<String, Long> athleteIds = new HashMap<>();

    public SampleDataLoader(final CountryUseCase countryUseCase,
                            final SportUseCase sportUseCase,
                            final AthleteUseCase athleteUseCase,
                            final MedalUseCase medalUseCase) {
        this.countryUseCase = countryUseCase;
        this.sportUseCase = sportUseCase;
        this.athleteUseCase = athleteUseCase;
        this.medalUseCase = medalUseCase;
    }

    public SampleDataLoader loadAll() {
        this.loadCountries();
        this.loadSports();
        this.loadAthletes();
        this.loadMedals();
        return this;
    }

    public void loadCountries() {
        SampleData.COUNTRIES.forEach(country -> this.countryUseCase.create(country.name(), country.code()));
    }

    public void loadSports() {
        SampleData.SPORTS.forEach(name -> this.sportIds.put(name, this.sportUseCase.create(name).getId()));
    }

    public void loadAthletes() {
        SampleData.ATHLETES.forEach(athlete ->
                this.athleteIds.put(athlete.name(), this.athleteUseCase.create(athlete.name(), athlete.countryCode()).getId()));
    }

    public void loadMedals() {
        SampleData.MEDALS.forEach(medal ->
                this.medalUseCase.create(this.athleteId(medal.athleteName()), this.sportId(medal.sportName()), medal.type()));
    }

    public Long athleteId(final String name) {
        return this.athleteIds.get(name);
    }

    public Long sportId(final String name) {
        return this.sportIds.get(name);
    }

}
