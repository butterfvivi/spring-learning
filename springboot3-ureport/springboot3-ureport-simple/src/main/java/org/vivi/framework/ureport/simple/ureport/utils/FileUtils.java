package org.vivi.framework.ureport.simple.ureport.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.common.config.PropertiesConfig;
import org.vivi.framework.ureport.simple.ureport.exception.ReportException;

public class FileUtils {

	public static InputStream getReport(String file) {
		try {
			if(StringUtils.isBlank(file)) {
				URL url = ToolUtils.class.getClassLoader().getResource("templates/template.ureport.xml");
				InputStream inputStream = url.openStream();
				return inputStream;
			}
			String fullPath = PropertiesConfig.getReportFileDir() + "/" + file;
			return new FileInputStream(fullPath);
		} catch (Exception e) {
			throw new ReportException(e);
		}
	}
}
