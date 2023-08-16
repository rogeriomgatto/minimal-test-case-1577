package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface DemoRepository extends CrudRepository<DemoEntity, UUID> {

    @Query("""
            insert into demo (id, route_uuid, route_uuid_last_modified_date)
            values (:id, :#{#routeInfo.routeUuid}, :#{#routeInfo.routeUuidLastModifiedDate})
            on conflict (id) do update set
                version = demo.version + 1,
                route_uuid = excluded.route_uuid,
                route_uuid_last_modified_date = excluded.route_uuid_last_modified_date
            returning *
            """)
    DemoEntity updateRouteInfo(UUID id, RouteInfo routeInfo);

    @Query("""
            insert into demo (id, route_uuid, route_uuid_last_modified_date)
            values (:id, :routeUuid, :routeUuidLastModifiedDate)
            on conflict (id) do update set
                version = demo.version + 1,
                route_uuid = excluded.route_uuid,
                route_uuid_last_modified_date = excluded.route_uuid_last_modified_date
            returning *
            """)
    DemoEntity updateRouteInfo(UUID id, @Nullable UUID routeUuid, Instant routeUuidLastModifiedDate);
}
