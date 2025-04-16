DROP TABLE IF EXISTS `report_dataset`;
CREATE TABLE `report_dataset` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `rt_id` int DEFAULT NULL,
                                  `sheet_index` int DEFAULT NULL,
                                  `rt_sql` text,
                                  `set_params` text,
                                  `is_pagination` int DEFAULT NULL,
                                  `data_type` int DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `report_sheet_setting`;
CREATE TABLE `report_sheet_setting` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `rt_id` int DEFAULT NULL,
                                        `dyn_header` text,
                                        `sheet_name` varchar(255) DEFAULT NULL,
                                        `sheet_order` int DEFAULT NULL,
                                        `title_name` varchar(255) DEFAULT NULL,
                                        `style_service` varchar(50) DEFAULT NULL,
                                        `cal_formula` varchar(255) DEFAULT NULL,
                                        `sheet_config` text,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `rt_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                          `rt_service` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                          `rt_type` int DEFAULT NULL,
                          `template_set` varchar(255) DEFAULT NULL,
                          `rt_group` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                          `group_order` int DEFAULT NULL,
                          `group_name` varchar(20) DEFAULT NULL,
                          `rt_order` int DEFAULT NULL,
                          `rt_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;