package com.huawei.olympic_ranking.sport.port;

import com.huawei.olympic_ranking.sport.model.Sport;

import java.util.Optional;

public interface SportRepository {

    Sport save(Sport sport);

    Optional<Sport> findById(Long id);

    /** Ad karsilastirmasi buyuk/kucuk harf duyarsizdir ("Judo" ve "judo" ayni spordur). */
    boolean existsByNameIgnoreCase(String name);

}
