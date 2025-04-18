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
package org.vivi.framework.ureport.store.core.cache;

import org.vivi.framework.ureport.store.console.exception.ReportDesignException;
import org.vivi.framework.ureport.store.core.definition.ReportDefinition;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Jacky.gao
 * @since 2016年12月4日
 */
public interface ReportDefinitionCache {
	ReportDefinition getReportDefinition(String file);

	void cacheReportDefinition(String file, ReportDefinition reportDefinition);

	void removeReportDefinition(String file);

	/**
	 * 是否开启
	 *
	 * @return
	 */
	boolean disabled();

	/**
	 * 获取sessionId
	 *
	 * @return
	 */
	default String sessionId() {
		try {
			String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
			return sessionId;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReportDesignException("sessionId获取失败");
		}
	}
}
