drop table if exists hits cascade;
create table if not exists hits
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app varchar(50) not null,
    uri varchar(50) not null,
    ip varchar(50) not null,
    timestamp TIMESTAMP WITHOUT TIME ZONE
);