drop database if exists bj;
create database bj;

drop table if exists account;
create table account(
    id int auto_increment,
    name varchar(50),
    money int,
    primary key(id)
)engine=innodb default charset=utf8;

insert into account(name, money) values('david', 2000);

drop database if exists sh;
create database sh;