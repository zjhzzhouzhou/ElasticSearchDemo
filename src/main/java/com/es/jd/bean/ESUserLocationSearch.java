package com.es.jd.bean;

import lombok.Data;

import java.util.List;

/**
 * 功能描述: 搜索附近的人
 *
 * @Author: zhouzhou
 * @Date: 2020/8/14$ 11:13$
 */
@Data
public class ESUserLocationSearch {

    // 纬度 [3.86， 53.55]
    private Double lat;

    // 经度 [73.66, 135.05]
    private Double lon;

    // 搜索范围(单位米)
    private Integer distance;

    // 年龄大于等于
    private Integer ageGte;

    // 年龄小于
    private Integer ageLt;
}
