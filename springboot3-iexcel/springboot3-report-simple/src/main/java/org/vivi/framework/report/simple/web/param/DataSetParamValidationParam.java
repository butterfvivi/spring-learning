package org.vivi.framework.report.simple.web.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by raodeming on 2021/3/24.
 */
@Data
public class DataSetParamValidationParam implements Serializable {

    /** 参数示例项 */
    private String sampleItem;


    /** js校验字段值规则，满足校验返回 true */
    @NotBlank(message = "validationRules not empty")
    private String validationRules;
}
