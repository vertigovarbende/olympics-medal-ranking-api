package com.huawei.olympic_ranking.country.testsupport;

import com.huawei.olympic_ranking.country.model.Country;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.TestDataBuilder;

public class CountryBuilder extends TestDataBuilder<Country> {

    private Long id = 1L;
    private String code = SampleData.TUR;
    private String name = SampleData.TUR_NAME;

    public CountryBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public CountryBuilder withoutId() {
        this.id = null;
        return this;
    }

    public CountryBuilder withCode(final String code) {
        this.code = code;
        return this;
    }

    public CountryBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public Country build() {
        return id == null ? Country.of(code, name) : Country.restore(id, code, name);
    }

}
