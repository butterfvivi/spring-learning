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
package org.vivi.framework.ureport.demo.core.cache;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import org.vivi.framework.ureport.demo.console.cache.CacheProperties;
import org.vivi.framework.ureport.demo.core.definition.ReportDefinition;
import org.springframework.stereotype.Component;


@Component
public class DefaultMemoryReportDefinitionCache implements ReportDefinitionCache {
    private Cache<String, ReportDefinition> reportMap = CacheUtil.newLRUCache(20);

    @Override
    public ReportDefinition getReportDefinition(String file) {
        return reportMap.get(appendKey(file));
    }

    @Override
    public void cacheReportDefinition(String file, ReportDefinition reportDefinition) {
        String key = appendKey(file);
        if (reportMap.containsKey(key)) {
            reportMap.remove(key);
        }
        reportMap.put(key, reportDefinition);
    }

    @Override
    public void removeReportDefinition(String file) {
        reportMap.remove(appendKey(file));
    }

    @Override
    public boolean disabled() {
        return !CacheProperties.isEnableRedis();
    }

    private String appendKey(String file) {
        if ("p".equals(file)) {
            return sessionId() + file;
        } else {
            return file;
        }
    }
}
