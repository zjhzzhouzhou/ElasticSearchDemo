package com.es.jd.bean;

import lombok.Data;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/8/3$ 14:38$
 */
@Data
public class JdSearchRequest {

    private String pName;

    private int pageNo = 1;

    private int pageSize = 10;

}
