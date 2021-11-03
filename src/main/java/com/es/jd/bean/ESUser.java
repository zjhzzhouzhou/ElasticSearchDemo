package com.es.jd.bean;

import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.geometry.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.util.List;

/**
 * 功能描述:ES的用户
 *
 * @Author: zhouzhou
 * @Date: 2020/7/30$ 9:57$
 */
@Data
@Document(indexName = "es_user")
public class ESUser {

    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Integer)
    private Integer age;
    @Field(type = FieldType.Keyword)
    private List<String> tags;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String desc;
    @GeoPointField
    private GeoPoint location;

}
