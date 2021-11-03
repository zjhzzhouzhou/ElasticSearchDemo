package com.es.jd.bean;

import lombok.Data;

import java.util.List;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/8/14$ 11:13$
 */
@Data
public class ESUserSearch {

    // 名字过滤
    private String name;
    // 年龄大于等于
    private Integer ageGte;
    // 年龄小于
    private Integer ageLt;
    // 必须符合的标签
    private List<String> tags;
    // 备注包含
    private String desc;

}
