-- 创建表
CREATE TABLE `demo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `content` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `c1` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `c2` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `c3` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `c4` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `c5` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `t` (`title`),
  KEY `c` (`content`),
  KEY `i` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
-- 插入1条数据
INSERT INTO `demo`(`title`,`content`,`c1`,`c2`,`c3`,`c4`,`c5`,`create_date`)
VALUES(substring(rand(),3,10),uuid(),uuid(),uuid(),uuid(),uuid(),uuid(),now());
-- 生成数据（多次执行）
INSERT INTO `demo`(`title`,`content`,`c1`,`c2`,`c3`,`c4`,`c5`,`create_date`)
SELECT substring(rand(),3,10),uuid(),uuid(),uuid(),uuid(),uuid(),uuid(),now()
FROM `demo`


CREATE TABLE `c_info` (
                        `c1` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c2` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c3` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c4` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c5` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c6` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c7` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c8` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c9` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c10` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c11` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c12` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c13` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c14` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c15` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c16` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c17` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c18` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c19` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
                        `c20` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

