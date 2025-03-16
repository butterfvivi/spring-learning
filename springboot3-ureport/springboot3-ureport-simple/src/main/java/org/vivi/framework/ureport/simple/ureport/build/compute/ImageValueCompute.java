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
package org.vivi.framework.ureport.simple.ureport.build.compute;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.ureport.build.BindData;
import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.definition.value.ImageValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.Source;
import org.vivi.framework.ureport.simple.ureport.definition.value.ValueType;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Image;
import org.vivi.framework.ureport.simple.ureport.utils.ImageUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月24日
 */
public class ImageValueCompute implements ValueCompute {
	
	@Override
	public List<BindData> compute(Cell cell, Context context) {
		ImageValue value = (ImageValue) cell.getValue();
		int width = value.getWidth();
		int height = value.getHeight();
		Source source = value.getSource();
		List<BindData> list = new ArrayList<BindData>();
		if (source.equals(Source.text)) {
			String base64Data = ImageUtils.getImageBase64Data(value.getValue(), width, height);
			list.add(new BindData(new Image(base64Data, value.getValue(), -1, -1)));
		} else if(source.equals(Source.upload)) {
			String base64Data = ImageUtils.getResizedBase64(value.getValue().substring(value.getValue().indexOf(",") + 1, value.getValue().length()), width, height);
			list.add(new BindData(new Image(base64Data, -1, -1)));
		} else {
			Expression expression = value.getExpression();
			ExpressionData<?> data = expression.execute(cell, cell, context);
			Object obj = data.getData();
			if (obj instanceof List) {
				List<?> listData = (List<?>) obj;
				for (Object o : listData) {
					if (o == null) {
						continue;
					}
					String path = null;
					if (o instanceof BindData) {
						BindData bindData = (BindData) o;
						Object valueData = bindData.getValue();
						if (valueData != null) {
							path = valueData.toString();
						}
					} else {
						path = o.toString();
					}
					if (StringUtils.isBlank(path)) {
						continue;
					}
					String base64Data = ImageUtils.getImageBase64Data(path, width, height);
					list.add(new BindData(new Image(base64Data, path, -1, -1)));
				}
			} else if (obj instanceof BindData) {
				BindData bindData = (BindData) obj;
				String path = null;
				Object valueData = bindData.getValue();
				if (valueData != null) {
					path = valueData.toString();
				}
				if (StringUtils.isNotBlank(path)) {
					String base64Data = ImageUtils.getImageBase64Data(path, width, height);
					list.add(new BindData(new Image(base64Data, path, -1, -1)));
				}
			} else if (obj instanceof String) {
				String text = obj.toString();
				if (text.startsWith("\"") && text.endsWith("\"")) {
					text = text.substring(1, text.length() - 1);
				}
				String base64Data = ImageUtils.getImageBase64Data(text, width, height);
				list.add(new BindData(new Image(base64Data, text, -1, -1)));
			} else {
				if (obj != null && StringUtils.isNotBlank(obj.toString())) {
					String base64Data = ImageUtils.getImageBase64Data(obj.toString(), width, height);
					list.add(new BindData(new Image(base64Data, obj.toString(), -1, -1)));
				}
			}
		}
		return list;
	}

	@Override
	public ValueType type() {
		return ValueType.image;
	}
}
