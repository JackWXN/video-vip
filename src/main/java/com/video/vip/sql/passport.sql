CREATE TABLE `passport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `phone` varchar(20) DEFAULT NULL COMMENT '帐号手机号，用户的手机号，全表唯一',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '帐号登录密码，md5加密',
  `password_salt` varchar(6) NOT NULL DEFAULT '' COMMENT '帐号密码盐，密码+密码盐生成md5',
  `status` tinyint(2) NOT NULL DEFAULT '-1' COMMENT '帐号状态，帐号状态，UserStatusEnum，1:禁用,2:启用,3:冻结',
  `account` varchar(30) DEFAULT NULL COMMENT '帐号，用来登录的帐号名称',
  `mail` varchar(50) DEFAULT NULL COMMENT '帐号邮箱，全表唯一',
  `password_edit_date` varchar(8) NOT NULL DEFAULT '' COMMENT '尝试修改密码天，和当天尝试修改密码次数联合使用，格式yyyyMMrr',
  `password_edit_date_count` tinyint(4) DEFAULT '0' COMMENT '当天尝试修改密码次数，和尝试修改密码天联合使用。每天清零，每天尝试修改密码次数最多5次',
  `password_edit_reset_count` tinyint(4) DEFAULT '0' COMMENT '重置当天修改密码次数，和尝试修改密码天联合使用。最高重置3次',
  `pp_ready_pwd` varchar(50) NOT NULL DEFAULT '' COMMENT '预处理密码，md5加密。确认生效后才替换主密码',
  `pp_ready_pwd_salt` varchar(6) NOT NULL DEFAULT '' COMMENT '预处理密码盐，确认生效后才替换主密码盐',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_Key_2` (`phone`) USING BTREE,
  UNIQUE KEY `AK_Key_3` (`account`) USING BTREE,
  UNIQUE KEY `AK_Key_mail` (`mail`) USING BTREE,
  KEY `Index_3` (`create_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户账号表。所有人员都根据手机号有唯一的帐号';