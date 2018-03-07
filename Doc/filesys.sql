/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : cas_test

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-07 09:28:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for file_group
-- ----------------------------
DROP TABLE IF EXISTS `file_group`;
CREATE TABLE `file_group` (
  `file_id` varchar(36) NOT NULL,
  `group_id` varchar(36) DEFAULT NULL,
  `group_pid` varchar(36) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `ext` varchar(10) DEFAULT NULL,
  `length` int(15) DEFAULT NULL,
  `md5` varchar(36) DEFAULT NULL,
  `creator_id` varchar(36) DEFAULT NULL,
  `creator_name` varchar(255) DEFAULT NULL,
  `upload_time` bigint(15) DEFAULT NULL,
  `upload_ip` varchar(15) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `can_del` int(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `file_id` varchar(36) NOT NULL COMMENT '文件唯一标识',
  `sys_id` varchar(10) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL COMMENT '上传用户id',
  `title` varchar(255) DEFAULT NULL,
  `ext` varchar(10) DEFAULT NULL COMMENT '文件格式',
  `length` int(15) DEFAULT NULL COMMENT '文件长度',
  `md5` varchar(32) NOT NULL COMMENT '文件md5编码',
  `upload_time` bigint(15) DEFAULT NULL COMMENT '上传时间',
  `real_time` bigint(15) DEFAULT NULL COMMENT '上传文件真正时间,用于保存文件路径',
  `real_file` int(1) DEFAULT NULL COMMENT '是否是真实文件(文件第一次上传时为1)',
  `group_id` varchar(36) DEFAULT NULL COMMENT '文件组id(一般对应一条业务的一个上传模块)',
  `group_pid` varchar(36) DEFAULT NULL COMMENT '文件组最高id(一般对应一条业务)',
  `status` int(1) DEFAULT NULL COMMENT '状态:1正在使用(正式库)2历史文件(历史库)3已移除(历史库)',
  `can_del` int(1) DEFAULT '1' COMMENT '是否可以删除',
  `upload_ip` varchar(15) DEFAULT NULL COMMENT '文件上传ip',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件上传表';

-- ----------------------------
-- Table structure for sys_file2
-- ----------------------------
DROP TABLE IF EXISTS `sys_file2`;
CREATE TABLE `sys_file2` (
  `file_id` varchar(36) NOT NULL COMMENT '文件唯一标识',
  `sys_id` varchar(10) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL COMMENT '上传用户id',
  `title` varchar(255) DEFAULT NULL,
  `ext` varchar(10) DEFAULT NULL COMMENT '文件格式',
  `length` int(15) DEFAULT NULL COMMENT '文件长度',
  `md5` varchar(32) NOT NULL COMMENT '文件md5编码',
  `upload_time` bigint(15) DEFAULT NULL COMMENT '上传时间',
  `real_time` bigint(15) DEFAULT NULL COMMENT '上传文件真正时间,用于保存文件路径',
  `real_file` int(1) DEFAULT NULL COMMENT '是否是真实文件(文件第一次上传时为1)',
  `group_id` varchar(36) DEFAULT NULL COMMENT '文件组id(一般对应一条业务的一个上传模块)',
  `group_pid` varchar(36) DEFAULT NULL COMMENT '文件组最高id(一般对应一条业务)',
  `status` int(1) DEFAULT NULL COMMENT '状态:1正在使用(正式库)2历史文件(历史库)3已移除(历史库)',
  `can_del` int(1) DEFAULT '1' COMMENT '是否可以删除',
  `upload_ip` varchar(15) DEFAULT NULL COMMENT '文件上传ip',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件上传表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(36) NOT NULL,
  `account` varchar(36) DEFAULT NULL,
  `password` varchar(36) DEFAULT NULL,
  `username` varchar(64) DEFAULT NULL,
  `pic_fileid` varchar(36) DEFAULT NULL,
  `details_fileid` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_index` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
