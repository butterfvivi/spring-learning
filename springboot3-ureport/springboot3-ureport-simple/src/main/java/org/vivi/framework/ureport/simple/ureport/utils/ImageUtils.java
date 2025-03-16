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
package org.vivi.framework.ureport.simple.ureport.utils;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;

import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.image.Base64ImageProvider;
import org.vivi.framework.ureport.simple.ureport.image.DefaultImageProvider;
import org.vivi.framework.ureport.simple.ureport.image.HttpImageProvider;
import org.vivi.framework.ureport.simple.ureport.image.HttpsImageProvider;
import org.vivi.framework.ureport.simple.ureport.image.ImageProvider;

/**
 * @author Jacky.gao
 * @since 2017年3月20日
 */
public class ImageUtils {
	
	private static final ImageProvider defaultImageProvider = new DefaultImageProvider();

	private static final ImageProvider httpImageProvider = new HttpImageProvider();

	private static final ImageProvider httpsImageProvider = new HttpsImageProvider();

	private static final Base64ImageProvider base64ImageProvider = new Base64ImageProvider();

	public static InputStream base64DataToInputStream(String base64Data) {
		byte[] bytes = Base64Utils.decodeFromString(base64Data);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		return inputStream;
	}
	
	public static String getResizedBase64(String base64Data, int width, int height) {
		InputStream inputStream = null;
		try {
			if (width > 0 && height > 0) {
				byte[] imageBytes = Base64.getDecoder().decode(base64Data);
				BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
				BufferedImage outputImage = new BufferedImage(width, height, Transparency.TRANSLUCENT);
				Graphics2D g = outputImage.createGraphics();
				g.drawImage(inputImage, 0, 0, width, height, null);
				g.dispose();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(outputImage, "png", outputStream);
				inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			}
			byte[] bytes = IOUtils.toByteArray(inputStream);
			return Base64Utils.encodeToString(bytes);
		} catch (Exception ex) {
			throw new ReportComputeException(ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public static String getImageBase64Data(Object data, int width, int height) {
		String path = String.valueOf(data);
		InputStream inputStream = null;
		try {
			inputStream = getImage(path);
			if (width > 0 && height > 0) {
				BufferedImage inputImage = ImageIO.read(inputStream);
				BufferedImage outputImage = new BufferedImage(width, height, Transparency.TRANSLUCENT);
				Graphics2D g = outputImage.createGraphics();
				g.drawImage(inputImage, 0, 0, width, height, null);
				g.dispose();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(outputImage, "png", outputStream);
				inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			}
			byte[] bytes = IOUtils.toByteArray(inputStream);
			return Base64Utils.encodeToString(bytes);
		} catch (Exception ex) {
			throw new ReportComputeException(ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	public static InputStream getImage(String file) {
		if (defaultImageProvider.support(file)) {
			return defaultImageProvider.getImage(file);
		}
		if (httpImageProvider.support(file)) {
			return httpImageProvider.getImage(file);
		}
		if (httpsImageProvider.support(file)) {
			return httpsImageProvider.getImage(file);
		}
		if (base64ImageProvider.support(file)) {
			return base64ImageProvider.getImage(file);
		}
		throw new ReportComputeException("Unsupport image path :" + file);
	}
}
