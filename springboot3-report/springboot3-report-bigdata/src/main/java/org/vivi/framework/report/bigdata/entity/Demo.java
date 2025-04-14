package org.vivi.framework.report.bigdata.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 示例 实体类
 */
@TableName
@Data
public class Demo {
    @ExcelProperty("编号")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ExcelProperty("标题")
    private String title;
    @ExcelProperty("内容")
    private String content;
    @ExcelProperty("创建日期")
    private LocalDateTime createDate;
    private String c1;
    private String c2;
    private String c3;
    private String c4;
    private String c5;
}
