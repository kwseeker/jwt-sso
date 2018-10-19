set names utf8;
set foreign_key_checks = 0;

create database jwt_sso default character set utf8 collate utf8_general_ci;
use jwt_sso;

create table `sso_user` (
	`id` int(11) not null auto_increment comment '用户ID',
    `username` varchar(20) default null comment '用户名称',
    `password` varchar(40) default null comment '用户密码',
    primary key (`id`)
) auto_increment=1 default charset=utf8;
