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
package org.vivi.framework.ureport.store.core.provider.report;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.vivi.framework.ureport.store.console.conf.serialize.CustomDateSerialize;

import java.util.Date;

/**
 * @author Jacky.gao
 * @since 2017年2月11日
 */
public class ReportFile {
	private String name;

	@JsonSerialize(using= CustomDateSerialize.class)
	private Date updateDate;
	
	public ReportFile(String name, Date updateDate) {
		this.name = name;
		this.updateDate = updateDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
