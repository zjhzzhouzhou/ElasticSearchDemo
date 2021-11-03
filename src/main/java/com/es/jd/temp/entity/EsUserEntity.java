package com.es.jd.temp.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (EsUser)实体类
 *
 * @author makejava
 * @since 2020-08-10 14:23:59
 */
@Data
public class EsUserEntity implements Serializable {
    private static final long serialVersionUID = 578800011612714754L;
    /**
    * 主键
    */
    private Long id;
    
    private String name;
    
    private Integer age;
    /**
    * 多标签用  '|' 分割
    */
    private String tags;
    /**
    * 用户简介
    */
    private String userDesc;
    
    private String isDeleted = "0";
    
    private Date gmtCreate;
    
    private Date gmtModified;

    // 经度
    private Double lat;

    // 维度
    private Double lon;

}