# shiro_01_nosecurity
This demo is copy from url:http://www.brucephillips.name/blog/index.cfm/2009/4/5/An-Introduction-to-Ki-formerly-JSecurity--A-Beginners--Tutorial-Part-1
But change the database to mysql on localhost.

Table create SQL:
```sql
create database if not exists shiro charset=utf8;
use shiro;

create table if not exists users(
    userid int not null auto_increment,
    username varchar(32),
    password varchar(64),

    primary key(id),
    unique index users_username_uix(username)
);

create table if not exists user_roles(
    username varchar(32),
    role_name varchar(32),

    primary key(username, role_name)
);
```

Data insert to tables:
```sql
insert into users(username, password) values
('admin', 'admin'),
('aaa', '123'),
('bbb', '123'),
('ccc', '123');

insert into user_roles(username, role_name) values
('aaa', 'user'),
('bbb', 'user'),
('ccc', 'user'),
('admin', 'admin');
```
