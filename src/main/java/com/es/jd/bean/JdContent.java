package com.es.jd.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/8/3$ 10:44$
 */
@Data
@Document(indexName = "jd_content")
public class JdContent {

    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String pName;
    @Field(type = FieldType.Keyword)
    private String pImg;
    @Field(type = FieldType.Keyword)
    private String price;

}
