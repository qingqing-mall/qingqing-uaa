--数据库
create database qingqing_uaa default character set utf8mb4 collate utf8mb4_unicode_ci;

CREATE TABLE `jhi_persistent_audit_event` (
`event_id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`principal`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
`event_date`  timestamp NULL DEFAULT NULL ,
`event_type`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`event_id`),
INDEX `idx_persistent_audit_event` (`principal`, `event_date`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=8
ROW_FORMAT=COMPACT
;


CREATE TABLE `jhi_persistent_audit_evt_data` (
`event_id`  bigint(20) NOT NULL ,
`name`  varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL ,
`value`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`event_id`, `name`),
FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `idx_persistent_audit_evt_data` (`event_id`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
ROW_FORMAT=COMPACT
;




DROP TABLE IF EXISTS `qingqing_user`;
CREATE TABLE `qingqing_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(60) NOT NULL COMMENT '用户名',
  `nickname` varchar(60) NOT NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(254) NOT NULL DEFAULT '' COMMENT '邮箱',
  `jhi_password` varchar(256) NOT NULL COMMENT '密码',
  `phone` varchar(60) DEFAULT '' COMMENT '手机号码',
  `image_url` varchar(256) COMMENT '头像',
  `gender` int(1) NOT NULL DEFAULT '0' COMMENT '性别：0-未知；1-男；2-女',
  `birthday` timestamp DEFAULT '0000-00-00 00:00:00' COMMENT '生日',
  `city` varchar(60) COMMENT '所在城市',
  `lang_key` varchar(20) NOT NULL DEFAULT 'zh-cn' COMMENT '语言',
  `active_status` int(1) NOT NULL DEFAULT 1 COMMENT '帐号启用状态:0-禁用；1-启用',
  `activation_status` int(1) NOT NULL DEFAULT 0 COMMENT '帐号激活状态:0-未激活；1-已激活',
  `activation_key` varchar(20) COMMENT '激活key',
  `reset_key` varchar(20) COMMENT '重置密码key',
  `reset_date` timestamp COMMENT '重置密码时间(有效期24小时)',

  `created_by` bigint(20) DEFAULT '0' COMMENT '创建者',
  `created_date` timestamp DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `last_modified_by` bigint(20) DEFAULT '0' COMMENT '更新者',
  `last_modified_date` timestamp DEFAULT '0000-00-00 00:00:00' COMMENT '最后更新时间',

  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';



DROP TABLE IF EXISTS `qingqing_user_receive_address`;
CREATE TABLE `qingqing_user_receive_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(60) NOT NULL COMMENT '收货人名称',
  `phone` varchar(60) NOT NULL COMMENT '收货人电话',
  `default_status` int(1) NOT NULL DEFAULT 0 COMMENT '是否为默认(0-否，1-是)',
  `post_code` varchar(60) NOT NULL DEFAULT '' COMMENT '邮政编码',
  `province` varchar(60) NOT NULL COMMENT '省份/直辖市',
  `city` varchar(60) NOT NULL COMMENT '城市',
  `region` varchar(60)  DEFAULT '' COMMENT '区',
  `detail_address` varchar(256) NOT NULL COMMENT '详细地址(街道)',

  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `last_modified_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新者',
  `last_modified_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收货地址表';



DROP TABLE IF EXISTS `qingqing_role`;
CREATE TABLE `qingqing_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(500) NOT NULL DEFAULT '' COMMENT '描述',
  `active_status` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态：0->禁用；1->启用',

  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `last_modified_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新者',
  `last_modified_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';



DROP TABLE IF EXISTS `qingqing_permission`;
CREATE TABLE `qingqing_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父级权限id：0表示一级',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(500) NOT NULL DEFAULT '' COMMENT '描述',
  `icon` varchar(500) NOT NULL DEFAULT '' COMMENT '图标',
  `jhi_type` int(1)  NOT NULL COMMENT '权限类型：0->目录；1-菜单；2-按钮 3-接口',
  `method` varchar(200) NOT NULL DEFAULT '' COMMENT '方法：GET、POST、PUT、DELETE、PATCH',
  `jhi_sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',

  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `last_modified_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '更新者',
  `last_modified_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权限表';


DROP TABLE IF EXISTS `qingqing_user_role_relation`;
CREATE TABLE `qingqing_user_role_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='角色和权限关系表';


DROP TABLE IF EXISTS `qingqing_role_permission_relation`;
CREATE TABLE `qingqing_role_permission_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='角色和权限关系表';



-- 用户
INSERT INTO `qingqing_user`(id, username, nickname, email, jhi_password, phone, image_url, gender, birthday, city, lang_key, active_status, activation_status, activation_key, reset_key, reset_date) VALUES (1, 'system', 'System', 'system@localhost', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', '', '', 0, NULL, NULL, 'zh-cn', 1, 1, NULL, NULL, NULL);
INSERT INTO `qingqing_user`(id, username, nickname, email, jhi_password, phone, image_url, gender, birthday, city, lang_key, active_status, activation_status, activation_key, reset_key, reset_date) VALUES (2, 'admin', 'Administrator', 'admin@localhost', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', '', '', 0, NULL, NULL, 'zh-cn', 1, 1, NULL, NULL, NULL);
INSERT INTO `qingqing_user`(id, username, nickname, email, jhi_password, phone, image_url, gender, birthday, city, lang_key, active_status, activation_status, activation_key, reset_key, reset_date) VALUES (3, 'user', 'User', 'user@localhost', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', '', '', 0, NULL, NULL, 'zh-cn', 1, 1, NULL, NULL, NULL);

-- 角色
INSERT INTO `qingqing_role`(id, name, description, active_status) VALUES (1, 'ROLE_ADMIN', '管理员', 1);
INSERT INTO `qingqing_role`(id, name, description, active_status) VALUES (2, 'ROLE_USER', '用户', 1);


-- 用户角色关系
INSERT INTO `qingqing_user_role_relation`(id, user_id, role_id) VALUES (1, 1, 1);
INSERT INTO `qingqing_user_role_relation`(id, user_id, role_id) VALUES (2, 2, 1);
INSERT INTO `qingqing_user_role_relation`(id, user_id, role_id) VALUES (3, 2, 2);
INSERT INTO `qingqing_user_role_relation`(id, user_id, role_id) VALUES (4, 3, 2);


-- 收货地址
INSERT INTO `qingqing_user_receive_address`(id, user_id, name, phone, default_status, post_code, province, city, region, detail_address) VALUES (1, 1, '涛逼', '13178319211', 1, '287991', '江苏省', '苏州', '高新区', '金开大道168号');


-- 权限
INSERT INTO `qingqing_permission`(id, parent_id, name, description, icon, jhi_type, method, jhi_sort) VALUES (1, 0, 'user:add', '用户添加','', 3, 'POST', 1);
INSERT INTO `qingqing_permission`(id, parent_id, name, description, icon, jhi_type, method, jhi_sort) VALUES (2, 0, 'user:delete', '用户删除', '', 3, 'DELETE', 1);

