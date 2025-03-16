package org.vivi.framework.ureport.simple.ureport.chart.dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.definition.mapping.MappingItem;
import org.vivi.framework.ureport.simple.ureport.definition.mapping.MappingType;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 字典映射
 * @author Administrator
 *
 */
public class Dict {
	
	private MappingType mappingType = MappingType.simple;
	
	private String mappingDataset;
	
	private String mappingKeyProperty;
	
	private String mappingValueProperty;
	
	private List<MappingItem> mappingItems;

	@JsonIgnore
	private Map<String, String> data = new HashMap<String, String>();
	
	@JsonIgnore
	private boolean dict = false;
	
	public MappingType getMappingType() {
		return mappingType;
	}

	public void setMappingType(MappingType mappingType) {
		this.mappingType = mappingType;
	}

	public String getMappingDataset() {
		return mappingDataset;
	}

	public void setMappingDataset(String mappingDataset) {
		this.mappingDataset = mappingDataset;
	}

	public String getMappingKeyProperty() {
		return mappingKeyProperty;
	}

	public void setMappingKeyProperty(String mappingKeyProperty) {
		this.mappingKeyProperty = mappingKeyProperty;
	}

	public String getMappingValueProperty() {
		return mappingValueProperty;
	}

	public void setMappingValueProperty(String mappingValueProperty) {
		this.mappingValueProperty = mappingValueProperty;
	}

	public List<MappingItem> getMappingItems() {
		return mappingItems;
	}

	public void setMappingItems(List<MappingItem> mappingItems) {
		this.mappingItems = mappingItems;
	}
	
	public void build(Context context) {
		if (MappingType.simple == mappingType) {
			if (mappingItems != null && mappingItems.size() > 0) {
				for (MappingItem mappingItem : mappingItems) {
					data.put(mappingItem.getValue(), mappingItem.getLabel());
				}
				dict = true;
			}
		} else {
			if (StringUtils.isNotBlank(mappingDataset)) {
				List<?> dataList = context.getDatasetData(mappingDataset);
				if (StringUtils.isNotBlank(mappingKeyProperty) && StringUtils.isNotBlank(mappingValueProperty)) {
					dict = true;
					for (Object obj : dataList) {
						String key = String.valueOf(ToolUtils.getProperty(obj, mappingKeyProperty));
						String value = String.valueOf(ToolUtils.getProperty(obj, mappingValueProperty));
						data.put(key, value);
					}
				}
			}
		}
	}
	
	public String getValue(String key) {
		return data.get(key);
	}

	public boolean isDict() {
		return dict;
	}
}
