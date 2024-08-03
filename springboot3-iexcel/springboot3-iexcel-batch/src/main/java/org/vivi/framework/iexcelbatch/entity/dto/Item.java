package org.vivi.framework.iexcelbatch.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Item  {

    //@Excel(name = "商品编号")
    @ExcelProperty(value = "商品编号",index = 0)
    private Integer id;

    //@Excel(name = "商品编号")
    @ExcelProperty(value = "商品编码",index = 1)
    private String code;

    //@Excel(name = "商品名称")
    @ExcelProperty(value = "商品名称",index = 2)
    private String name;

    //@Excel(name = "创建时间",dateFormat = "yyyy-MM-dd")
    @ExcelProperty(value = "创建时间",index = 3,format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


    public Item(String code, String name) {
        this.code = code;
        this.name = name;
    }
}