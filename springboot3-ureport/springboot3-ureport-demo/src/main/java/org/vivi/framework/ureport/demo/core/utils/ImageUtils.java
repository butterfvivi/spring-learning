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
package org.vivi.framework.ureport.demo.core.utils;

import java.util.Base64;
import org.vivi.framework.ureport.demo.core.exception.ReportComputeException;
import org.vivi.framework.ureport.demo.core.image.ChartImageProcessor;
import org.vivi.framework.ureport.demo.core.image.ImageProcessor;
import org.vivi.framework.ureport.demo.core.image.ImageType;
import org.vivi.framework.ureport.demo.core.image.StaticImageProcessor;
import org.apache.poi.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    private static Map<ImageType, ImageProcessor<?>> imageProcessorMap = new HashMap<ImageType, ImageProcessor<?>>();

    static {
        StaticImageProcessor staticImageProcessor = new StaticImageProcessor();
        imageProcessorMap.put(ImageType.image, staticImageProcessor);
        ChartImageProcessor chartImageProcessor = new ChartImageProcessor();
        imageProcessorMap.put(ImageType.chart, chartImageProcessor);
    }

    public static InputStream base64DataToInputStream(String base64Data) {
        //byte[] bytes = Base64Utils.decodeFromString(base64Data);
        byte[] bytes = Base64.getEncoder().encode(base64Data.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return inputStream;
    }

    @SuppressWarnings("unchecked")
    public static String getImageBase64Data(ImageType type, Object data, int width, int height) {
        ImageProcessor<Object> targetProcessor = (ImageProcessor<Object>) imageProcessorMap.get(type);
        if (targetProcessor == null) {
            throw new ReportComputeException("Unknow image type :" + type);
        }
        InputStream inputStream = targetProcessor.getImage(data);
        try {
            if (width > 0 && height > 0) {
                BufferedImage inputImage = ImageIO.read(inputStream);
                BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
                Graphics2D g = outputImage.createGraphics();
                g.drawImage(inputImage, 0, 0, width, height, null);
                g.dispose();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(outputImage, "png", outputStream);
                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            }
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new ReportComputeException(ex);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
