package com.example.demo.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

@Table("demo")
public record DemoEntity(
        @Id
        UUID id,

        @Version
        int version,

        @Nullable
        UUID routeUuid,

        @Nullable
        Instant routeUuidLastModifiedDate) {
}
