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

 Date: 14/01/2020 15:25:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `create_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  `pid` bigint(20) NOT NULL COMMENT '用户pid，passport主键',
  `vip_type` tinyint(2) NOT NULL COMMENT 'vip类型。枚举 VipTypeEnum 1:体验卡 2:月卡 3:季卡 4:半年卡 5:年卡 6:终身卡',
  `order_amount` bigint(20) NOT NULL COMMENT '订单金额，单位分',
  `order_status` tinyint(2) NOT NULL COMMENT '订单状态。枚举 OrderStatusEnum 1:支付失败 2:支付成功',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `AK_Key_2`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
