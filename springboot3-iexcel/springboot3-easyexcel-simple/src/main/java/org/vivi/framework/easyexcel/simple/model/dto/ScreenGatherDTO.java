package org.vivi.framework.easyexcel.simple.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * @author Cauli
 * @date 2022/12/1 15:10
 * @description 查询返回数据模型
 */
@Data
@ToString
public class ScreenGatherDTO {
    /**
     * 产品型号
     */
    private String partMode;

    /**
     * 厂商
     */
    private String supplier;

    /**
     * 屏幕尺寸
     */
    private String screenSize;

    /**
     * 屏幕分辨率
     */
    private String resolution;

    /**
     * 屏幕刷新率
     */
    private String refreshRate;

    /**
     * 面板属性
     */
    private String panel;

    /**
     * 用于存储得分模块map
     */
    private Map<String, String> scoreMap;

    /**
     * 表头数据模板参数
     */
    private String valueTemplateParam;

    /**
     * 表格数据模板参数
     */
    private String headerTemplateParam;
}
