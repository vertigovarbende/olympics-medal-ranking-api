package com.huawei.olympic_ranking.testsupport;

import com.huawei.olympic_ranking.medal.model.MedalType;

import java.util.List;

public final class SampleData {

    public static final String TUR = "TUR";
    public static final String USA = "USA";
    public static final String JPN = "JPN";

    public static final String TUR_NAME = "Türkiye";
    public static final String USA_NAME = "United States";
    public static final String JPN_NAME = "Japan";

    public static final String ARCHERY = "Archery";
    public static final String SWIMMING = "Swimming";
    public static final String JUDO = "Judo";

    public static final String METE_GAZOZ = "Mete Gazoz";
    public static final String NOAH_LYLES = "Noah Lyles";
    public static final String ABBY_STEUTER = "Abby Steuter";
    public static final String HIFUMI_ABE = "Hifumi Abe";

    public static final List<CountrySample> COUNTRIES = List.of(
            new CountrySample(TUR, TUR_NAME),
            new CountrySample(USA, USA_NAME),
            new CountrySample(JPN, JPN_NAME)
    );

    public static final List<String> SPORTS = List.of(ARCHERY, SWIMMING, JUDO);

    public static final List<AthleteSample> ATHLETES = List.of(
            new AthleteSample(METE_GAZOZ, TUR),
            new AthleteSample(NOAH_LYLES, USA),
            new AthleteSample(ABBY_STEUTER, USA),
            new AthleteSample(HIFUMI_ABE, JPN)
    );

    public static final List<MedalSample> MEDALS = List.of(
            new MedalSample(METE_GAZOZ, ARCHERY, MedalType.GOLD),
            new MedalSample(METE_GAZOZ, ARCHERY, MedalType.SILVER),
            new MedalSample(NOAH_LYLES, SWIMMING, MedalType.GOLD),
            new MedalSample(NOAH_LYLES, SWIMMING, MedalType.BRONZE),
            new MedalSample(HIFUMI_ABE, JUDO, MedalType.GOLD)
    );

    private SampleData() {
    }

    public record CountrySample(String code, String name) {

    }

    public record AthleteSample(String name, String countryCode) {

    }

    public record MedalSample(String athleteName, String sportName, MedalType type) {

    }

}
