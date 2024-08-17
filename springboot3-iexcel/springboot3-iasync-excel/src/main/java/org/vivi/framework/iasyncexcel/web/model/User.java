package org.vivi.framework.iasyncexcel.web.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.bigdecimal.BigDecimalStringConverter;
import com.alibaba.excel.converters.localdate.LocalDateStringConverter;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeStringConverter;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@TableName("item_user")
public class User {

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
    @ExcelProperty(value = "性别", index = 1)
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


    @ExcelProperty(value = "薪水", converter = BigDecimalStringConverter.class, index = 4)
    private BigDecimal salary;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeStringConverter.class, index = 5)
    private LocalDateTime createTime;

    public User(String name, Integer sex, Integer age, BigDecimal salary) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.salary = salary;
    }
}
