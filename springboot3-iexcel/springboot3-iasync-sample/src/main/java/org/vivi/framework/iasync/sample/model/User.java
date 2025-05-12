package org.vivi.framework.iasync.sample.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeStringConverter;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@TableName("user")
public class User {

    /**
     *
     */
    private Long id;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名", index = 0)
    private String userName;

    /**
     * 性别: 0未知 1男 2 女
     */
    @ExcelProperty(value = "性别", index = 1)
    private Integer gender;

    /**
     * 年龄
     */
    @ExcelProperty(value = "code", index = 2)
    private Integer userCode;

    /**
     * 手机号
     */
    /**
     * 年龄
     */
    @ExcelProperty(value = "mobile", index = 3)
    private Long mobile;
//    @ExcelProperty(value = "生日", converter = LocalDateStringConverter.class, index = 3)
//    private LocalDate birthday;
//
//
//    @ExcelProperty(value = "薪水", converter = BigDecimalStringConverter.class, index = 4)
//    private BigDecimal salary;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeStringConverter.class, index = 4)
    private LocalDateTime createTime;

}
