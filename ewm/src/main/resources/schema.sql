create table if not exists categories
(
    id   bigserial
        primary key,
    name varchar(128) unique not null
);

create table if not exists users
(
    id    bigserial
        primary key,
    email varchar(128) unique not null,
    name  varchar(255) not null
);

create table if not exists location
(
    id   bigserial
        primary key,
    lat  float not null,
    lon float not null
);

create table if not exists events
(
    id                 bigserial
        primary key,
    annotation         varchar(1000)         not null,
    category_id        bigint       not null
        constraint events_categories_id_fk
            references categories,
    description        varchar(1000)         not null,
    event_date         timestamp    not null,
    location_id        bigint       not null
        constraint events_location_id_fk
            references location,
    paid               boolean      not null,
    participant_limit  integer      not null,
    request_moderation boolean      not null,
    title              varchar(128) not null,
    initiator_id       bigint       not null
        constraint events_users_id_fk
            references users,
    published_on       timestamp,
    state              varchar(64) default 'pending' not null,
    created            timestamp    not null,
    is_available       boolean      not null
);

create table if not exists compilations
(
    id     bigserial
        primary key,
    title  varchar(128) not null,
    pinned boolean      not null
);

create table if not exists compilations_events
(
    compilation_id integer not null
        constraint compilations_events_compilations_id_fk
            references compilations ON DELETE CASCADE,
    event_id       integer not null
        constraint compilations_events_events_id_fk
            references events ON DELETE CASCADE,
    primary key (compilation_id, event_id)
);

create table if not exists requests
(
    id           serial
        primary key,
    event_id     bigint      not null
        constraint requests_events_id_fk
            references events,
    requester_id bigint      not null
        constraint requests_users_id_fk
            references users,
    status       varchar(64) not null,
    created      timestamp   not null
);

create table if not exists comments
(
    id        bigserial
        constraint comments_pk
            primary key,
    author_id bigint        not null
        constraint comments_users_id_fk
            references users,
    created   timestamp     not null,
    event_id  bigint        not null
        constraint comments_events_id_fk
            references events,
    text      varchar(1000) not null
);



