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
package org.vivi.framework.ureport.demo.console.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.CacheObj;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.vivi.framework.ureport.demo.console.RequestHolder;
import org.vivi.framework.ureport.demo.core.cache.ReportCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class HttpSessionReportCache implements ReportCache {
    private Cache<String, ObjectMap> sessionReportMap = CacheUtil.newLRUCache(10);

    @Override
    public Object getObject(String file) {
        HttpServletRequest req = RequestHolder.getRequest();
        if (req == null) {
            return null;
        }
        ObjectMap objMap = getObjectMap(req);
        return objMap.get(file);
    }

    @Override
    public void storeObject(String file, Object object) {
        HttpServletRequest req = RequestHolder.getRequest();
        if (req == null) {
            return;
        }
        ObjectMap map = getObjectMap(req);
        map.put(file, object);
    }

    @Override
    public boolean disabled() {
        return !CacheProperties.isEnableRedis();
    }

    private ObjectMap getObjectMap(HttpServletRequest req) {
        List<String> expiredList = new ArrayList<String>();
        Iterator<CacheObj<String, ObjectMap>> iterator = sessionReportMap.cacheObjIterator();
        while (iterator.hasNext()) {
            CacheObj<String, ObjectMap> next = iterator.next();
            String key = next.getKey();
            ObjectMap reportObj = next.getValue();
            if (reportObj.isExpired()) {
                expiredList.add(key);
            }
        }
        for (String key : expiredList) {
            sessionReportMap.remove(key);
        }
        String sessionId = req.getSession().getId();
        ObjectMap obj = sessionReportMap.get(sessionId);
        if (obj != null) {
            return obj;
        } else {
            ObjectMap objMap = new ObjectMap();
            sessionReportMap.put(sessionId, objMap);
            return objMap;
        }
    }
}
