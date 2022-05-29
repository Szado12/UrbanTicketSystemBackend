SET FOREIGN_KEY_CHECKS = 0;

create table if not exists users (
    id          bigint primary key,
    username    varchar(255),
    password    varchar(255),
    role        varchar(255),
    active      bit(1),
    name        varchar(255),
    surname     varchar(255)
);

create table if not exists ticket_categories (
    id      bigint primary key,
    name    varchar(255)
);

create table if not exists ticket_types (
    id                  bigint primary key,
    days_of_validity    integer,
    minutes_of_validity integer,
    price               bigint,
    reduced             bit(1),
    category_id         bigint,
    display_name        varchar(255),
    foreign key (category_id) references ticket_categories(id)
);

create table if not exists tickets (
    id                  bigint primary key,
    bought_time         datetime(6),
    status              varchar(255),
    validated_in_bus    integer,
    validated_time      datetime(6),
    type_id             bigint,
    user_id             bigint,
    foreign key (type_id) references ticket_types(id),
    foreign key (user_id) references users(id)
);

create table if not exists hibernate_sequence (
    next_val            bigint
);

SET FOREIGN_KEY_CHECKS = 1;