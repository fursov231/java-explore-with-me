create table if not exists stats
(
    id      bigserial
        primary key,
    app     varchar(128) not null,
    uri     varchar(255) not null,
    ip      varchar(64)  not null,
    timestamp timestamp
);