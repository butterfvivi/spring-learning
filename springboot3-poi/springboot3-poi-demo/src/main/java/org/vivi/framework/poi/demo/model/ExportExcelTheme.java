package org.vivi.framework.poi.demo.model;

import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.*;

/**
 * 定义导出excel的样式主题
 **/
@AllArgsConstructor
public enum ExportExcelTheme {

    /**
     * 默认主题
     */
    DEFAULT("DEFAULT", new XSSFColor[]{
            new XSSFColor(new Color(165, 165, 165), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap())
    }
    ),
    /**
     * 蓝色主题
     */
    BLUE("BLUE", new XSSFColor[]{
            new XSSFColor(new Color(91, 155, 213), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(221, 235, 247), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(189, 215, 238), new DefaultIndexedColorMap())
    }
    ),
    /**
     * 绿色主题
     */
    GREEN("GREEN", new XSSFColor[]{
            new XSSFColor(new Color(112, 173, 71), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(226, 239, 218), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(198, 224, 180), new DefaultIndexedColorMap())
    }
    ),
    /**
     * 橙色主题
     */
    ORANGE("ORANGE", new XSSFColor[]{
            new XSSFColor(new Color(237, 125, 49), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(250, 250, 250), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(252, 228, 214), new DefaultIndexedColorMap()),
            new XSSFColor(new Color(248, 203, 173), new DefaultIndexedColorMap())
    }
    );

    public String code;

    /**
     * 该数组有且只能有4个值
     * theme[0]: 标题栏背景色
     * theme[1]: 标题栏字体颜色
     * theme[2]: 数据隔行色，浅
     * theme[3]: 数据隔行色，深
     */
    public XSSFColor[] theme;
}
