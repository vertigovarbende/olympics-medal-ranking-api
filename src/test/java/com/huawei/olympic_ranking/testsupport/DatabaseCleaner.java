package com.huawei.olympic_ranking.testsupport;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;

@TestComponent
public class DatabaseCleaner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleaner(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clean() {
        this.jdbcTemplate.execute("DELETE FROM medal");
        this.jdbcTemplate.execute("DELETE FROM athlete");
        this.jdbcTemplate.execute("DELETE FROM sport");
        this.jdbcTemplate.execute("DELETE FROM country");
    }

}
