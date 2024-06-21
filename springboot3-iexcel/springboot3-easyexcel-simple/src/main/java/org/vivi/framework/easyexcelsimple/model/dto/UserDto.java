package org.vivi.framework.easyexcelsimple.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.vivi.framework.easyexcelsimple.common.enums.EnumFiledConvert;
import org.vivi.framework.easyexcelsimple.converter.CommonConvert;
import org.vivi.framework.easyexcelsimple.converter.LocalDateTimeConverter;

import java.time.LocalDateTime;

@Data
public class UserDto {

    @ExcelProperty(index = 0)
    private String userCode;

    @ExcelProperty(index = 1)
    private String userName;

    @ExcelProperty(index = 2)
    private String mobile;

    @ExcelProperty(index = 3, converter = CommonConvert.class)
    @EnumFiledConvert(enumMap = "0-保密,1-男,2-女")
    private Integer gender;

    @ExcelProperty(index = 4,converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;
}
