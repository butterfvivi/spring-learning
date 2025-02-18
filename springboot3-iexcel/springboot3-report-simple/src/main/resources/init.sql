CREATE DATABASE IF NOT EXISTS report DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

use report;

-- ----------------------------
-- Table structure for gaea_report_data_set
-- ----------------------------
DROP TABLE IF EXISTS `report_data_set`;
CREATE TABLE `report_data_set`  (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                         `set_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集编码',
                                         `set_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集名称',
                                         `set_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集描述',
                                         `source_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源编码',
                                         `dyn_sentence` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '动态查询sql或者接口中的请求体',
                                         `case_result` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '结果案例',
                                         `enable_flag` int(1) NULL DEFAULT 1 COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                         `delete_flag` int(1) NULL DEFAULT 0 COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                         `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                         `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                         `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                         `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                         `version` int(8) NULL DEFAULT NULL,
                                         `set_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `unique_set_code`(`set_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据集管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gaea_report_data_set_param
-- ----------------------------
DROP TABLE IF EXISTS `report_data_set_param`;
CREATE TABLE `report_data_set_param`  (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                               `set_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集编码',
                                               `param_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数名',
                                               `param_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数描述',
                                               `param_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数类型，字典=',
                                               `sample_item` varchar(1080) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数示例项',
                                               `required_flag` int(1) NULL DEFAULT 1 COMMENT '0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG',
                                               `validation_rules` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'js校验字段值规则，满足校验返回 true',
                                               `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
                                               `enable_flag` int(1) NULL DEFAULT 1 COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                               `delete_flag` int(1) NULL DEFAULT 0 COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                               `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                               `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                               `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                               `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                               `version` int(8) NULL DEFAULT NULL,
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据集查询参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_data_set_transform
-- ----------------------------
DROP TABLE IF EXISTS `report_data_set_transform`;
CREATE TABLE `report_data_set_transform`  (
                                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                                   `set_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集编码',
                                                   `transform_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据转换类型，DIC_NAME=TRANSFORM_TYPE; js，javaBean，字典转换',
                                                   `transform_script` varchar(10800) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据转换script,处理逻辑',
                                                   `order_num` int(2) NULL DEFAULT NULL COMMENT '排序,执行数据转换顺序',
                                                   `enable_flag` int(1) NULL DEFAULT 1 COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                                   `delete_flag` int(1) NULL DEFAULT 0 COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                                   `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                                   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                   `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                                   `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                                   `version` int(8) NULL DEFAULT NULL,
                                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据集数据转换' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_data_source
-- ----------------------------
DROP TABLE IF EXISTS `report_data_source`;
CREATE TABLE `report_data_source`  (
                                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                            `source_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源编码',
                                            `source_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源名称',
                                            `source_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源描述',
                                            `source_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单',
                                            `source_config` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源连接配置json：关系库{ jdbcUrl:\'\', username:\'\', password:\'\' } ES{ hostList:\'ip1:9300,ip2:9300,ip3:9300\', clusterName:\'elasticsearch_cluster\' }  接口{ apiUrl:\'http://ip:port/url\', method:\'\' } javaBean{ beanNamw:\'xxx\' }',
                                            `enable_flag` int(1) NULL DEFAULT 1 COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                            `delete_flag` int(1) NULL DEFAULT 0 COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                            `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                            `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                            `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                            `version` int(8) NULL DEFAULT NULL,
                                            PRIMARY KEY (`id`) USING BTREE,
                                            UNIQUE INDEX `unique_source_code`(`source_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据源管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_excel
-- ----------------------------
DROP TABLE IF EXISTS `report_excel`;
CREATE TABLE `report_excel`  (
                                      `id` bigint(11) NOT NULL AUTO_INCREMENT,
                                      `report_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报表编码',
                                      `set_codes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集编码，以|分割',
                                      `set_param` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集查询参数',
                                      `json_str` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '报表json串',
                                      `enable_flag` int(1) NULL DEFAULT 1 COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                      `delete_flag` int(1) NULL DEFAULT 0 COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                      `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                      `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                      `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                      `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                      `version` int(8) NULL DEFAULT NULL COMMENT '版本号',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      UNIQUE INDEX `UNIQUE_REPORT_CODE`(`report_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 215 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
