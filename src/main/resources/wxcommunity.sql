/*==============================================================*/
/* DBMS name:      MySQL                                    */
/* Created on:     2020/2/20 23:10:29                       */
/* 创建数据库:      CREATE DATABASE wxcommunity;             */
/* 使用数据库:      USE wxcommunity;                         */
/*==============================================================*/
set names utf8;

drop table if exists author_login;
drop table if exists author_post;

/*==============================================================*/
/* Table: author_login                                         */
/*==============================================================*/
create table author_login
(
   aid                   int not null auto_increment,
   author                varchar(100) not null,
   password              varchar(32) not null,
   avatar                varchar(200) not null,
   status                int not null,/*登陆状态 1：登陆 0：未登录*/
   primary key (aid)
);

/*==============================================================*/
/* Table: author_post                                          */
/*==============================================================*/
create table author_post
(
   postId               int not null auto_increment,
   aid                  int not null,
   date                 varchar(19),
   disease              varchar(100) not null,
   avatar               varchar(200),
   imgSrc               varchar(200),
   detail               varchar(1000),
   primary key (postId)
);

ALTER TABLE author_post ADD FOREIGN KEY (aid) REFERENCES author_login(aid) ;
	  
	  
insert  into `author_login`(`aid`,`author`,`password`,`status`) values (1,'qsb','888888','0'),(2,'ghs','888888','0'),(3,'cds','888888','0'),(4,'fgs','888888','0');

insert  into `author_post`(`postId`,`aid`,`date`,`disease`,`avatar`,`imgSrc`,`detail`) values (0,1,'2019-02-09 01:13:17','玉米叶斑病','/images/1.png','/images/plant/yumiyebanbing.jpg','多晒晒太阳就好了1'),(1,2,
'2019-02-09 01:13:17','玉米叶斑病','/images/2.png','/images/plant/yumiyebanbing.jpg','多晒晒太阳就好了2'),(2,3,
'2019-02-09 01:13:17','玉米叶斑病','/images/3.png','/images/plant/yumiyebanbing.jpg','多晒晒太阳就好了3'),(3,4,
'2019-02-09 01:13:17','玉米叶斑病','/images/4.png','/images/plant/yumiyebanbing.jpg','多晒晒太阳就好了4');

