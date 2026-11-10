package com.huawei.olympic_ranking.athlete.testsupport;

import com.huawei.olympic_ranking.athlete.model.Athlete;
import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.country.testsupport.CountryBuilder;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.TestDataBuilder;

public class AthleteBuilder extends TestDataBuilder<Athlete> {

    private Long id = 1L;
    private String name = SampleData.METE_GAZOZ;
    private Country country = new CountryBuilder().build();

    public AthleteBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public AthleteBuilder withoutId() {
        this.id = null;
        return this;
    }

    public AthleteBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public AthleteBuilder withCountry(final Country country) {
        this.country = country;
        return this;
    }

    @Override
    public Athlete build() {
        return id == null ? Athlete.of(name, country) : Athlete.restore(id, name, country);
    }

}
