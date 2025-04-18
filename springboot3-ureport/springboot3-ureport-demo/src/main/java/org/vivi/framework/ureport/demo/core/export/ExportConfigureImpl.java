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
package org.vivi.framework.ureport.demo.core.export;

import java.io.OutputStream;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2017年3月20日
 */
public class ExportConfigureImpl implements ExportConfigure {
    private String file;
    private OutputStream outputStream;
    private Map<String, Object> parameters;

    public ExportConfigureImpl(String file, Map<String, Object> parameters, OutputStream outputStream) {
        this.file = file;
        this.parameters = parameters;
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getFile() {
        return file;
    }
}
