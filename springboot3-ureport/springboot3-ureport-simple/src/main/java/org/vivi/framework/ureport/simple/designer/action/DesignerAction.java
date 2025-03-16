/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vivi.framework.ureport.simple.designer.action;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.vivi.framework.ureport.simple.common.config.PropertiesConfig;
import org.vivi.framework.ureport.simple.common.exception.ParamErrorException;
import org.vivi.framework.ureport.simple.common.exception.ReportDesignException;
import org.vivi.framework.ureport.simple.designer.bean.ReportDefinitionWrapper;
import org.vivi.framework.ureport.simple.designer.bean.ReportFile;
import org.vivi.framework.ureport.simple.designer.excel.ExcelParserUtils;
import org.vivi.framework.ureport.simple.ureport.definition.ReportDefinition;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserLexer;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.DatasetContext;
import org.vivi.framework.ureport.simple.ureport.exception.ReportException;
import org.vivi.framework.ureport.simple.ureport.export.ReportRender;
import org.vivi.framework.ureport.simple.ureport.expression.ErrorInfo;
import org.vivi.framework.ureport.simple.ureport.expression.ScriptErrorListener;
import org.vivi.framework.ureport.simple.ureport.utils.StringUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月25日
 */
@RestController
@RequestMapping("/designer")
public class DesignerAction {

	private ReportRender reportRender = new ReportRender();

	/**
	 * 表达式校验
	 * @param content 表达式内容
	 * @return
	 */
	@RequestMapping("/scriptValidation")
	public List<ErrorInfo> scriptValidation(String content) {
		content = StringUtils.decode(content);
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(content);
		ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ReportParserParser parser = new ReportParserParser(tokenStream);
		ScriptErrorListener errorListener = new ScriptErrorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);
		parser.expression();
		List<ErrorInfo> infos = errorListener.getInfos();
		return infos;
	}

	/**
	 * 解析表达式中的数据集
	 * @param expr
	 * @return
	 */
	@RequestMapping("/parseDataSet")
	public Map<String, String> parseDatasetName(String expr) {
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(expr);
		ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ReportParserParser parser = new ReportParserParser(tokenStream);
		parser.removeErrorListeners();
		
		Map<String, String> result = new HashMap<String, String>();
		DatasetContext ctx = parser.dataset();
		if (ctx != null) {
			TerminalNode node = ctx.Identifier();
			if(node != null) {
				String datasetName = ctx.Identifier().getText();
				result.put("datasetName", datasetName);
			}
		}
		return result;
	}

	/**
	 * 加载报表内容
	 * @param request
	 * @return
	 */
	@RequestMapping("/get")
	public ReportDefinitionWrapper loadReport(HttpServletRequest request)  {
		String file = request.getParameter("file");
		ReportDefinition reportDef = reportRender.getReportDefinition(file);
		return new ReportDefinitionWrapper(reportDef);
	}

	/**
	 * 删除报表模板
	 * @param request
	 * @return
	 */
	@RequestMapping("/remove")
	public int deleteReportFile(HttpServletRequest request) {
		String file = request.getParameter("file");
		if (file == null) {
			throw new ReportDesignException("Report file can not be null.");
		}
		String fullPath = PropertiesConfig.getReportFileDir() + "/" + file;
		File f = new File(fullPath);
		if (f.exists()) {
			f.delete();
		}
		return 1;
	}

	/**
	 * 保存报表模板
	 * @param reportFile
	 * @return
	 */
	@RequestMapping("/save")
	public int saveReportFile(@RequestBody ReportFile reportFile) {
		String fileName = reportFile.getName();
		String content = reportFile.getContent();
		content = StringUtils.decode(content);
		ReportDefinition reportDef = reportRender.getReportDefinition(content, "utf-8");
		if(reportDef != null) {
			String oldName = reportFile.getOldName();
			if (StringUtils.isBlank(oldName) || !fileName.equals(oldName)) {
				List<ReportFile> files = loadReportProviders();
				for (ReportFile f : files) {
					boolean isSameName = f.getName().equals(fileName);
					if(isSameName) {
						throw new ParamErrorException("名称已存在");
					}
				}
			}
			String fullPath = PropertiesConfig.getReportFileDir() + "/" + fileName;
			try (FileOutputStream outStream = new FileOutputStream(new File(fullPath))){
				IOUtils.write(content, outStream, "utf-8");
			} catch (Exception ex) {
				throw new ReportException(ex);
			}
		}
		return 1;
	}

	/**
	 * 查看所有报表模板
	 * @return
	 */
	@RequestMapping("/getList")
	public List<ReportFile> loadReportProviders() {
		File file = new File(PropertiesConfig.getReportFileDir());
		List<ReportFile> list = new ArrayList<ReportFile>();
		File[] files = file.listFiles();
		Calendar calendar = Calendar.getInstance();
		if(files != null && files.length > 0) {
			for (File f : file.listFiles()) {
				calendar.setTimeInMillis(f.lastModified());
				list.add(new ReportFile(f.getName(), calendar.getTime()));
			}
			Collections.sort(list, new Comparator<ReportFile>() {
				@Override
				public int compare(ReportFile f1, ReportFile f2) {
					return f2.getUpdateDate().compareTo(f1.getUpdateDate());
				}
			});
		}
		return list;
	}
	
	/**
	 * 导入excel模板
	 * @param file
	 * @return
	 */
	@RequestMapping("/import/excel")
	public ReportDefinitionWrapper importExcel(@RequestParam("file") MultipartFile file) {
		ReportDefinition report = ExcelParserUtils.parser(file);
		if (report != null) {
			report.setReportFullName("");
			return new ReportDefinitionWrapper(report);
		}
		throw new RuntimeException("未识别文件");
	}
}
