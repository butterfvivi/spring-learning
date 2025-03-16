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
package org.vivi.framework.ureport.simple.ureport.parser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.dataset.ApiDatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.BeanDatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.DatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.Field;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.Parameter;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.SqlDatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.ApiDatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.BuildinDatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.DataType;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.DatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.JdbcDatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.SpringBeanDatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

/**
 * @author Jacky.gao
 * @since 2016年12月30日
 */
public class DatasourceParser implements Parser<DatasourceDefinition> {
	
	public final static Parser<DatasourceDefinition> instance = new DatasourceParser();
	
	@Override
	public DatasourceDefinition parse(Element element) {
		String type = element.attributeValue("type");
		// JDBC数据源
		if (type.equals("jdbc")) {
			String name = element.attributeValue("name");
			String username = element.attributeValue("username");
			String password = element.attributeValue("password");
			String driver = element.attributeValue("driver");
			String url = element.attributeValue("url");
			JdbcDatasourceDefinition ds = new JdbcDatasourceDefinition();
			ds.setName(name);
			ds.setDriver(driver);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setDatasets(parseDatasets(element));
			return ds;
		} 
		// 内置数据源
		if(type.equals("buildin")) {
			BuildinDatasourceDefinition ds = new BuildinDatasourceDefinition();
			ds.setName(element.attributeValue("name"));
			ds.setDatasets(parseDatasets(element));
			return ds;
		} 
		// SpringBean数据源
		if(type.equals("spring")){
			SpringBeanDatasourceDefinition ds=new SpringBeanDatasourceDefinition();
			ds.setName(element.attributeValue("name"));
			ds.setBeanId(element.attributeValue("bean"));
			ds.setDatasets(parseDatasets(element));
			return ds;
		}
		// API数据源
		if(type.equals("api")) {
			ApiDatasourceDefinition ds = new ApiDatasourceDefinition();
			ds.setName(element.attributeValue("name"));
			ds.setUrl(element.attributeValue("url"));
			
			List<Map<String,String>> headers = new ArrayList<Map<String,String>>();
			List<DatasetDefinition> list = new ArrayList<DatasetDefinition>();
			for (Object obj : element.elements()) {
				Element ele = (Element) obj;
				if(ele.getName().equals("dataset")) {
					ApiDatasetDefinition dataset = new ApiDatasetDefinition();
					dataset.setName(ele.attributeValue("name"));
					dataset.setMethod(ele.attributeValue("method"));
					dataset.setFields(parseFields(ele));
					dataset.setParameters(parseParameters(ele));
					list.add(dataset);
				}  else if(ele.getName().equals("header")) {
					Map<String,String> header = new HashMap<String, String>();
					header.put("name", ele.attributeValue("name"));
					header.put("value", ele.attributeValue("value"));
					headers.add(header);
				}
			}
			ds.setHeaders(headers);
			ds.setDatasets(list);
			return ds;
		}
		return null;
	}

	private List<DatasetDefinition> parseDatasets(Element element) {
		List<DatasetDefinition> list = new ArrayList<DatasetDefinition>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			String type = ele.attributeValue("type");
			if (type.equals("sql")) {
				SqlDatasetDefinition dataset = new SqlDatasetDefinition();
				dataset.setName(ele.attributeValue("name").trim());
				dataset.setSql(toXmlString(ele));
				dataset.setFields(parseFields(ele));
				dataset.setParameters(parseParameters(ele));
				list.add(dataset);
			} else if (type.equals("bean")) {
				BeanDatasetDefinition dataset = new BeanDatasetDefinition();
				dataset.setName(ele.attributeValue("name"));
				dataset.setMethod(ele.attributeValue("method"));
				dataset.setFields(parseFields(ele));
				dataset.setClazz(ele.attributeValue("clazz"));
				list.add(dataset);
			}
		}
		return list;
	}

	private List<Parameter> parseParameters(Element element) {
		List<Parameter> parameters = new ArrayList<Parameter>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (!ele.getName().equals("parameter")) {
				continue;
			}
			Parameter param = new Parameter();
			param.setName(ele.attributeValue("name"));
			param.setDefaultValue(ele.attributeValue("default-value"));
			param.setType(DataType.valueOf(ele.attributeValue("type")));
			parameters.add(param);
		}
		return parameters;
	}

	private List<Field> parseFields(Element element) {
		List<Field> fields = new ArrayList<Field>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (!ele.getName().equals("field")) {
				continue;
			}
			Field field = new Field(ele.attributeValue("name"));
			fields.add(field);
		}
		return fields;
	}
	
	private String toXmlString(Element element) {
		Element el = element.element("sql");
		String xml = el.asXML().replaceFirst("<sql>", "");
		xml = xml.substring(0, xml.lastIndexOf("</sql>"));
        return xml;
    }

	@Override
	public boolean support(String name) {
		return name.equals("datasource");
	}
}
