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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import org.vivi.framework.ureport.simple.ureport.utils.QRCodeWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import org.vivi.framework.ureport.simple.ureport.build.BindData;
import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.definition.value.Source;
import org.vivi.framework.ureport.simple.ureport.definition.value.ValueType;
import org.vivi.framework.ureport.simple.ureport.definition.value.ZxingValue;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Image;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author Jacky.gao
 * @since 2017年3月27日
 */
public class ZxingValueCompute implements ValueCompute {
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;
	@Override
	public List<BindData> compute(Cell cell, Context context) {
		List<BindData> list=new ArrayList<BindData>();
		ZxingValue value=(ZxingValue)cell.getValue();
		String format=value.getFormat();
		BarcodeFormat barcodeForamt=BarcodeFormat.QR_CODE;
		if(StringUtils.isNotBlank(format)){
			barcodeForamt=BarcodeFormat.valueOf(format);
		}
		int w=value.getWidth();
		int h=value.getHeight();
		Source source=value.getSource();
		if(source.equals(Source.text)){
			String data=value.getValue();
			Image image=buildImage(barcodeForamt,data,w,h);
			list.add(new BindData(image));
		}else{
			Expression expression=value.getExpression();
			ExpressionData<?> data=expression.execute(cell,cell, context);
			if(data instanceof BindDataListExpressionData){
				BindDataListExpressionData listData=(BindDataListExpressionData)data;
				List<BindData> bindDataList=listData.getData();
				for(BindData bindData:bindDataList){
					Object obj=bindData.getValue();
					if(obj==null)obj="";
					Image image=buildImage(barcodeForamt,obj.toString(),w,h);
					list.add(new BindData(image));
				}
			}else if(data instanceof ObjectExpressionData){
				ObjectExpressionData exprData=(ObjectExpressionData)data;
				Object obj=exprData.getData();
				if(obj==null){
					obj="";
				}else if(obj instanceof String){
					String text=obj.toString();
					if(text.startsWith("\"") && text.endsWith("\"")){
						text=text.substring(1,text.length()-1);
					}
					obj=text;
				}
				Image image=buildImage(barcodeForamt,obj.toString(),w,h);
				list.add(new BindData(image));
			}else if(data instanceof ObjectListExpressionData){
				ObjectListExpressionData listExprData=(ObjectListExpressionData)data;
				List<?> listData=listExprData.getData();
				for(Object obj:listData){
					if(obj==null){
						obj="";
					}else if(obj instanceof String){
						String text=obj.toString();
						if(text.startsWith("\"") && text.endsWith("\"")){
							text=text.substring(1,text.length()-1);
						}
						obj=text;
					}
					Image image=buildImage(barcodeForamt,obj.toString(),w,h);
					list.add(new BindData(image));
				}
			}
		}
		return list;
	}

	private Image buildImage(BarcodeFormat format, String data, int w, int h){
		try{
			Map<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			hints.put(EncodeHintType.MARGIN,0);
			BitMatrix matrix;
			if(format.equals(BarcodeFormat.QR_CODE)){
				hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
				matrix = new QRCodeWriter().encode(data,format, w, h,hints);
			} else {
				matrix = new MultiFormatWriter().encode(data,format, w, h,hints);
			}

			int width = matrix.getWidth();
			int height = matrix.getHeight();
			BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
				}
			}
			image = zoomInImage(image, w, h);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			byte[] bytes=outputStream.toByteArray();
			String base64Data=Base64Utils.encodeToString(bytes);
			IOUtils.closeQuietly(outputStream);
			return new Image(base64Data,w,h);
		}catch(Exception ex){
			throw new ReportComputeException(ex);
		}
	}

	private BufferedImage zoomInImage(BufferedImage  originalImage, int width, int height){
		BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0,0,width,height,null);
		g.dispose();
		return newImage;
	}

	@Override
	public ValueType type() {
		return ValueType.zxing;
	}
}
