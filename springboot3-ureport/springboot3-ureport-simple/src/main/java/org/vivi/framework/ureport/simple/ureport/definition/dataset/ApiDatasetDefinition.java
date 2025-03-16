package org.vivi.framework.ureport.simple.ureport.definition.dataset;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.vivi.framework.ureport.simple.ureport.build.Dataset;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.DataType;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;

public class ApiDatasetDefinition implements DatasetDefinition {

	private static final long serialVersionUID = -7238059187866936269L;

	private String name;
	
	private String method;
	
	private List<Parameter> parameters;
	
	private List<Field> fields;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public Dataset buildDataset(String dataSourceName, String url, HttpHeaders headers,Map<String, Object> parameters) {
		if("GET".equals(method)) {
			return doGet(dataSourceName, url, headers, parameters);
		} 
		return doPost(dataSourceName, url, headers, parameters);
	}
	
	private Dataset doGet(String dataSourceName, String url, HttpHeaders headers,Map<String, Object> parameters) {
        Map<String,Object> params = buildParameters(parameters);
		HttpEntity<Object> entity = new HttpEntity<>(params, headers);
		String linkURL = url;
		if (linkURL.indexOf("?") == -1) {
			linkURL += "?dataSourceName=" + dataSourceName;
		} else {
			linkURL += "&dataSourceName=" + dataSourceName;
		}
		linkURL += "&dataSetName=" + name;
		String urlParameter = buildLinkParameters(params);
		if (StringUtils.isNotBlank(urlParameter)) {
			linkURL += "&" + urlParameter;
		}
		ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(linkURL, HttpMethod.GET, entity, ApiResult.class);
		ApiResult result = responseEntity.getBody();
		return new Dataset(name, result.getData());
	}
	
	private Dataset doPost(String dataSourceName, String url, HttpHeaders headers,Map<String, Object> parameters) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataSourceName", dataSourceName);
		params.put("dataSetName", name);
		params.put("parameters", buildParameters(parameters));
		HttpEntity<Object> entity = new HttpEntity<>(params, headers);
		ResponseEntity<ApiResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, ApiResult.class);
		ApiResult result = responseEntity.getBody();
		return new Dataset(name, result.getData());
	}
	
	private Map<String,Object> buildParameters(Map<String, Object> params) {
		Map<String,Object> map = new HashMap<String, Object>();
		for (Parameter param : parameters) {
			String name = param.getName();
			DataType datatype = param.getType();
			Object value = param.getDefaultValue();
			if (params != null && params.containsKey(name)) {
				value = params.get(name);
			}
			map.put(name, datatype.parse(value));
		}
		return map;
	}
	
	private String buildLinkParameters(Map<String,Object> params) {
		StringBuilder sb = new StringBuilder();
		if (params != null) {
			int i = 0;
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String name = entry.getKey();
				String value = String.valueOf(entry.getValue());
				try {
					value = URLEncoder.encode(value, "utf-8");
					value = URLEncoder.encode(value, "utf-8");
				} catch (UnsupportedEncodingException e) {
					throw new ReportComputeException(e);
				}
				if (i > 0) {
					sb.append("&");
				}
				sb.append(name + "=" + value);
				i++;
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Field> getFields() {
		return fields;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}
