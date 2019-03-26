/*
Navicat MySQL Data Transfer

Source Server         : javatry
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : flea_market

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-03-26 22:04:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `number` varchar(8) NOT NULL,
  `password` varchar(16) NOT NULL,
  PRIMARY KEY (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('123', '123');
INSERT INTO `user` VALUES ('Easy', '123');
INSERT INTO `user` VALUES ('Lucy', '123');
INSERT INTO `user` VALUES ('Mike', '123');
INSERT INTO `user` VALUES ('Montana', '123');
SET FOREIGN_KEY_CHECKS=1;
