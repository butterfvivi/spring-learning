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

import org.vivi.framework.ureport.demo.console.cache.CacheProperties;
import org.vivi.framework.ureport.demo.core.utils.SpringContextUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2017年3月17日
 */
public class ResourceCache {

    private static RedisTemplate redisTemplate;

    private static Map<String, Object> map = new HashMap<String, Object>();

    static {
        redisTemplate = SpringContextUtils.getBean(RedisTemplate.class);
    }

    public static void putObject(String key, Object obj) {
        if (CacheProperties.isEnableRedis()) {
            redisTemplate.opsForValue().set(key, obj);
            return;
        }

        if (map.containsKey(key)) {
            map.remove(key);
        }
        map.put(key, obj);
    }

    public static Object getObject(String key) {
        if (CacheProperties.isEnableRedis()) {
            return redisTemplate.opsForValue().get(key);
        }

        return map.get(key);
    }
}
