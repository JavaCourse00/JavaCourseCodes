CREATE DATABASE IF NOT EXISTS `mysql_demo`;

USE `mysql_demo`;

-- 业务订单表
CREATE TABLE `t_biz_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键;订单id',
  `user_id` bigint(20) NOT NULL COMMENT '用户userId',
  `state` int(8) NOT NULL DEFAULT '0' COMMENT '订单状态;参考OrderStateEnum',
  `create_time` bigint(20) NOT NULL COMMENT '下单时间',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';

-- 订单-商品信息表;
