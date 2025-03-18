package org.vivi.framework.lucky.demo.utils;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

	/**
	 * 获取url参数
	 * @param path
	 * @param key
	 * @return
	 */
	public static String getParameter(String path, String key) {
		char[] data = path.toCharArray();
		char[] keyArr = key.toCharArray();
		for (int i = 0; i < data.length; i++) {
			if (data[i] == '?') {
				for (int j = i + 1; j < data.length; j++) {
					if (data[j] == keyArr[0]) {

						boolean flag = true;
						for (int k = 1; k < keyArr.length; k++) {
							if (keyArr[k] != data[j + k]) {
								flag = false;
								break;
							}
						}

						if (flag) {
							int start = j + keyArr.length + 1;
							int end = start;
							while (end < data.length) {
								if (data[end] == '&') {
									break;
								}

								end++;
							}

							return new String(data, start, end - start);
						}
					}
				}
			}
		}

		return null;
	}

	public static String post(String url, Map<String, String> params, String charset)
			throws IOException {
		String responseEntity = "";

		// 创建CloseableHttpClient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);
		// 生成请求参数
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}

		// 将参数添加到post请求中
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
		// 发送请求，获取结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		// 获取响应实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			responseEntity = EntityUtils.toString(entity, charset);
		}

		// 释放资源
		EntityUtils.consume(entity);
		response.close();

		return responseEntity;
	}
}
