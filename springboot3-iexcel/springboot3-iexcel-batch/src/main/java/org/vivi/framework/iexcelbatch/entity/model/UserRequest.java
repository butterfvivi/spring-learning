package org.vivi.framework.iexcelbatch.entity.model;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.bigdecimal.BigDecimalStringConverter;
import com.alibaba.excel.converters.localdate.LocalDateStringConverter;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeStringConverter;
import lombok.Data;
import org.vivi.framework.iexcelbatch.common.converter.EnumColumConvert;
import org.vivi.framework.iexcelbatch.common.converter.EnumFormat;
import org.vivi.framework.iexcelbatch.common.enums.SexEnum;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class UserRequest {

    /**
     *
     */
    private Long id;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    /**
     * 性别: 0未知 1男 2 女
     */
    @ExcelProperty(value = "性别", converter = EnumColumConvert.class, index = 1)
    @EnumFormat(value = SexEnum.class,columnCode ="code",columnName = "desc")
    private Integer sex;

    /**
     * 年龄
     */
    @ExcelProperty(value = "年龄", index = 2)
    private Integer age;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日", converter = LocalDateStringConverter.class, index = 3)
    private LocalDate birthday;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeStringConverter.class, index = 5)
    private LocalDateTime createTime;


    @ExcelProperty(value = "薪水", converter = BigDecimalStringConverter.class, index = 4)
    private BigDecimal salary;

}