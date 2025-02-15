package org.vivi.framework.generator.ddl.translate.entity;

import lombok.Data;

@Data
public class RangeContext {
    // 字段名
    String fieldName;
    // 字段注释
    String fieldComment;
    // 追加注释
    String fieldCommentAppend;
    //合体注释
    String syndicationNotes;
    String fieldType;
    // 字段长度
    String fieldLength;
    /** // 是否为主键 */
    String isPrimaryKey;
    // 是否自增
    String isSelfIncreasing;
}
