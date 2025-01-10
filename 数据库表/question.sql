/*
 Navicat Premium Dump SQL

 Source Server         : p1
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3306
 Source Schema         : question

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 01/01/2025 16:03:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operate_user` int unsigned DEFAULT NULL COMMENT '操作人ID',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `class_name` varchar(100) DEFAULT NULL COMMENT '操作的类名',
  `method_name` varchar(100) DEFAULT NULL COMMENT '操作的方法名',
  `method_params` varchar(1000) DEFAULT NULL COMMENT '方法参数',
  `return_value` text COMMENT '返回值',
  `cost_time` bigint DEFAULT NULL COMMENT '方法执行耗时, 单位:ms',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1943 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';



-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) NOT NULL COMMENT '题目标题',
  `question_type` varchar(50) NOT NULL COMMENT '题型（单选题、多选题、判断题等）',
  `category` varchar(100) NOT NULL COMMENT '分类（如Java、数据库等）',
  `correct_choice` varchar(10) NOT NULL COMMENT '正确答案，多个正确答案用逗号分隔',
  `word_book_id` bigint DEFAULT NULL COMMENT '词书ID，外键关联词书表',
  `note` text COMMENT '笔记',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人',
  `status` int NOT NULL DEFAULT '1' COMMENT '1表示启用，0表示关闭',
  PRIMARY KEY (`id`),
  KEY `fk_question_word_book` (`word_book_id`),
  CONSTRAINT `fk_question_word_book` FOREIGN KEY (`word_book_id`) REFERENCES `word_book` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='题目表';

-- ----------------------------
-- Records of question
-- ----------------------------
BEGIN;
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (1, 'Java基础：什么是面向对象编程？', '单选题', 'Java', 'A', 1, '', '2024-12-13 11:31:30', 1, NULL, 1, 1);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (2, 'Java核心：什么是多线程？', '单选题', 'Java', 'B', 2, '多线程是提高程序执行效率的一种方式。', '2024-12-13 11:31:30', 1, NULL, 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (3, '计算机网络：OSI模型有哪些层次？', '单选题', '计算机网络', 'C', 3, 'OSI模型是网络通信中常见的分层模型。', '2024-12-13 11:31:30', 1, '2024-12-18 15:37:07', 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (4, '计算机基础：以下哪些属于操作系统的功能？', '多选题', '计算机基础', 'A,B,C', 1, '操作系统是计算机系统中至关重要的部分，负责管理硬件和软件资源。', '2024-12-13 11:31:30', 1, '2025-01-01 11:17:55', 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (5, 'Java中哪些是面向对象的特征？', '多选题', 'Java', 'A,C', 1, '选择符合面向对象特征的选项123', '2024-12-13 11:32:55', 1, NULL, 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (9, 'Java多线程基础题', '单选题', 'Java', 'A', 1, '考察线程基础知识', '2024-12-17 14:35:36', 1, '2024-12-17 14:35:36', 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (10, '数据库事务隔离级别考察', '多选题', '数据库', 'B,C,F', 2, '相信12212qwe', '2024-12-17 14:45:25', 1, '2024-12-17 14:45:25', 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (48, 'Which of the following are prime numbers?', '多选题', '默认分类', 'A,B', 1, '', '2025-01-01 11:51:43', 1, '2025-01-01 11:51:43', 1, 1);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (49, 'What is the capital of France?', '单选题', '默认分类', 'A', 1, '', '2025-01-01 11:52:39', 1, '2025-01-01 11:52:39', 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (51, 'Which of these are programming languages?', '多选题', '默认分类', 'A,B,C', 1, '', '2025-01-01 11:55:39', 1, '2025-01-01 11:55:39', 1, 0);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (53, 'the first letter', '单选题', '默认分类', 'A', 40, '', '2025-01-01 15:36:58', 19, '2025-01-01 15:36:58', 19, 1);
INSERT INTO `question` (`id`, `title`, `question_type`, `category`, `correct_choice`, `word_book_id`, `note`, `created_at`, `created_by`, `updated_at`, `updated_by`, `status`) VALUES (54, 'the second letter', '单选题', '默认分类', 'B', 40, '', '2025-01-01 15:45:06', 19, '2025-01-01 15:45:06', 19, 1);
COMMIT;

-- ----------------------------
-- Table structure for question_option
-- ----------------------------
DROP TABLE IF EXISTS `question_option`;
CREATE TABLE `question_option` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `question_id` bigint NOT NULL COMMENT '题目ID，外键关联题目表',
  `option_label` char(1) NOT NULL COMMENT '选项标识（A、B、C、D等）',
  `option_text` varchar(255) NOT NULL COMMENT '选项内容',
  PRIMARY KEY (`id`),
  KEY `fk_question_option_question` (`question_id`),
  CONSTRAINT `fk_question_option_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='题目选项表';

-- ----------------------------
-- Records of question_option
-- ----------------------------
BEGIN;
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (17, 5, 'A', '封装');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (18, 5, 'B', '继承');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (19, 5, 'C', '多态');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (20, 5, 'D', '算法优化');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (30, 9, 'A', '实现Runnable接口');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (31, 9, 'B', '继承Thread类');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (32, 9, 'C', '使用ExecutorService接口');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (33, 9, 'D', '使用Callable接口');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (34, 10, 'A', '读未提交会阻止脏读');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (35, 10, 'B', '可重复读会阻止幻读');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (36, 10, 'C', '读已提交会阻止不可重复读');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (37, 10, 'D', '读未提交适合银行转账业务场景');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (38, 10, 'E', '序列化隔离级别不会阻塞任何事务');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (39, 10, 'F', '事务隔离级别越高，性能通常越低');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (44, 1, 'A', '面向对象编程是指通过对象进行编程的思想');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (45, 1, 'B', '面向对象编程是通过数据流编程的思想');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (46, 1, 'C', '面向对象编程是通过函数的方式进行编程');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (47, 1, 'D', '面向对象编程是以结构化编程为基础');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (48, 2, 'A', '多线程是一种编程语言');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (49, 2, 'B', '多线程是指一个程序中可以并发执行多个任务');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (50, 2, 'C', '多线程是指一个程序中只有一个任务');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (51, 2, 'D', '多线程是一种数据库操作技术');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (52, 3, 'A', '物理层、数据链路层、网络层、传输层、会话层、表示层、应用层');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (53, 3, 'B', '物理层、网络层、会话层、应用层');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (54, 3, 'C', '物理层、数据链路层、网络层、传输层、会话层、表示层、应用层');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (55, 3, 'D', '应用层、会话层、传输层、网络层、数据链路层、物理层');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (353, 4, 'A', '管理计算机硬件和软件资源');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (354, 4, 'B', '提供用户接口');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (355, 4, 'C', '执行程序和任务调度');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (356, 4, 'D', '提供互联网连接');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (357, 4, 'E', '管理应用程序的文件系统');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (358, 4, 'F', '确保计算机系统的物理安全');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (359, 48, 'A', '2');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (360, 48, 'B', '4');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (361, 48, 'C', '5');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (362, 48, 'D', '6');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (363, 49, 'A', 'Paris');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (364, 49, 'B', 'London');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (365, 49, 'C', 'Berlin');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (366, 49, 'D', 'Madrid');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (377, 51, 'A', 'Java');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (378, 51, 'B', 'Python');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (379, 51, 'C', 'Ruby');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (380, 51, 'D', 'English');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (381, 51, 'E', 'Spanish');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (382, 51, 'F', 'French');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (385, 53, 'A', 'A');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (386, 53, 'B', 'B');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (387, 53, 'C', 'C');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (388, 53, 'D', 'D');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (389, 54, 'A', 'a');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (390, 54, 'B', 'b');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (391, 54, 'C', 'c');
INSERT INTO `question_option` (`id`, `question_id`, `option_label`, `option_text`) VALUES (392, 54, 'D', 'd');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `role` enum('ADMIN','USER') NOT NULL COMMENT '用户角色',
  `default_word_book_id` bigint DEFAULT NULL COMMENT '默认词书ID',
  `nickname` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username` (`username`),
  KEY `fk_default_word_book` (`default_word_book_id`),
  CONSTRAINT `fk_default_word_book` FOREIGN KEY (`default_word_book_id`) REFERENCES `word_book` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `email`, `role`, `default_word_book_id`, `nickname`, `avatar_url`) VALUES (1, 'admin', 'admin', 'admin@example.com', 'ADMIN', 40, 'FirstLover', '');
INSERT INTO `user` (`id`, `username`, `password`, `email`, `role`, `default_word_book_id`, `nickname`, `avatar_url`) VALUES (2, 'user1', 'user1_password', 'user1@example.com', 'USER', 1, NULL, NULL);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `role`, `default_word_book_id`, `nickname`, `avatar_url`) VALUES (3, 'user2', 'newPass456', 'user2@example.com', 'USER', 1, NULL, NULL);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `role`, `default_word_book_id`, `nickname`, `avatar_url`) VALUES (19, 'cvuser', '123', '123@qq.com', 'USER', 1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_question_stats
-- ----------------------------
DROP TABLE IF EXISTS `user_question_stats`;
CREATE TABLE `user_question_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键关联用户表',
  `question_id` bigint NOT NULL COMMENT '题目ID，外键关联题目表',
  `total_answer_count` int DEFAULT '0' COMMENT '该用户答这道题的总次数',
  `correct_answer_count` int DEFAULT '0' COMMENT '该用户答对这道题的次数',
  `last_answer_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后答题时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_question` (`user_id`,`question_id`),
  KEY `fk_user_question_stats_question_id` (`question_id`),
  CONSTRAINT `fk_user_question_stats_question_id` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_question_stats_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=419 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户答题统计表';

-- ----------------------------
-- Records of user_question_stats
-- ----------------------------
BEGIN;
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (1, 1, 1, 31, 18, '2025-01-01 14:48:20');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (2, 1, 2, 0, 1, '2024-12-13 12:00:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (3, 1, 3, 0, 2, '2024-12-22 12:10:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (4, 1, 4, 1, 1, '2025-01-01 14:23:36');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (5, 1, 5, 7, 5, '2025-01-01 14:24:06');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (7, 2, 1, 0, 3, '2024-12-13 11:45:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (8, 2, 2, 0, 1, '2024-12-13 12:00:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (9, 2, 3, 0, 2, '2024-12-13 12:10:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (10, 2, 4, 0, 0, '2024-12-13 12:20:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (11, 2, 5, 2, 1, '2024-12-17 11:00:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (13, 3, 1, 0, 3, '2024-12-13 11:45:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (14, 3, 2, 0, 1, '2024-12-13 12:00:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (15, 3, 3, 0, 2, '2024-12-13 12:10:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (16, 3, 4, 0, 0, '2024-12-13 12:20:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (17, 3, 5, 2, 1, '2024-12-17 11:00:00');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (20, 1, 9, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (21, 1, 10, 16, 3, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (22, 2, 10, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (23, 3, 10, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (26, 2, 9, 0, 0, '2024-12-17 14:47:21');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (27, 3, 9, 0, 0, '2024-12-22 14:47:31');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (349, 1, 48, 18, 14, '2025-01-01 14:49:44');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (350, 2, 48, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (351, 3, 48, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (358, 1, 49, 2, 1, '2025-01-01 14:24:24');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (359, 2, 49, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (360, 3, 49, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (376, 1, 51, 1, 1, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (377, 2, 51, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (378, 3, 51, 0, 0, NULL);
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (394, 19, 1, 0, 0, '2025-01-01 15:17:25');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (395, 19, 4, 0, 0, '2025-01-01 15:17:25');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (396, 19, 5, 0, 0, '2025-01-01 15:17:25');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (398, 19, 9, 0, 0, '2025-01-01 15:17:25');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (405, 19, 48, 0, 0, '2025-01-01 15:17:25');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (406, 19, 49, 0, 0, '2025-01-01 15:17:25');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (408, 19, 51, 0, 0, '2025-01-01 15:17:25');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (410, 1, 53, 0, 0, '2025-01-01 15:36:57');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (411, 2, 53, 0, 0, '2025-01-01 15:36:57');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (412, 3, 53, 0, 0, '2025-01-01 15:36:57');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (413, 19, 53, 0, 0, '2025-01-01 15:36:57');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (414, 1, 54, 0, 0, '2025-01-01 15:45:06');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (415, 2, 54, 0, 0, '2025-01-01 15:45:06');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (416, 3, 54, 0, 0, '2025-01-01 15:45:06');
INSERT INTO `user_question_stats` (`id`, `user_id`, `question_id`, `total_answer_count`, `correct_answer_count`, `last_answer_time`) VALUES (417, 19, 54, 0, 0, '2025-01-01 15:45:06');
COMMIT;

-- ----------------------------
-- Table structure for user_word_book
-- ----------------------------
DROP TABLE IF EXISTS `user_word_book`;
CREATE TABLE `user_word_book` (
  `word_book_id` bigint NOT NULL COMMENT '词书ID，外键关联词书表',
  `user_id` bigint NOT NULL COMMENT '用户ID，外键关联用户表',
  `permission` enum('READ','WRITE') NOT NULL COMMENT '权限（读取、写入）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`word_book_id`,`user_id`),
  KEY `fk_user_word_book_user_id` (`user_id`),
  CONSTRAINT `fk_user_word_book_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_word_book_word_book_id` FOREIGN KEY (`word_book_id`) REFERENCES `word_book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户词书权限关联表';

-- ----------------------------
-- Records of user_word_book
-- ----------------------------
BEGIN;
INSERT INTO `user_word_book` (`word_book_id`, `user_id`, `permission`, `created_at`) VALUES (1, 1, 'WRITE', '2024-12-13 12:00:00');
INSERT INTO `user_word_book` (`word_book_id`, `user_id`, `permission`, `created_at`) VALUES (1, 3, 'READ', '2024-12-13 12:10:00');
INSERT INTO `user_word_book` (`word_book_id`, `user_id`, `permission`, `created_at`) VALUES (1, 19, 'READ', '2025-01-01 15:17:25');
INSERT INTO `user_word_book` (`word_book_id`, `user_id`, `permission`, `created_at`) VALUES (2, 2, 'WRITE', '2024-12-13 12:20:00');
INSERT INTO `user_word_book` (`word_book_id`, `user_id`, `permission`, `created_at`) VALUES (3, 3, 'WRITE', '2024-12-13 12:30:00');
INSERT INTO `user_word_book` (`word_book_id`, `user_id`, `permission`, `created_at`) VALUES (40, 1, 'READ', '2025-01-01 15:46:15');
INSERT INTO `user_word_book` (`word_book_id`, `user_id`, `permission`, `created_at`) VALUES (40, 19, 'WRITE', '2025-01-01 15:17:12');
COMMIT;

-- ----------------------------
-- Table structure for word_book
-- ----------------------------
DROP TABLE IF EXISTS `word_book`;
CREATE TABLE `word_book` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '词书ID',
  `name` varchar(255) NOT NULL COMMENT '词书名称',
  `description` text NOT NULL COMMENT '词书描述',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `updated_by` bigint NOT NULL COMMENT '更新人ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `share_code` varchar(255) DEFAULT NULL COMMENT '分享码',
  `is_public` tinyint(1) NOT NULL DEFAULT '0' COMMENT '公开状态，0为不可公开，1为公开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name_user` (`name`,`created_by`),
  UNIQUE KEY `unique_share_code` (`share_code`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='词书表';

-- ----------------------------
-- Records of word_book
-- ----------------------------
BEGIN;
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (1, 'Java基础词书', '包含Java基础相关知识的题目', 1, 1, '2024-12-13 11:31:13', '2024-12-20 21:31:33', 'ABCE', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (2, '数据库词书', '包含数据库基础知识的题目', 2, 2, '2024-12-13 11:31:13', '2024-12-20 21:31:33', 'ABCF', 0);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (3, 'test1', 'A test1', 3, 1, '2024-12-13 11:31:13', '2024-12-29 22:22:49', 'ABCH', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (18, 'test', 'A test', 1, 1, '2024-12-20 21:11:21', '2024-12-20 21:31:05', 'ABCD', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (20, 'GRE Vocabulary111222', 'A vocabulary book for GRE exam preparation22', 1, 1, '2024-12-20 21:23:34', '2024-12-20 21:23:34', 'MDBV', 0);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (21, 'GRE Vocabulary111222555', 'A vocabulary book for GRE exam preparation2255', 1, 1, '2024-12-20 21:24:32', '2024-12-20 21:24:32', 'YGWQ', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (22, 'exampleUser的默认词书', '默认词书', 1, 1, '2024-12-22 15:29:38', '2024-12-22 15:29:38', 'GMSR', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (25, 'fUser的默认词书', '默认词书', 1, 1, '2024-12-22 15:35:48', '2024-12-22 15:35:48', 'XYRH', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (26, 'mUser的默认词书', '默认词书', 1, 1, '2024-12-22 19:33:06', '2024-12-22 19:33:06', 'ZVMR', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (29, 'abc的默认词书', '默认词书', 15, 15, '2024-12-23 11:19:39', '2024-12-23 11:19:39', 'AIMK', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (30, 'xUser的默认词书', '默认词书', 16, 16, '2024-12-24 13:43:54', '2024-12-24 13:43:54', 'FKFH', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (31, 'zUser的默认词书', '默认词书', 17, 17, '2024-12-24 14:03:15', '2024-12-24 14:03:15', 'ZHAL', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (32, 'testbook1', 'A vocabulary book for GRE exam preparation2255', 1, 1, '2024-12-25 19:39:04', '2024-12-25 19:39:04', 'IQZW', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (33, 'test11', 'test11', 1, 1, '2024-12-27 16:56:24', '2024-12-27 16:56:24', 'PPBK', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (34, 'test22', 'test22', 1, 1, '2024-12-27 16:58:59', '2024-12-27 16:58:59', 'NIFX', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (35, 'test5', 'test5', 1, 1, '2024-12-29 20:17:12', '2024-12-29 20:17:12', 'WZBJ', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (36, 'test6', 'test61', 1, 1, '2024-12-29 20:44:54', '2024-12-30 13:31:14', 'KQHN', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (37, 'test7', 'test7', 1, 1, '2024-12-29 21:28:25', '2024-12-29 21:28:25', 'BGGH', 1);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (38, 'testb3', 'test', 1, 1, '2025-01-01 14:18:47', '2025-01-01 14:18:47', 'GYZI', 0);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (39, 'tuser1的默认词书', '默认词书', 18, 18, '2025-01-01 14:53:16', '2025-01-01 14:53:16', 'JXUW', 0);
INSERT INTO `word_book` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`, `share_code`, `is_public`) VALUES (40, 'cvuser的默认词书', '默认词书', 19, 19, '2025-01-01 15:17:12', '2025-01-01 15:17:12', 'IQXA', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
