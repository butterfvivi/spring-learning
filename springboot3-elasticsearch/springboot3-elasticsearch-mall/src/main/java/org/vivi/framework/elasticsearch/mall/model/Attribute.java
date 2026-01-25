package org.vivi.framework.elasticsearch.mall.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class Attribute {

    /** 属性ID*/
    private long attrId;
    /**属性名称，如“容量”或“网络” */
    @Field(type = FieldType.Keyword)
    private String attrName;
    /** 属性值，如“128G”或“5G” */
    @Field(type = FieldType.Keyword)
    private String attrValue;
}
