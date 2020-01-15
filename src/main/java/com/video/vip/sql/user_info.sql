/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : video

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 14/01/2020 15:13:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ''主键自增'',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `update_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''修改时间'',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT ''版本号'',
  `pid` bigint(20) NOT NULL COMMENT ''用户pid，passport主键'',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '''' COMMENT ''用户姓名'',
  `head_img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '''' COMMENT ''用户头像'',
  `source` tinyint(2) NOT NULL DEFAULT 0 COMMENT ''用户来源。枚举 UserSourceEnum 0:自己注册 1:好友分享'',
  `referrer_pid` bigint(20) COMMENT ''该用户介绍人pid，passport主键'',
  `vip_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT ''vip状态。枚举 VipStatusEnum 0:未开通 1:已开通 2:已过期'',
  `vip_type` tinyint(2) DEFAULT NULL COMMENT ''vip类型。枚举 VipTypeEnum 1:体验卡 2:月卡 3:季卡 4:半年卡 5:年卡 6:终身卡'',
  `vip_start_date` datetime(0) NULL DEFAULT NULL COMMENT ''vip开通时间'',
  `vip_end_date` datetime(0) DEFAULT NULL COMMENT ''vip到期时间'',
  `user_platform` tinyint(2) NOT NULL DEFAULT 0 COMMENT ''用户所属平台。枚举 UserPlatformEnum 0:C端普通用户 1:B端运营人员'',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `AK_Key_2`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = ''用户基本信息表'' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
