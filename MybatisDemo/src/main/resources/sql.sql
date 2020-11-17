drop table if exists user;
create table "user"(
    id serial,
    name varchar(255),
    age int4,
    primary key(id)
);

insert into "user"(name,age) values('zs','15'),('zs1','15'),('zs2','15');