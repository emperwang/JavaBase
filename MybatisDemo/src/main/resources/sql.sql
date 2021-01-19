drop table if exists users;
create table "users"(
    id serial,
    name varchar(255),
    age int4,
    logdata timestamp default now(),
    primary key(id)
);

insert into "user"(name,age) values('zs','15'),('zs1','15'),('zs2','15');