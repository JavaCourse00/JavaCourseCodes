
## 读写分离

create schema demo_master;
create schema demo_slave_0;
create schema demo_slave_1;

create table demo_master.users(id bigint, name varchar(8), comment varchar(16));
create table demo_slave_0.users(id bigint, name varchar(8), comment varchar(16));
create table demo_slave_1.users(id bigint, name varchar(8), comment varchar(16));

insert into demo_master.users values(1,'KK01','master'),(2,'KK02','master'),(3,'KK03','master');
insert into demo_slave_0.users values(1,'KK01','slave0'),(2,'KK02','slave0'),(3,'KK03','slave0');
insert into demo_slave_1.users values(1,'KK01','slave1'),(2,'KK02','slave1'),(3,'KK03','slave1');


## 分库分表

create schema demo_ds_0;
create schema demo_ds_1;

CREATE TABLE IF NOT EXISTS demo_ds_0.t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS demo_ds_0.t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS demo_ds_1.t_order_0 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS demo_ds_1.t_order_1 (order_id BIGINT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));

CREATE TABLE IF NOT EXISTS demo_ds_0.t_order_item_0 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
CREATE TABLE IF NOT EXISTS demo_ds_0.t_order_item_1 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
CREATE TABLE IF NOT EXISTS demo_ds_1.t_order_item_0 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));
CREATE TABLE IF NOT EXISTS demo_ds_1.t_order_item_1 (order_item_id BIGINT NOT NULL AUTO_INCREMENT, order_id BIGINT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_item_id));

