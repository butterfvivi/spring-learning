package org.vivi.framework.report.bigdata;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.vivi.framework.report.bigdata.common.annotation.Header;
import org.vivi.framework.report.bigdata.poi.model.Format;

import java.util.Date;

/**
 * 接口实体类
 */
@Data
public class Interface2 {
    /**
     * 接口名称
     */
    @Header(name = "接口名称",prompt = "测试提示内容",dropdown = {"3213","djdjd","哈哈"},order = "1",align = HorizontalAlignment.LEFT)
    @NotNull(message = "接口名称不能为空")
    private String name;
    /**
     * 请求url地址
     */
    @Header(name = "请求url地址",order = "2",prompt = "测试提示内容")
    @NotNull(message = "接口地址不能为空")
    private String url;
    /**
     * 请求参数json数据
     */
    @Header(name = "请求参数json数据",order = "4")
    private String params;
    /**
     * 参数请求类型：json/form
     */
    @Header(name = "参数请求类型")
    private Integer paramType;
    /**
     * 请求方式：get/post
     */
    @Header(name = "请求方式",order = "10")
    private String requestType;
    /**
     * 期望的返回结果code
     */
    @Header(name = "期望的返回结果code",order = "-1")
    private String expectCode;
    /**
     * 完整的返回结果
     */
    @Header(name = "完整的返回结果",order = "-2")
    private String result;

    @Header(name ="价格", format = Format.DECIMAL_2, regex = "[0-9]+(\\.[0-9]{0,5})?" , align = HorizontalAlignment.LEFT)
    private Double price;

    @Header(name = "日期",type = Date.class, format = "yyyy-MM-dd")
    private String date;

}
