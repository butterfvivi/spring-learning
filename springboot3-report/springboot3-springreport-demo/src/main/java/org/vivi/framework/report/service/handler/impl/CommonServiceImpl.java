package org.vivi.framework.report.service.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.service.common.enums.RequestTypeEnum;
import org.vivi.framework.report.service.common.utils.*;
import org.vivi.framework.report.service.handler.ICommonService;
import org.vivi.framework.report.service.model.common.ApiRequestDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**  
 * @ClassName: CommonServiceImpl
 * @Description: 共通服务实现
*/
@Service
public class CommonServiceImpl implements ICommonService {

	/**
     * 本地保存路径
     */
	@Value("${file.path}")
    private String dirPath;
	
	/**
	 * @Title: upload
	 * @Description: 上传文件
	 */
	@Override
	public Object upload(MultipartFile file) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		InputStream inputStream2 = file.getInputStream();
		//文件后缀
		String fileExt = cn.hutool.core.io.FileUtil.extName(file.getOriginalFilename());
		String filename = IdWorker.getIdStr()+"."+fileExt;
		String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
		File dest = new File(dirPath + date + "/" + filename);
		FileUtil.createFile(dest);
		file.transferTo(dest);
		 //拼接上传文件路径
        String fileUri = MessageUtil.getValue("file.url.prefix")+date+"/"+filename+"?t="+System.currentTimeMillis();
        result.put("fileUri", fileUri);
		BufferedImage image = ImageIO.read(inputStream2);
		int width = image.getWidth();
		int height = image.getHeight();
        result.put("width", width);
        result.put("height", height);
		return result;
	}
	
	/**  
	 * @MethodName: upload
	 * @Description: 字节流上传图片
	 */
	@Override
	public Map<String, String> upload(byte[] bytes, String fileName) {
		Map<String, String> result = new HashMap<String, String>();
		String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
		File dest = new File(dirPath + date + "/" + fileName);
		FileUtil.createFile(dest);
		cn.hutool.core.io.FileUtil.writeBytes(bytes, dest);
		 //拼接上传文件路径
        String fileUri = MessageUtil.getValue("file.url.prefix")+date+"/"+fileName+"?t="+System.currentTimeMillis();
        result.put("fileUri", fileUri);
		return result;
	}
	
	
	/**  
	 * @Title: upload
	 * @Description: 上传文件
	 */
	@Override
	public Object uploadFile(MultipartFile file) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		//文件后缀
		String fileExt = cn.hutool.core.io.FileUtil.extName(file.getOriginalFilename());
		String filename = IdWorker.getIdStr()+"."+fileExt;
		String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
		File dest = new File(dirPath + date + "/" + filename);
		FileUtil.createFile(dest);
		file.transferTo(dest);
		 //拼接上传文件路径
        String fileUri = MessageUtil.getValue("file.url.prefix")+date+"/"+filename+"?t="+System.currentTimeMillis();
        result.put("fileUri", fileUri);
        result.put("fileName", file.getOriginalFilename());
		return result;
	}
	
	@Override
	public Object apiTest(ApiRequestDto apiRequestDto) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(!ListUtil.isEmpty(apiRequestDto.getParams()))
		{
			for (int i = 0; i < apiRequestDto.getParams().size(); i++) {
				if(apiRequestDto.getParams().get(i).get("paramCode") != null && StringUtil.isNotEmpty(String.valueOf(apiRequestDto.getParams().get(i).get("paramCode"))))
				{
					if(apiRequestDto.getParams().get(i).get("defaultValue") != null && StringUtil.isNotEmpty(String.valueOf(apiRequestDto.getParams().get(i).get("defaultValue"))))
					{
						params.put(String.valueOf(apiRequestDto.getParams().get(i).get("paramCode")), String.valueOf(apiRequestDto.getParams().get(i).get("defaultValue")));
					}
					
				}
			}
		}
		String result = "";
		if(RequestTypeEnum.POST.getCode().equals(apiRequestDto.getRequestType().toLowerCase()))
		{//post请求
			result = HttpClientUtil.doPostJson(apiRequestDto.getUrl(),JSONObject.toJSONString(params));
		}else {
			result = HttpClientUtil.doGet(apiRequestDto.getUrl(), params);
		}
		return result;
	}

	/**  
	 * @MethodName: parseXlsxByUrl
	 * @Description: 通过url解析xlsx文件
	 */
	@Override
	public JSONArray parseXlsxByUrl(JSONObject model) throws Exception {
		JSONArray result = null;
		String url = model.getString("url");
		InputStream input = new URL(url).openStream();
		String fileType = model.getString("fileType");
		if("xlsx".equals(fileType)) {
			result =  DocumentToLuckysheetUtil.xlsx2Luckysheet(input);
		}else if("csv".equals(fileType)) {
			result =  DocumentToLuckysheetUtil.csv2Luckysheet(input,model.getString("fileName"));
		}
		return result;
	}
}
