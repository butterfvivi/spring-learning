package org.vivi.framework.lucky.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.DbImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vivi.framework.lucky.demo.socket.SheetWebSocketServer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class SheetConfig {

	@Value("${ws.port:8082}")
	int port;

	@Value("${def}")
	String defExcel;

	// 启动ws服务
	@Bean
	public SheetWebSocketServer getWebSocketServer() throws Exception {
		SheetWebSocketServer server = new SheetWebSocketServer(port);
		server.start();
		log.info("LuckySheetServer started on port: ws://127.0.0.1:{}", port);
		return server;
	}

	// 为了简化部署,这里使用嵌入式LeveDB-kv数据库
	@Bean
	public DB getLeveDB() throws IOException {
		Options options = new Options();
		options.createIfMissing(true);
		DbImpl db = new DbImpl(options, new File("./data"));
		// 初始化文档
		if (db.get(KeysConfig.FILE) == null) {
			init(db);
		}
		// 每天0点重置Excel文件
		/*Executors
		.newSingleThreadScheduledExecutor()
		.scheduleAtFixedRate(
				() -> init(db), 
				24 - LocalTime.now().getHour(),
				24, 
				TimeUnit.HOURS);*/
		return db;
	}

	private void init(DbImpl db) {
		db.put(KeysConfig.FILE, defExcel.getBytes(StandardCharsets.UTF_8));
		log.info("excel init");
	}
}
