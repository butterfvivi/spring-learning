package org.vivi.framework.iexcelbatch.entity.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.bigdecimal.BigDecimalStringConverter;
import com.alibaba.excel.converters.localdate.LocalDateStringConverter;
import com.alibaba.excel.converters.localdatetime.LocalDateTimeStringConverter;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vivi.framework.iexcelbatch.common.converter.EnumColumConvert;
import org.vivi.framework.iexcelbatch.common.converter.EnumFormat;
import org.vivi.framework.iexcelbatch.common.enums.SexEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@TableName("item_user")
@AllArgsConstructor
public class User {

    /**
     *
     */
    private Long id;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名", index = 0)
    @TableField("name")
    private String name;

    /**
     * 性别: 0未知 1男 2 女
     */
    @ExcelProperty(value = "性别", converter = EnumColumConvert.class, index = 1)
    @EnumFormat(value = SexEnum.class,columnCode ="code",columnName = "desc")
    @TableField("sex")
    private Integer sex;

    /**
     * 年龄
     */
    @ExcelProperty(value = "年龄", index = 2)
    @TableField("age")
    private Integer age;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日", converter = LocalDateStringConverter.class, index = 3)
    @TableField("birthday")
    private LocalDate birthday;


    @ExcelProperty(value = "薪水", converter = BigDecimalStringConverter.class, index = 4)
    @TableField("salary")
    private BigDecimal salary;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeStringConverter.class, index = 5)
    @TableField("create_time")
    private LocalDateTime createTime;

    @ExcelProperty(value = "field1", index = 6)
    @TableField("field1")
    private String field1;

    @ExcelProperty(value = "field2", index = 7)
    @TableField("field2")
    private String field2;

    @ExcelProperty(value = "field3", index = 8)
    @TableField("field3")
    private String field3;

    @ExcelProperty(value = "field4", index = 9)
    @TableField("field4")
    private String field4;

    @ExcelProperty(value = "field5", index = 10)
    @TableField("field5")
    private String field5;

    @ExcelProperty(value = "field6", index = 11)
    @TableField("field6")
    private String field6;

    @ExcelProperty(value = "field7", index = 12)
    @TableField("field7")
    private String field7;

    @ExcelProperty(value = "field8", index = 13)
    @TableField("field8")
    private String field8;

    @ExcelProperty(value = "field9", index = 14)
    @TableField("field9")
    private String field9;

    @ExcelProperty(value = "field10", index = 15)
    @TableField("field10")
    private String field10;

    @ExcelProperty(value = "field11", index = 16)
    @TableField("field11")
    private String field11;

    @ExcelProperty(value = "field12", index = 17)
    @TableField("field12")
    private String field12;

    @ExcelProperty(value = "field13", index = 18)
    @TableField("field13")
    private String field13;

    @ExcelProperty(value = "field14", index = 19)
    @TableField("field14")
    private String field14;

    @ExcelProperty(value = "field15", index = 20)
    @TableField("field15")
    private String field15;

    @ExcelProperty(value = "field16", index = 21)
    @TableField("field16")
    private String field16;

    @ExcelProperty(value = "field17", index = 22)
    @TableField("field17")
    private String field17;

    @ExcelProperty(value = "field18", index = 23)
    @TableField("field18")
    private String field18;

    @ExcelProperty(value = "field19", index = 24)
    @TableField("field19")
    private String field19;

    @ExcelProperty(value = "field20", index = 25)
    @TableField("field20")
    private String field20;

    @ExcelProperty(value = "field21", index = 26)
    @TableField("field21")
    private String field21;

    @ExcelProperty(value = "field22", index = 27)
    @TableField("field22")
    private String field22;

    @ExcelProperty(value = "field23", index = 28)
    @TableField("field23")
    private String field23;

    @ExcelProperty(value = "field24", index = 29)
    @TableField("field24")
    private String field24;

    @ExcelProperty(value = "field25", index = 30)
    @TableField("field25")
    private String field25;

    @ExcelProperty(value = "field26", index = 31)
    @TableField("field26")
    private String field26;

    @ExcelProperty(value = "field27", index = 32)
    @TableField("field27")
    private String field27;

    @ExcelProperty(value = "field28", index = 33)
    @TableField("field28")
    private String field28;

    @ExcelProperty(value = "field29", index = 34)
    @TableField("field29")
    private String field29;

    @ExcelProperty(value = "field30", index = 35)
    @TableField("field30")
    private String field30;

    @ExcelProperty(value = "field31", index = 36)
    @TableField("field31")
    private String field31;

    @ExcelProperty(value = "field32", index = 37)
    @TableField("field32")
    private String field32;

    @ExcelProperty(value = "field33", index = 38)
    @TableField("field33")
    private String field33;

    @ExcelProperty(value = "field34", index = 39)
    @TableField("field34")
    private String field34;

    @ExcelProperty(value = "field35", index = 40)
    @TableField("field35")
    private String field35;

    @ExcelProperty(value = "field36", index = 41)
    @TableField("field36")
    private String field36;

    @ExcelProperty(value = "field37", index = 42)
    @TableField("field37")
    private String field37;

    @ExcelProperty(value = "field38", index = 43)
    @TableField("field38")
    private String field38;

    @ExcelProperty(value = "field39", index = 44)
    @TableField("field39")
    private String field39;

    @ExcelProperty(value = "field40", index = 45)
    @TableField("field40")
    private String field40;

    @ExcelProperty(value = "field41", index = 46)
    @TableField("field41")
    private String field41;

    @ExcelProperty(value = "field42", index = 47)
    @TableField("field42")
    private String field42;

    @ExcelProperty(value = "field43", index = 48)
    @TableField("field43")
    private String field43;

    @ExcelProperty(value = "field44", index = 49)
    @TableField("field44")
    private String field44;

    @ExcelProperty(value = "field45", index = 50)
    @TableField("field45")
    private String field45;

    @ExcelProperty(value = "field46", index = 51)
    @TableField("field46")
    private String field46;

    @ExcelProperty(value = "field47", index = 52)
    @TableField("field47")
    private String field47;

    @ExcelProperty(value = "field48", index = 53)
    @TableField("field48")
    private String field48;

    @ExcelProperty(value = "field49", index = 54)
    @TableField("field49")
    private String field49;


    @ExcelProperty(value = "field50", index = 55)
    @TableField("field50")
    private String field50;

}
