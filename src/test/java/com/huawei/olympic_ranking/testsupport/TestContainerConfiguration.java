package com.huawei.olympic_ranking.testsupport;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
@Import({DatabaseCleaner.class, SampleDataLoader.class})
public abstract class TestContainerConfiguration extends LogTrackerConfiguration {

    // ------ H2 (in-memory, Docker gerekmez; bkz. ADR-005) ------
    private static final String H2_DATABASE_NAME = "olympics-test";
    private static final String H2_USERNAME = "sa";
    private static final String H2_PASSWORD = "";

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @DynamicPropertySource
    static void h2Properties(final DynamicPropertyRegistry registry) {
        // DB_CLOSE_DELAY=-1: son baglanti kapansa da context boyunca bellekte kalir; testler gelistirme veritabanindan ("olympics") ayridir.
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:" + H2_DATABASE_NAME + ";DB_CLOSE_DELAY=-1");
        registry.add("spring.datasource.username", () -> H2_USERNAME);
        registry.add("spring.datasource.password", () -> H2_PASSWORD);
    }

    // Context tum testlerde paylasildigi icin her testten once tablolar temizlenir.
    @BeforeEach
    void cleanDatabase() {
        this.databaseCleaner.clean();
    }

}
