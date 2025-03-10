CREATE TABLE `luckysheet`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `block_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `row_col` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `index` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `list_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `status` int NULL DEFAULT NULL,
                               `json_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
                               `order` int NULL DEFAULT NULL,
                               `is_delete` int NULL DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of luckysheet
-- ----------------------------
INSERT INTO `luckysheet` VALUES (1, 'fblock', NULL, '1', '1', 1, '{\"row\":84,\"name\":\"Sheet1\",\"chart\":[],\"color\":\"\",\"index\":\"1\",\"order\":0,\"column\":60,\"config\":{},\"status\":0,\"celldata\":[],\"ch_width\":4748,\"rowsplit\":[],\"rh_height\":1790,\"scrollTop\":0,\"scrollLeft\":0,\"visibledatarow\":[],\"visibledatacolumn\":[],\"jfgird_select_save\":[],\"jfgrid_selection_range\":{}}', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
