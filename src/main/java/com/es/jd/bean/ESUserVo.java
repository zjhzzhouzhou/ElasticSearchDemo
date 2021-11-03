package com.es.jd.bean;

import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * 功能描述:ES的用户搜索结果
 *
 * @Author: zhouzhou
 * @Date: 2020/7/30$ 9:57$
 */
@Data
public class ESUserVo {

    private Long id;
    private String name;
    private Integer age;
    private List<String> tags;
    // 高亮部分
    private List<String> highLightTags;
    private String desc;
    // 高亮部分
    private List<String> highLightDesc;
    // 坐标
    private GeoPoint location;

}
