package com.huawei.olympic_ranking.ranking.repository;

import com.huawei.olympic_ranking.medal.model.entity.MedalEntity;
import com.huawei.olympic_ranking.ranking.repository.projection.AthleteTallyRow;
import com.huawei.olympic_ranking.ranking.repository.projection.CountryTallyRow;
import com.huawei.olympic_ranking.ranking.repository.projection.SportTallyRow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/** Siralama icin madalya sayim sorgulari (salt okunur; yalnizca asagidaki sorgular acik). */
public interface SpringDataRankingRepository extends Repository<MedalEntity, Long> {

    @Query("""
            select new com.huawei.olympic_ranking.ranking.repository.projection.CountryTallyRow(
                c.id, c.code, c.name,
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.GOLD then 1 end),
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.SILVER then 1 end),
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.BRONZE then 1 end))
            from MedalEntity m
            join m.athlete a
            join a.country c
            group by c.id, c.code, c.name
            """)
    List<CountryTallyRow> tallyByCountry();

    /** Sporcu basina altin/gumus/bronz sayisi (sporcunun ulkesiyle); yalnizca en az bir madalyasi olan sporcular doner. */
    @Query("""
            select new com.huawei.olympic_ranking.ranking.repository.projection.AthleteTallyRow(
                a.id, a.name, c.id, c.code, c.name,
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.GOLD then 1 end),
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.SILVER then 1 end),
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.BRONZE then 1 end))
            from MedalEntity m
            join m.athlete a
            join a.country c
            group by a.id, a.name, c.id, c.code, c.name
            """)
    List<AthleteTallyRow> tallyByAthlete();

    /** Spor basina altin/gumus/bronz sayisi; yalnizca en az bir madalya verilmis sporlar doner. */
    @Query("""
            select new com.huawei.olympic_ranking.ranking.repository.projection.SportTallyRow(
                s.id, s.name,
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.GOLD then 1 end),
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.SILVER then 1 end),
                count(case when m.type = com.huawei.olympic_ranking.medal.model.MedalType.BRONZE then 1 end))
            from MedalEntity m
            join m.sport s
            group by s.id, s.name
            """)
    List<SportTallyRow> tallyBySport();

}
