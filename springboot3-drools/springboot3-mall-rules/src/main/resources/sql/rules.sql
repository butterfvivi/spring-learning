CREATE TABLE `fee_rule` (
                            `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `name` varchar(200) NOT NULL COMMENT '规则名称',
                            `rule` text NOT NULL COMMENT '规则代码',
                            `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态代码，1有效，2关闭',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='代驾费用规则表';

#
# Data for table "fee_rule"
#

#
# Structure for table "profitsharing_rule"
#

CREATE TABLE `profitsharing_rule` (
                                      `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `name` varchar(200) NOT NULL COMMENT '规则名称',
                                      `rule` text NOT NULL COMMENT '规则代码',
                                      `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '状态代码，1有效，2关闭',
                                      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                      `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='分账规则表';

#
# Data for table "profitsharing_rule"
#

#
# Structure for table "reward_rule"
#

CREATE TABLE `reward_rule` (
                               `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `name` varchar(200) NOT NULL COMMENT '规则名称',
                               `rule` text NOT NULL COMMENT '规则代码',
                               `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态代码，1有效，2关闭',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                               `is_deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='奖励规则表';