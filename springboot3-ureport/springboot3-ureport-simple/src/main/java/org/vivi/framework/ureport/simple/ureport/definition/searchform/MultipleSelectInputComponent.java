package org.vivi.framework.ureport.simple.ureport.definition.searchform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vivi.framework.ureport.simple.common.utils.StringUtils;
import org.vivi.framework.ureport.simple.ureport.build.Dataset;
import org.vivi.framework.ureport.simple.ureport.export.html.InputComponentData;
import org.vivi.framework.ureport.simple.ureport.export.html.MultipleSelectInputComponentData;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

public class MultipleSelectInputComponent extends SelectInputComponent {

	@Override
	@SuppressWarnings("unchecked")
	public InputComponentData buildComponent(RenderContext context) {
		MultipleSelectInputComponentData data = new MultipleSelectInputComponentData();
		super.setInputComponentData(data);
		
		String bindParameter = getBindParameter();
		List<Object> value = (List<Object>) context.getParameter(bindParameter);
		if(value == null) {
			value = Collections.emptyList();
		}
		data.setValue(value);
		
		String constant = super.getConstant();
		if (StringUtils.isNotBlank(constant) && "1".equals(constant)) {
			String datasetName = getDataset();
			Dataset dataset = context.getDataset(datasetName);
			List<Option> options = new ArrayList<Option>();
			for (Object obj : dataset.getData()) {
				Object optionLabel = ToolUtils.getProperty(obj, super.getLabelField());
				Object optionValue = ToolUtils.getProperty(obj, super.getValueField());
				Option option = new Option();
				option.setLabel(String.valueOf(optionLabel));
				option.setValue(String.valueOf(optionValue));
				options.add(option);
			}
			data.setOptions(options);
		} else {
			data.setOptions(getOptions());
		}
		return data;
	}
}
