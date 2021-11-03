package com.es.jd.bean;

import lombok.Data;

import java.util.List;

/**
 * 功能描述:ES的用户搜索结果
 *
 * @Author: zhouzhou
 * @Date: 2020/7/30$ 9:57$
 */
@Data
public class PeopleNearByVo {

     private ESUserVo esUserVo;

     private Double distance;

}
