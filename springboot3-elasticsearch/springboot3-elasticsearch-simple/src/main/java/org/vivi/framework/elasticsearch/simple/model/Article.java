package org.vivi.framework.elasticsearch.simple.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "article",createIndex = true)
public class Article {

    @Id
    private Long id;

    /**
     * 文章标题（分词字段）
     * type: 字段类型（Text 用于全文检索，会分词）
     * analyzer: 分词器（ik_max_word 为 IK 分词器的细粒度分词）
     * searchAnalyzer: 搜索时使用的分词器
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;

    /**
     * 关键字字段，不分词
     * type: Keyword 用于精确匹配，不分词
     */
    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Keyword)
    private String publishTime;


}
