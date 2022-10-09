create table if not exists stats
(
    id      bigserial
        primary key,
    app     varchar(128) not null,
    uri     varchar(255) not null,
    ip      inet  not null,
    timestamp timestamp
);