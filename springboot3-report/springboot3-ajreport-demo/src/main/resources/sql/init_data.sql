INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('log_ajdevices', '日志-资产统计', '', 'mysql_ajreport', 'SELECT device_type,COUNT(device_id)as nums from report_devices GROUP BY device_type;', '[{\"device_type\":\"交换机\",\"nums\":12},{\"device_type\":\"服务器\",\"nums\":10},{\"device_type\":\"路由器\",\"nums\":4},{\"device_type\":\"防火墙\",\"nums\":2}]', 1, 0, 'admin', '2021-06-30 09:57:47', 'admin', '2021-06-30 09:57:47', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('log_ajfireacl', '日志-防火墙ACL次数', '', 'mysql_ajreport', 'SELECT acl_type,COUNT(id) as nums from report_init.report_fireacl GROUP BY acl_type;', '[{\"acl_type\":\"LDAP389\",\"nums\":13},{\"acl_type\":\"Server1433\",\"nums\":9},{\"acl_type\":\"共享文件445\",\"nums\":7},{\"acl_type\":\"存储514\",\"nums\":14},{\"acl_type\":\"网页浏览443\",\"nums\":31},{\"acl_type\":\"远程桌面3389\",\"nums\":62},{\"acl_type\":\"远程登陆23\",\"nums\":66}]', 1, 0, 'admin', '2021-06-30 10:41:36', 'admin', '2021-06-30 10:41:36', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('log_ajattack', '日志-攻击占比', '', 'mysql_ajreport', 'SELECT attack_type,COUNT(id)as nums from report_fireattack GROUP BY attack_type;', '[{\"attack_type\":\"SQL注入\",\"nums\":9},{\"attack_type\":\"UDP\",\"nums\":11},{\"attack_type\":\"WEB\",\"nums\":7},{\"attack_type\":\"僵尸网络\",\"nums\":24},{\"attack_type\":\"弱口令\",\"nums\":6},{\"attack_type\":\"端口扫描\",\"nums\":16},{\"attack_type\":\"网站扫描\",\"nums\":8},{\"attack_type\":\"超大ICMP\",\"nums\":19}]', 1, 0, 'admin', '2021-06-30 10:44:01', 'admin', '2021-06-30 13:13:56', 3, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('log_ajmailfailtop5', '日志-邮件认证失败top5', '', 'mysql_ajreport', 'SELECT username,count(id)as nums from report_mail WHERE status=0 GROUP BY username ORDER BY nums desc limit 5;', '[{\"nums\":63,\"username\":\"zhangsi\"},{\"nums\":52,\"username\":\"wangwu\"},{\"nums\":39,\"username\":\"liliu\"},{\"nums\":39,\"username\":\"IT1\"},{\"nums\":30,\"username\":\"IT2\"}]', 1, 0, 'admin', '2021-06-30 11:07:53', 'admin', '2021-06-30 11:07:53', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('log_ajmailfail', '日志-邮件认证失败趋势', '', 'mysql_ajreport', 'SELECT DATE_FORMAT(create_time,\'%Y-%m-%d\')as date,count(id)as nums from report_mail WHERE status=0 \ngroup by date', '[{\"date\":\"2021-06-21\",\"nums\":25},{\"date\":\"2021-06-22\",\"nums\":16},{\"date\":\"2021-06-23\",\"nums\":89},{\"date\":\"2021-06-24\",\"nums\":61},{\"date\":\"2021-06-25\",\"nums\":53}]', 1, 0, 'admin', '2021-06-30 12:58:19', 'admin', '2021-06-30 12:58:19', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('log_ajwifiamount', '日志-wifi登陆趋势', '', 'mysql_ajreport', 'SELECT * from report_wifiamount;', '[{\"fail\":15,\"datetime\":\"2021-06-17\",\"success\":210},{\"fail\":43,\"datetime\":\"2021-06-18\",\"success\":234},{\"fail\":28,\"datetime\":\"2021-06-19\",\"success\":199},{\"fail\":80,\"datetime\":\"2021-06-20\",\"success\":260},{\"fail\":45,\"datetime\":\"2021-06-21\",\"success\":245},{\"fail\":26,\"datetime\":\"2021-06-22\",\"success\":216},{\"fail\":10,\"datetime\":\"2021-06-23\",\"success\":150}]', 1, 0, 'admin', '2021-06-30 13:07:35', 'admin', '2021-06-30 13:07:35', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('car_ajpro', '汽车-生产趋势', '', 'mysql_ajreport', 'SELECT datetime,sum(manus)as nums from report_manus GROUP BY datetime;', '[{\"datetime\":\"2021-06-18\",\"nums\":252},{\"datetime\":\"2021-06-19\",\"nums\":133},{\"datetime\":\"2021-06-20\",\"nums\":189},{\"datetime\":\"2021-06-21\",\"nums\":219},{\"datetime\":\"2021-06-22\",\"nums\":159}]', 1, 0, 'admin', '2021-06-30 14:23:02', 'admin', '2021-06-30 14:23:02', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('car_ajsale', '汽车-销售趋势', '', 'mysql_ajreport', 'SELECT datetime,sum(sales)as nums from report_manus GROUP BY datetime;', '[{\"datetime\":\"2021-06-18\",\"nums\":231},{\"datetime\":\"2021-06-19\",\"nums\":140},{\"datetime\":\"2021-06-20\",\"nums\":170},{\"datetime\":\"2021-06-21\",\"nums\":191},{\"datetime\":\"2021-06-22\",\"nums\":144}]', 1, 0, 'admin', '2021-06-30 14:24:33', 'admin', '2021-06-30 14:24:33', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('car_ajproTop5', '汽车-生产TOP5', '', 'mysql_ajreport', 'SELECT brand,sum(manus)as nums from report_manus GROUP BY brand ORDER BY nums desc limit 5;', '[{\"brand\":\"E-30\",\"nums\":215},{\"brand\":\"C-50\",\"nums\":210},{\"brand\":\"D-40\",\"nums\":175},{\"brand\":\"A-100\",\"nums\":100},{\"brand\":\"A-110\",\"nums\":97}]', 1, 0, 'admin', '2021-06-30 14:26:55', 'admin', '2021-06-30 14:26:55', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('car_ajsaleTop5', '汽车-销售TOP5', '', 'mysql_ajreport', 'SELECT brand,sum(sales)as nums from report_manus GROUP BY brand ORDER BY nums desc limit 5;', '[{\"brand\":\"E-30\",\"nums\":221},{\"brand\":\"C-50\",\"nums\":189},{\"brand\":\"D-40\",\"nums\":169},{\"brand\":\"A-100\",\"nums\":81},{\"brand\":\"B-100\",\"nums\":80}]', 1, 0, 'admin', '2021-06-30 14:30:00', 'admin', '2021-06-30 14:30:00', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('car_ajunsale', '汽车-滞销', '', 'mysql_ajreport', 'SELECT brand,sum(unsales) as nums from report_manus GROUP BY brand ORDER BY nums desc;', '[{\"brand\":\"C-50\",\"nums\":21},{\"brand\":\"A-110\",\"nums\":20},{\"brand\":\"A-100\",\"nums\":19},{\"brand\":\"B-110\",\"nums\":11},{\"brand\":\"E-30\",\"nums\":9},{\"brand\":\"D-40\",\"nums\":6},{\"brand\":\"B-100\",\"nums\":5}]', 1, 0, 'admin', '2021-06-30 14:30:46', 'admin', '2021-06-30 14:30:46', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('car_ajrework', '汽车-返修', '', 'mysql_ajreport', 'SELECT brand,sum(rework) as nums from report_manus GROUP BY brand ORDER BY nums desc;', '[{\"brand\":\"E-30\",\"nums\":58},{\"brand\":\"D-40\",\"nums\":27},{\"brand\":\"C-50\",\"nums\":22},{\"brand\":\"A-100\",\"nums\":5},{\"brand\":\"A-110\",\"nums\":5},{\"brand\":\"B-110\",\"nums\":5},{\"brand\":\"B-100\",\"nums\":4}]', 1, 0, 'admin', '2021-06-30 14:31:16', 'admin', '2021-06-30 14:31:16', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('car_ajreturn', '汽车-退货', '', 'mysql_ajreport', 'SELECT brand,sum(`return`) as nums from report_manus GROUP BY brand ORDER BY nums desc;', '[{\"brand\":\"E-30\",\"nums\":24},{\"brand\":\"C-50\",\"nums\":10},{\"brand\":\"D-40\",\"nums\":6},{\"brand\":\"A-110\",\"nums\":2},{\"brand\":\"B-110\",\"nums\":1},{\"brand\":\"A-100\",\"nums\":0},{\"brand\":\"B-100\",\"nums\":0}]', 1, 0, 'admin', '2021-06-30 14:31:45', 'admin', '2021-06-30 14:31:45', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('acc_ajacc', '访问-访问人数趋势', '', 'mysql_ajreport', 'SELECT datetime,access from report_access ORDER BY datetime;', '[{\"datetime\":\"2021-06-18\",\"access\":1000},{\"datetime\":\"2021-06-19\",\"access\":1200},{\"datetime\":\"2021-06-20\",\"access\":1600},{\"datetime\":\"2021-06-21\",\"access\":2000},{\"datetime\":\"2021-06-22\",\"access\":800}]', 1, 0, 'admin', '2021-06-30 15:15:17', 'admin', '2021-06-30 15:16:04', 2, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('acc_ajregister', '访问-注册人数趋势', '', 'mysql_ajreport', 'SELECT datetime,register from report_access ORDER BY datetime;', '[{\"datetime\":\"2021-06-18\",\"register\":12},{\"datetime\":\"2021-06-19\",\"register\":20},{\"datetime\":\"2021-06-20\",\"register\":40},{\"datetime\":\"2021-06-21\",\"register\":100},{\"datetime\":\"2021-06-22\",\"register\":30}]', 1, 0, 'admin', '2021-06-30 15:15:55', 'admin', '2021-06-30 15:15:55', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('acc_ajrt', '访问-系统RT', '', 'mysql_ajreport', 'SELECT datetime,rt from report_exper ORDER BY datetime;', '[{\"datetime\":\"2021-06-18\",\"rt\":90.92},{\"datetime\":\"2021-06-19\",\"rt\":100.02},{\"datetime\":\"2021-06-20\",\"rt\":98.89},{\"datetime\":\"2021-06-21\",\"rt\":110.99},{\"datetime\":\"2021-06-22\",\"rt\":89.78}]', 1, 0, 'admin', '2021-06-30 15:16:37', 'admin', '2021-07-01 16:06:00', 2, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('acc_ajqps', '访问-系统QPS', '', 'mysql_ajreport', 'SELECT datetime,qps from report_exper ORDER BY datetime;', '[{\"datetime\":\"2021-06-18\",\"qps\":9000},{\"datetime\":\"2021-06-19\",\"qps\":10000},{\"datetime\":\"2021-06-20\",\"qps\":9560},{\"datetime\":\"2021-06-21\",\"qps\":13456},{\"datetime\":\"2021-06-22\",\"qps\":8990}]', 1, 0, 'admin', '2021-06-30 15:19:06', 'admin', '2021-06-30 15:19:06', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('acc_ajerror', '访问-系统ERROR', '', 'mysql_ajreport', 'SELECT datetime,error from report_exper ORDER BY datetime;', '[{\"datetime\":\"2021-06-18\",\"error\":2},{\"datetime\":\"2021-06-19\",\"error\":3},{\"datetime\":\"2021-06-20\",\"error\":1},{\"datetime\":\"2021-06-21\",\"error\":9},{\"datetime\":\"2021-06-22\",\"error\":3}]', 1, 0, 'admin', '2021-06-30 15:19:33', 'admin', '2021-06-30 15:19:33', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('amount_1', 'amount1', '', 'mysql_ajreport', 'SELECT sum(success)as nums from report_wifiamount;', '[{\"nums\":1514}]', 1, 0, 'admin', '2021-07-05 15:00:18', 'admin', '2021-07-05 15:00:18', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('logis_1', '库存', '', 'mysql_ajreport', 'select name,nums from report_common1 order by nums', '[{\"name\":\"上海\",\"nums\":500},{\"name\":\"北京\",\"nums\":600},{\"name\":\"西安\",\"nums\":1000},{\"name\":\"河南\",\"nums\":1200},{\"name\":\"武汉\",\"nums\":2000}]', 1, 0, 'admin', '2021-07-06 15:44:41', 'admin', '2021-07-06 15:54:16', 3, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('logis_2', '收车量', '', 'mysql_ajreport', 'select name,nums from report_common2 order by nums;', '[{\"name\":\"武汉\",\"nums\":20},{\"name\":\"河南\",\"nums\":50},{\"name\":\"西安\",\"nums\":70},{\"name\":\"北京\",\"nums\":100},{\"name\":\"上海\",\"nums\":200}]', 1, 0, 'admin', '2021-07-06 16:51:27', 'admin', '2021-07-06 16:51:27', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('logis_3', '收发车情况', '', 'mysql_ajreport', 'select time,collect,start from report_common3;', '[{\"start\":8,\"time\":\"1月\",\"collect\":10},{\"start\":12,\"time\":\"2月\",\"collect\":15},{\"start\":22,\"time\":\"3月\",\"collect\":20},{\"start\":28,\"time\":\"4月\",\"collect\":30},{\"start\":35,\"time\":\"5月\",\"collect\":28},{\"start\":38,\"time\":\"6月\",\"collect\":40},{\"start\":100,\"time\":\"7月\",\"collect\":80},{\"start\":120,\"time\":\"8月\",\"collect\":90},{\"start\":89,\"time\":\"9月\",\"collect\":65},{\"start\":50,\"time\":\"10月\",\"collect\":50},{\"start\":34,\"time\":\"11月\",\"collect\":35},{\"start\":23,\"time\":\"12月\",\"collect\":27}]', 1, 0, 'admin', '2021-07-06 17:24:16', 'admin', '2021-07-06 17:30:15', 2, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('logis_table', '表格测试', '', 'mysql_ajreport', 'select date,address,name from report_table;', '[{\"date\":\"2021-05-01\",\"address\":\"这是一条测试表格事件1\",\"name\":\"上汽安吉\"},{\"date\":\"2021-05-02\",\"address\":\"这是一条测试表格事件2\",\"name\":\"上汽大通\"},{\"date\":\"2021-05-03\",\"address\":\"这是一条测试表格事件3\",\"name\":\"上汽智行\"},{\"date\":\"2021-05-04\",\"address\":\"这是一条测试表格事件4\",\"name\":\"上汽国际\"},{\"date\":\"2021-05-05\",\"address\":\"这是一条测试表格事件5\",\"name\":\"上汽国内\"},{\"date\":\"2021-05-06\",\"address\":\"这是一条测试表格事件6\",\"name\":\"上汽运输\"},{\"date\":\"2021-05-07\",\"address\":\"这是一条测试表格事件7\",\"name\":\"上汽大众\"}]', 1, 0, 'admin', '2021-07-06 17:56:23', 'admin', '2021-07-07 10:59:34', 4, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('per', '百分比', '', 'mysql_ajreport', 'select doub from report_nums where id =2;', '[{\"doub\":55.33}]', 1, 0, 'admin', '2021-07-14 16:17:14', 'admin', '2021-07-15 10:35:21', 2, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('barstack_ajreport', '柱状堆叠数据', '', 'mysql_ajreport', 'select time,type,nums from report_barstack', '[{\"time\":\"2021-07-27\",\"type\":\"A\",\"nums\":12},{\"time\":\"2021-07-27\",\"type\":\"B\",\"nums\":20},{\"time\":\"2021-07-27\",\"type\":\"C\",\"nums\":11},{\"time\":\"2021-07-26\",\"type\":\"A\",\"nums\":11},{\"time\":\"2021-07-26\",\"type\":\"B\",\"nums\":30},{\"time\":\"2021-07-25\",\"type\":\"B\",\"nums\":20},{\"time\":\"2021-07-25\",\"type\":\"C\",\"nums\":15}]', 1, 0, 'admin', '2021-07-27 19:50:52', 'admin', '2021-08-16 14:08:51', 7, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`, `set_type`) VALUES ('compare_ajreport', '柱状对比图示例数据', '', 'mysql_ajreport', 'SELECT time,type,nums from report_comparestack', '[{\"time\":\"2021-08-23\",\"type\":\"成功\",\"nums\":12},{\"time\":\"2021-08-23\",\"type\":\"失败\",\"nums\":1},{\"time\":\"2021-08-24\",\"type\":\"成功\",\"nums\":24},{\"time\":\"2021-08-24\",\"type\":\"失败\",\"nums\":5},{\"time\":\"2021-08-25\",\"type\":\"成功\",\"nums\":13},{\"time\":\"2021-08-25\",\"type\":\"失败\",\"nums\":8},{\"time\":\"2021-08-26\",\"type\":\"成功\",\"nums\":19},{\"time\":\"2021-08-26\",\"type\":\"失败\",\"nums\":3},{\"time\":\"2021-08-27\",\"type\":\"成功\",\"nums\":9},{\"time\":\"2021-08-27\",\"type\":\"失败\",\"nums\":15}]', 1, 0, 'admin', '2021-08-27 13:48:33', 'admin', '2021-08-27 13:48:33', 1, 'sql');
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `set_type`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`) VALUES ('report_city_total', '城市数据总览', '', 'sql', 'mysql_ajreport', 'SELECT city_name,sum(nums) sum_nums  FROM report_city where create_time>=\'${startTime}\' and create_time < \'${endTime}\' group by city_name', '[{"city_name":"上海","sum_nums":138},{"city_name":"北京","sum_nums":188},{"city_name":"天津","sum_nums":150},{"city_name":"广州","sum_nums":134},{"city_name":"成都","sum_nums":125},{"city_name":"杭州","sum_nums":140},{"city_name":"重庆","sum_nums":135}]', 1, 0, 'admin', '2023-05-10 16:10:27', 'admin', '2023-05-12 09:43:42', 9);
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `set_type`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`) VALUES ('report_single_city', '单一城市数据', '', 'sql', 'mysql_ajreport', 'SELECT DATE_FORMAT(create_time,\'%Y-%m-%d\') create_time,sum(nums) sum_nums  FROM report_city where city_name =\'${city_name}\' group by create_time ;', '[{"create_time":"2023-05-05","sum_nums":25},{"create_time":"2023-05-06","sum_nums":35},{"create_time":"2023-05-07","sum_nums":45},{"create_time":"2023-05-08","sum_nums":12},{"create_time":"2023-05-09","sum_nums":44},{"create_time":"2023-05-10","sum_nums":27}]', 1, 0, 'admin', '2023-05-10 16:15:57', 'admin', '2023-05-10 16:15:57', 1);
INSERT INTO `report`.`report_data_set`(`set_code`, `set_name`, `set_desc`, `set_type`, `source_code`, `dyn_sentence`, `case_result`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`) VALUES ('report_city_select', '城市名称下拉', '', 'sql', 'mysql_ajreport', 'SELECT DISTINCT(city_code)city_code ,city_name  FROM report_city group by city_code,city_name  ;', '[{"city_name":"北京","city_code":"beijing"},{"city_name":"成都","city_code":"chengdu"},{"city_name":"重庆","city_code":"chongqing"},{"city_name":"广州","city_code":"guangzhou"},{"city_name":"杭州","city_code":"hangzhou"},{"city_name":"上海","city_code":"shanghai"},{"city_name":"天津","city_code":"tianjin"}]', 1, 0, 'admin', '2023-05-10 16:20:50', 'admin', '2023-05-10 16:20:50', 1);


INSERT INTO `report`.`report_data_set_param` (set_code, param_name, param_desc, param_type, sample_item, required_flag, validation_rules, order_num, enable_flag, delete_flag, create_by, create_time, update_by, update_time, version) VALUES ('report_single_city', 'city_name', '', '', '北京', 1, 'function verification(data){
	//自定义脚本内容
	//可返回true/false单纯校验键入的data正确性
	//可返回文本，实时替换,比如当前时间等
	//return "2099-01-01 00:00:00";
	return true;
}', null, 1, 0, 'admin', '2023-05-10 16:15:57', 'admin', '2023-05-10 16:15:57', 1);
INSERT INTO `report`.`report_data_set_param` (set_code, param_name, param_desc, param_type, sample_item, required_flag, validation_rules, order_num, enable_flag, delete_flag, create_by, create_time, update_by, update_time, version) VALUES ('report_city_total', 'startTime', '', '', '2023-05-12', 1, 'function verification(data){
	//自定义脚本内容
	//可返回true/false单纯校验键入的data正确性
	//可返回文本，实时替换,比如当前时间等
	//return "2099-01-01 00:00:00";
	//设置日期，当前日期的前七天
	data = data.sampleItem;
    if(data.length == 10){
		return getDay(-7);
	}
	return data;
}

function getDay(day){
　　var today = new Date();
　　var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;
　　today.setTime(targetday_milliseconds); //注意，这行是关键代码
　　var tYear = today.getFullYear();
　　var tMonth = today.getMonth();
　　var tDate = today.getDate();
　　tMonth = doHandleMonth(tMonth + 1);
　　tDate = doHandleMonth(tDate);
　　return tYear+"-"+tMonth+"-"+tDate+" 00:00:00";
}

function doHandleMonth(month){
　　var m = month;
　　if(month.toString().length == 1){
　　　　m = "0" + month;
　　}
　　return m;
}', null, 1, 0, 'admin', '2023-05-12 09:43:42', 'admin', '2023-05-12 09:43:42', 1);
INSERT INTO `report`.`report_data_set_param` (set_code, param_name, param_desc, param_type, sample_item, required_flag, validation_rules, order_num, enable_flag, delete_flag, create_by, create_time, update_by, update_time, version) VALUES ('report_city_total', 'endTime', '', '', '2023-05-13', 1, 'function verification(data){
	//自定义脚本内容
	//可返回true/false单纯校验键入的data正确性
	//可返回文本，实时替换,比如当前时间等
	//return "2099-01-01 00:00:00";
	//设置日期，当前日期的前七天
	data = data.sampleItem;
    if(data.length == 10){
		return getDay(1);
	}
	return data;
}

function getDay(day){
　　var today = new Date();
　　var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;
　　today.setTime(targetday_milliseconds); //注意，这行是关键代码
　　var tYear = today.getFullYear();
　　var tMonth = today.getMonth();
　　var tDate = today.getDate();
　　tMonth = doHandleMonth(tMonth + 1);
　　tDate = doHandleMonth(tDate);
　　return tYear+"-"+tMonth+"-"+tDate+" 00:00:00";
}

function doHandleMonth(month){
　　var m = month;
　　if(month.toString().length == 1){
　　　　m = "0" + month;
　　}
　　return m;
}', null, 1, 0, 'admin', '2023-05-12 09:43:42', 'admin', '2023-05-12 09:43:42', 1);


INSERT INTO `report`.`report_data_source`(`source_code`, `source_name`, `source_desc`, `source_type`, `source_config`, `enable_flag`, `delete_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`) VALUES ('mysql_report', 'mysql数据源', '', 'mysql', '{\"driverName\":\"com.mysql.cj.jdbc.Driver\",\"jdbcUrl\":\"jdbc:mysql://127.0.0.1:3306/report?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8\",\"username\":\"root\",\"password\":\"wtf0010.\"}', 1, 0, 'admin', '2021-06-30 09:48:46', 'admin', '2021-06-30 09:48:46', 1);




-- ----------------------------
-- Records of aj_report_city
-- ----------------------------
INSERT INTO `report`.`report_city` VALUES (1, 'beijing', '北京', 25, '2023-05-05 02:00:00');
INSERT INTO `report`.`report_city` VALUES (2, 'tianjin', '天津', 15, '2023-05-05 02:00:00');
INSERT INTO `report`.`report_city` VALUES (3, 'shanghai', '上海', 20, '2023-05-05 02:00:00');
INSERT INTO `report`.`report_city` VALUES (4, 'hangzhou', '杭州', 9, '2023-05-05 02:00:00');
INSERT INTO `report`.`report_city` VALUES (5, 'guangzhou', '广州', 13, '2023-05-05 02:00:00');
INSERT INTO `report`.`report_city` VALUES (6, 'chongqing', '重庆', 16, '2023-05-05 02:00:00');
INSERT INTO `report`.`report_city` VALUES (7, 'chengdu', '成都', 19, '2023-05-05 02:00:00');
INSERT INTO `report`.`report_city` VALUES (8, 'beijing', '北京', 35, '2023-05-06 02:00:00');
INSERT INTO `report`.`report_city` VALUES (9, 'tianjin', '天津', 27, '2023-05-06 02:00:00');
INSERT INTO `report`.`report_city` VALUES (10, 'shanghai', '上海', 36, '2023-05-06 02:00:00');
INSERT INTO `report`.`report_city` VALUES (11, 'hangzhou', '杭州', 29, '2023-05-06 02:00:00');
INSERT INTO `report`.`report_city` VALUES (12, 'guangzhou', '广州', 23, '2023-05-06 02:00:00');
INSERT INTO `report`.`report_city` VALUES (13, 'chongqing', '重庆', 21, '2023-05-06 02:00:00');
INSERT INTO `report`.`report_city` VALUES (14, 'chengdu', '成都', 24, '2023-05-06 02:00:00');
INSERT INTO `report`.`report_city` VALUES (15, 'beijing', '北京', 45, '2023-05-07 02:00:00');
INSERT INTO `report`.`report_city` VALUES (16, 'tianjin', '天津', 21, '2023-05-07 02:00:00');
INSERT INTO `report`.`report_city` VALUES (17, 'shanghai', '上海', 23, '2023-05-07 02:00:00');
INSERT INTO `report`.`report_city` VALUES (18, 'hangzhou', '杭州', 21, '2023-05-07 02:00:00');
INSERT INTO `report`.`report_city` VALUES (19, 'guangzhou', '广州', 19, '2023-05-07 02:00:00');
INSERT INTO `report`.`report_city` VALUES (20, 'chongqing', '重庆', 17, '2023-05-07 02:00:00');
INSERT INTO `report`.`report_city` VALUES (21, 'chengdu', '成都', 14, '2023-05-07 02:00:00');
INSERT INTO `report`.`report_city` VALUES (22, 'beijing', '北京', 12, '2023-05-08 02:00:00');
INSERT INTO `report`.`report_city` VALUES (23, 'tianjin', '天津', 24, '2023-05-08 02:00:00');
INSERT INTO `report`.`report_city` VALUES (24, 'shanghai', '上海', 13, '2023-05-08 02:00:00');
INSERT INTO `report`.`report_city` VALUES (25, 'hangzhou', '杭州', 31, '2023-05-08 02:00:00');
INSERT INTO `report`.`report_city` VALUES (26, 'guangzhou', '广州', 29, '2023-05-08 02:00:00');
INSERT INTO `report`.`report_city` VALUES (27, 'chongqing', '重庆', 37, '2023-05-08 02:00:00');
INSERT INTO `report`.`report_city` VALUES (28, 'chengdu', '成都', 24, '2023-05-08 02:00:00');
INSERT INTO `report`.`report_city` VALUES (29, 'beijing', '北京', 44, '2023-05-09 02:00:00');
INSERT INTO `report`.`report_city` VALUES (30, 'tianjin', '天津', 32, '2023-05-09 02:00:00');
INSERT INTO `report`.`report_city` VALUES (31, 'shanghai', '上海', 33, '2023-05-09 02:00:00');
INSERT INTO `report`.`report_city` VALUES (32, 'hangzhou', '杭州', 29, '2023-05-09 02:00:00');
INSERT INTO `report`.`report_city` VALUES (33, 'guangzhou', '广州', 19, '2023-05-09 02:00:00');
INSERT INTO `report`.`report_city` VALUES (34, 'chongqing', '重庆', 17, '2023-05-09 02:00:00');
INSERT INTO `report`.`report_city` VALUES (35, 'chengdu', '成都', 34, '2023-05-09 02:00:00');
INSERT INTO `report`.`report_city` VALUES (36, 'beijing', '北京', 27, '2023-05-10 02:00:00');
INSERT INTO `report`.`report_city` VALUES (37, 'tianjin', '天津', 31, '2023-05-10 02:00:00');
INSERT INTO `report`.`report_city` VALUES (38, 'shanghai', '上海', 13, '2023-05-10 02:00:00');
INSERT INTO `report`.`report_city` VALUES (39, 'hangzhou', '杭州', 21, '2023-05-10 02:00:00');
INSERT INTO `report`.`report_city` VALUES (40, 'guangzhou', '广州', 31, '2023-05-10 02:00:00');
INSERT INTO `report`.`report_city` VALUES (41, 'chongqing', '重庆', 27, '2023-05-10 02:00:00');
INSERT INTO `report`.`report_city` VALUES (42, 'chengdu', '成都', 10, '2023-05-10 02:00:00');