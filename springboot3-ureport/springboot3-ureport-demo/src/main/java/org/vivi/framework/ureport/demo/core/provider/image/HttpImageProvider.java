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
package org.vivi.framework.ureport.demo.core.provider.image;

import org.vivi.framework.ureport.demo.core.exception.ReportException;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jacky.gao
 * @since 2017年12月11日
 */
@Component
public class HttpImageProvider implements ImageProvider {

    @Override
    public InputStream getImage(String path) {
        try {
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return inputStream;
        } catch (Exception ex) {
            throw new ReportException(ex);
        }
    }

    @Override
    public boolean support(String path) {
        return path.startsWith("http:");
    }

}
