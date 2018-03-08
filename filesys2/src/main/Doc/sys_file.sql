/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : cas_test

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-12-15 18:21:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `file_id` varchar(32) NOT NULL COMMENT '文件唯一标识',
  `sys_id` varchar(10) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '上传用户id',
  `title` varchar(255) DEFAULT NULL,
  `ext` varchar(10) DEFAULT NULL COMMENT '文件格式',
  `length` int(10) DEFAULT NULL COMMENT '文件长度',
  `md5` varchar(32) DEFAULT NULL COMMENT '文件md5编码',
  `upload_time` int(19) DEFAULT NULL COMMENT '上传时间',
  `real_time` int(19) DEFAULT NULL COMMENT '上传文件真正时间,用于保存文件路径',
  `real_file` int(1) DEFAULT NULL COMMENT '是否是真实文件(文件第一次上传时为1)',
  `grop_id` varchar(32) DEFAULT NULL COMMENT '文件组id(一般对应一条业务的一个上传模块)',
  `group_pid` varchar(32) DEFAULT NULL COMMENT '文件组最高id(一般对应一条业务)',
  `status` int(1) DEFAULT NULL COMMENT '状态',
  `can_del` int(1) DEFAULT '1' COMMENT '是否可以删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件上传表';
