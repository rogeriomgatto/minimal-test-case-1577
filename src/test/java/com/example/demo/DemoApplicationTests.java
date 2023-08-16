package com.example.demo;

import com.example.demo.repository.DemoEntity;
import com.example.demo.repository.DemoRepository;
import com.example.demo.repository.RouteInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class DemoApplicationTests {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    DemoRepository repository;

    @Test
    void shouldPersistEntity() {
        var demo = new DemoEntity(UUID.randomUUID(), 0, null, null);

        repository.save(demo);

        var fromDb = repository.findById(demo.id());
        assertThat(fromDb).hasValueSatisfying(it -> {
            assertThat(it.id()).isEqualTo(demo.id());
            assertThat(it.version()).isGreaterThan(demo.version());
            assertThat(it.routeUuid()).isEqualTo(demo.routeUuid());
            assertThat(it.routeUuidLastModifiedDate()).isEqualTo(demo.routeUuidLastModifiedDate());
        });
    }

    @Test
    void shouldInsertRouteInformationWithParameters() {
        var id = UUID.randomUUID();
        var timestamp = Instant.now().truncatedTo(ChronoUnit.MICROS);

        var inserted = repository.updateRouteInfo(id, null, timestamp);
        assertThat(inserted).isNotNull().satisfies(it -> {
            assertThat(it.id()).isEqualTo(id);
            assertThat(it.version()).isEqualTo(1);
            assertThat(it.routeUuid()).isNull();
            assertThat(it.routeUuidLastModifiedDate()).isEqualTo(timestamp);
        });
    }

    @Test
    @Sql(statements = "insert into demo (id) values ('11111111-1111-1111-1111-111111111111');")
    void shouldUpdateRouteInformationWithParameters() {
        var id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var routeUuid = UUID.randomUUID();
        var timestamp = Instant.now().truncatedTo(ChronoUnit.MICROS);

        var updated = repository.updateRouteInfo(id, routeUuid, timestamp);
        assertThat(updated).isNotNull().satisfies(it -> {
            assertThat(it.id()).isEqualTo(id);
            assertThat(it.version()).isEqualTo(2);
            assertThat(it.routeUuid()).isEqualTo(routeUuid);
            assertThat(it.routeUuidLastModifiedDate()).isEqualTo(timestamp);
        });
    }

    @Test
    void shouldInsertRouteInformationWithDTO() {
        var id = UUID.randomUUID();
        var timestamp = Instant.now().truncatedTo(ChronoUnit.MICROS);

        var inserted = repository.updateRouteInfo(id, new RouteInfo(null, timestamp));
        assertThat(inserted).isNotNull().satisfies(it -> {
            assertThat(it.id()).isEqualTo(id);
            assertThat(it.version()).isEqualTo(1);
            assertThat(it.routeUuid()).isNull();
            assertThat(it.routeUuidLastModifiedDate()).isEqualTo(timestamp);
        });
    }

    @Test
    @Sql(statements = "insert into demo (id) values ('11111111-1111-1111-1111-111111111111');")
    void shouldUpdateRouteInformationWithDTO() {
        var id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        var routeUuid = UUID.randomUUID();
        var timestamp = Instant.now().truncatedTo(ChronoUnit.MICROS);

        var updated = repository.updateRouteInfo(id, new RouteInfo(routeUuid, timestamp));
        assertThat(updated).isNotNull().satisfies(it -> {
            assertThat(it.id()).isEqualTo(id);
            assertThat(it.version()).isEqualTo(2);
            assertThat(it.routeUuid()).isEqualTo(routeUuid);
            assertThat(it.routeUuidLastModifiedDate()).isEqualTo(timestamp);
        });
    }
}
