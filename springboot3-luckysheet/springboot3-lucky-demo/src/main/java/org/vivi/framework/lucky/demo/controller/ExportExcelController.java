
/** 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.vivi.framework.lucky.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.vivi.framework.lucky.demo.utils.ExcelUtils;


@Controller
public class ExportExcelController {

	@GetMapping("/export")
	public String exportExcel() {
		return "export";
	}

	@PostMapping("/excel/exportFile")
	public void downExcelFile(@RequestParam(value = "exceldata") String exceldata, HttpServletRequest request,
			HttpServletResponse response) {
		// 去除luckysheet中 &#xA的换行
		exceldata = exceldata.replace("&#xA;", "\\r\\n");
		ExcelUtils.exportLuckySheetXlsx(exceldata, request, response);
	}
}
