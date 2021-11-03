package com.es.jd.repository;

        import com.es.jd.bean.JdContent;
        import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/7/30$ 14:17$
 */

public  interface JdContentRepository extends ElasticsearchRepository<JdContent,String> {


}
