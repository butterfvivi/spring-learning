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
package org.vivi.framework.ureport.store.core.export.excel.high;

import org.vivi.framework.ureport.store.core.export.excel.high.builder.ExcelBuilderDirect;
import org.vivi.framework.ureport.store.core.export.excel.high.builder.ExcelBuilderWithPaging;
import org.vivi.framework.ureport.store.core.model.Report;

import java.io.OutputStream;


/**
 * @author Jacky.gao
 * @since 2016年12月30日
 */
public class ExcelProducer {
	private ExcelBuilderWithPaging excelBuilderWithPaging=new ExcelBuilderWithPaging();
	private ExcelBuilderDirect excelBuilderDirect=new ExcelBuilderDirect();
	public void produceWithPaging(Report report, OutputStream outputStream) {
		doProduce(report, outputStream, true,false);
	}
	public void produce(Report report, OutputStream outputStream) {
		doProduce(report, outputStream, false,false);
	}
	public void produceWithSheet(Report report, OutputStream outputStream) {
		doProduce(report, outputStream, true,true);
	}
	
	private void doProduce(Report report, OutputStream outputStream,boolean withPaging,boolean withSheet) {
		if(withPaging){
			excelBuilderWithPaging.build(report, outputStream, withSheet);
		}else{
			excelBuilderDirect.build(report, outputStream);
		}
	}
}
