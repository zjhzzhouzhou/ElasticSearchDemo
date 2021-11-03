package com.es.jd.repository;

import com.es.jd.bean.ESUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/7/30$ 14:17$
 */
public interface EsUserRepository extends ElasticsearchRepository<ESUser,Long> {


}
