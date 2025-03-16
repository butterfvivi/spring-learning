package org.vivi.framework.ureport.simple.common.config;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConfig {

	private static final String fileStoreDir = MessageFormat.format("{0}/resource/reportlets", System.getProperty("user.dir"));
	
	private static final Map<String, Object> properties = new HashMap<String, Object>();
	
	@Value("${ureport.token.secret-key}")
	private String secretKey;
	
	@Value("${ureport.token.expires}")
	private Integer expires;
	
	@Value("${ureport.api.header.token.key}")
	private String tokenKey;
	
	@Value("${ureport.report.file.dir:}")
	private String filePath;
	
	@PostConstruct
	private void init() {
		if(secretKey == null) {
			secretKey = "6fa87140-3a53-11ed-ad8e-89304f903851";
		}
		properties.put("secretKey", secretKey);
		if(expires == null) {
			expires = 60 * 60 * 24;
		}
		properties.put("expires", expires);
		properties.put("tokenKey", tokenKey);
		properties.put("filePath", filePath);
	}
	
	public static int getExpires() {
		return Integer.parseInt(String.valueOf(properties.get("expires")));
	}
	
	public static String getTokenKey() {
		return String.valueOf(properties.get("tokenKey"));
	}
	
	public static String getSecretKey() {
		return String.valueOf(properties.get("secretKey"));
	}
	
	public static String getReportFileDir() {
		String dir = String.valueOf(properties.get("filePath"));
		if(StringUtils.isBlank(dir)) {
			return fileStoreDir;
		}
		return String.valueOf(properties.get("filePath"));
	}
}
