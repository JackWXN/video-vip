CREATE TABLE `login_trail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `pid` bigint(20) NOT NULL COMMENT '用户帐户表外键',
  `operation_type` tinyint(2) NOT NULL DEFAULT '-1' COMMENT '操作类型,LoginTypeEnum:1:登录,2:登出',
	`create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `Index_1` (`pid`),
  KEY `Index_2` (`create_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户账户登录轨迹';