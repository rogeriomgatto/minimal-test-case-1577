create table if not exists demo (
    id uuid not null primary key,
    version int not null default 1,
    route_uuid uuid,
    route_uuid_last_modified_date timestamp with time zone
)