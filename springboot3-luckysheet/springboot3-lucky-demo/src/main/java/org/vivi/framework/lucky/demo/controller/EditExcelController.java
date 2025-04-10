package org.vivi.framework.lucky.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.lucky.demo.config.KeysConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
@CrossOrigin
@RestController
public class EditExcelController {

	@Autowired
	DB db;

	@GetMapping("/version")
	public Object version() {
		return new Object() {
			public String version = "v0.0.3";
		};
	}

	// 取文件
	@RequestMapping("/get")
	public Object get() throws IOException {
		return new String(db.get(KeysConfig.FILE), StandardCharsets.UTF_8);
	}

	// 设置文件
	@PostMapping("/set")
	public Object set(String jsonExcel) throws IOException {
		db.put(KeysConfig.FILE, jsonExcel.getBytes(StandardCharsets.UTF_8));
		return true;
	}
}
