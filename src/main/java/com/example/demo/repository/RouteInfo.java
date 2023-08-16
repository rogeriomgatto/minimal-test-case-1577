package com.example.demo.repository;

import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

public record RouteInfo(
        @Nullable
        UUID routeUuid,

        Instant routeUuidLastModifiedDate
) {
    public static final RouteInfo EMPTY = new RouteInfo(null, null);
}
