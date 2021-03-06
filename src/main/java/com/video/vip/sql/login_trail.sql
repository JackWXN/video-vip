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

 Date: 14/01/2020 15:14:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for login_trail
-- ----------------------------
DROP TABLE IF EXISTS `login_trail`;
CREATE TABLE `login_trail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  `pid` bigint(20) NOT NULL COMMENT '用户帐户表外键',
  `operation_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '操作类型,LoginTypeEnum:【1:登录；2:登出】',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_1`(`pid`) USING BTREE,
  INDEX `Index_2`(`create_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户账户登录轨迹' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
