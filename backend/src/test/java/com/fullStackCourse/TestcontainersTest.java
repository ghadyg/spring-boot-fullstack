package com.fullStackCourse;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainersTest extends AbstractTestcontainer {

    @Test
    void canStartPostgresDB() {
        assertThat(container.isRunning()).isTrue();
        assertThat(container.isCreated()).isTrue();

    }

}
