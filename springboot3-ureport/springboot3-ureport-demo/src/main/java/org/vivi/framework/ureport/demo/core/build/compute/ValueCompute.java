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
package org.vivi.framework.ureport.demo.core.build.compute;

import org.vivi.framework.ureport.demo.core.build.BindData;
import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.definition.value.ValueType;
import org.vivi.framework.ureport.demo.core.model.Cell;

import java.util.List;

/**
 * 单元格值计算接口
 *
 */
public interface ValueCompute {

    /**
     * 计算方法
     *
     * @param cell    单元格
     * @param context 上线文
     * @return {@link List}<{@link BindData}>
     */
    List<BindData> compute(Cell cell, Context context);

    /**
     * 值类型
     *
     * @return {@link ValueType}
     */
    ValueType type();
}
