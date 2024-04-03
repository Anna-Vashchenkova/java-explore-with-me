drop table if exists users cascade;
create table if not exists users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(100) not null,
    name varchar(50) not null,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

drop table if exists compilations cascade;
create table if not exists compilations
(
    id BIGINT generated always as identity primary key,
    pinned bool not null,
    title varchar(100) not null
    );