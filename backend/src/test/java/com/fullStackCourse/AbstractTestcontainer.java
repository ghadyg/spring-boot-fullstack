package com.fullStackCourse;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers
public abstract class AbstractTestcontainer {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        ).load();
        flyway.migrate();

    }

    @Container
    protected static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("ghady-dao-unit-test")
                    .withUsername("ghady")
                    .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry)
    {
        registry.add(
                "spring.datasource.url",
                container::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                container::getUsername
        );
        registry.add(
                "spring.datasource.password",
                container::getPassword
        );
    }

    private static DataSource getDataSource()
    {
        DataSourceBuilder builder = DataSourceBuilder.create()
                .driverClassName(container.getDriverClassName())
                .url(container.getJdbcUrl())
                .username(container.getUsername())
                .password(container.getPassword());

        return builder.build();
    }
    protected static JdbcTemplate getJdbcTemplate()
    {
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker faker = new Faker();
}
