drop table if exists users cascade;
create table if not exists users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(254) not null,
    name varchar(250) not null,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

drop table if exists categories cascade;
create table if not exists categories
(
    id BIGINT generated always as identity primary key,
    name varchar(50) not null,
    CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (name)

    );

drop table if exists events cascade;
create table if not exists events
(
    id bigint generated always as identity primary key,
    title varchar(120) not null,
    annotation varchar(2000) not null,
    description varchar(7000),
    category_id bigint not null,
    state varchar(10) not null,
    lat real not null,
    lon real not null,
    event_date timestamp without time zone not null,
    created_on timestamp without time zone not null,
    published_on timestamp without time zone,
    initiator_id bigint not null,
    paid bool not null,
    participant_limit  bigint,
    request_moderation boolean,
    confirmed_requests bigint,
    views bigint,
    foreign key (category_id) references categories (id) on delete cascade,
    foreign key (initiator_id) references users (id) on delete cascade
    );

drop table if exists requests cascade;
create table if not exists requests
(
    id bigint generated always as identity primary key,
    event_id bigint not null,
    requester_id bigint not null,
    created timestamp without time zone not null,
    status varchar(9),
    foreign key (event_id) references events (id) on delete cascade,
    foreign key (requester_id) references users (id) on delete cascade
    );

drop table if exists compilations cascade;
create table if not exists compilations
(
    id bigint generated always as identity primary key,
    pinned bool not null,
    title varchar(50) not null,
    CONSTRAINT UQ_TITLE_COMPILATION UNIQUE (title)
    );

drop table if exists event_compilations cascade;
create table if not exists event_compilations
(
    compilation_id bigint not null references compilations (id) on delete cascade,
    event_id       bigint not null references events (id) on delete cascade
    );

drop table if exists comments cascade;
create table if not exists comments

(
    id bigint generated always as identity primary key,
    text varchar(2000) not null,
    owner_id bigint not null,
    event_id bigint not null,
    comment_created_on timestamp without time zone not null,
    foreign key (owner_id) references users (id) on delete cascade,
    foreign key (event_id) references events (event_id)
    );