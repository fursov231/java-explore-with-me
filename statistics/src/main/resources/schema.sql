create table if not exists stats
(
    id      bigserial
        primary key,
    uri     varchar(255) not null,
    ip      varchar(64)  not null,
    created timestamp
);