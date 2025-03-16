package org.vivi.framework.ureport.simple.ureport.image;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import org.vivi.framework.ureport.simple.ureport.utils.ImageUtils;

@Service
public class Base64ImageProvider implements ImageProvider {

	@Override
	public InputStream getImage(String path) {
		String base64Image = path.substring(path.indexOf(",") + 1, path.length());
		return ImageUtils.base64DataToInputStream(base64Image);
	}

	@Override
	public boolean support(String path) {
		return path.startsWith("data:image");
	}

}
