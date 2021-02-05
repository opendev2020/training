use training;

drop table if exists c_user;
create table c_user (
  c_username varchar(32),
  c_password varchar(60),
  c_name varchar(30),
  primary key (c_username)
);

drop table if exists c_role;
create table c_role (
  c_id varchar(32),
  c_name varchar(50),
  primary key (c_id)
);

drop table if exists c_perm;
create table c_perm (
  c_id varchar(32),
  c_name varchar(50),
  primary key (c_id)
);

drop table if exists c_user_role;
create table c_user_role (
  c_username varchar(32),
  c_role_id varchar(32),
  primary key (c_username, c_role_id),
  FOREIGN KEY (c_username) REFERENCES c_user(c_username)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (c_role_id) REFERENCES c_role(c_id) ON DELETE CASCADE ON UPDATE CASCADE
);

drop table if exists c_role_perm;
create table c_role_perm (
  c_role_id varchar(32),
  c_perm_id varchar(32),
  primary key (c_role_id, c_perm_id),
  FOREIGN KEY (c_role_id) REFERENCES c_role(c_id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (c_perm_id) REFERENCES c_perm(c_id) ON DELETE CASCADE ON UPDATE CASCADE
);

insert into c_user (c_username, c_password, c_name) values ('111', '0e6a053944e52ac34333e6e6df5846a7bcd42787418d5fccd40cd23f', '张三');
insert into c_role (c_id, c_name) values ('sa', '超级管理员');
insert into c_perm (c_id, c_name) values ('yhgl', '用户管理');
insert into c_perm (c_id, c_name) values ('jsgl', '角色管理');
insert into c_perm (c_id, c_name) values ('qxgl', '权限管理');
insert into c_user_role (c_username, c_role_id) values ('111', 'sa');
insert into c_role_perm (c_role_id, c_perm_id) values ('sa', 'yhgl');
insert into c_role_perm (c_role_id, c_perm_id) values ('sa', 'jsgl');
insert into c_role_perm (c_role_id, c_perm_id) values ('sa', 'qxgl');