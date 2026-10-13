package com.huawei.olympic_ranking.sport.testsupport;

import com.huawei.olympic_ranking.sport.model.Sport;
import com.huawei.olympic_ranking.testsupport.SampleData;
import com.huawei.olympic_ranking.testsupport.TestDataBuilder;

public class SportBuilder extends TestDataBuilder<Sport> {

    private Long id = 1L;
    private String name = SampleData.ARCHERY;

    public SportBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public SportBuilder withoutId() {
        this.id = null;
        return this;
    }

    public SportBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public Sport build() {
        return id == null ? Sport.of(name) : Sport.restore(id, name);
    }

}
