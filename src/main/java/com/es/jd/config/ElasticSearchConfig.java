package com.es.jd.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * 功能描述:这个ES的配置
 *
 * @Author: zhouzhou
 * @Date: 2020/7/29$ 17:31$
 */
//@Configuration
//public class ElasticSearchConfig {
//
//    @Bean
//    public RestHighLevelClient restHighLevelClient(){
//        RestClientBuilder builder = RestClient.builder(
//                new HttpHost("47.114.142.39", 9200, "http"));
////                new HttpHost("localhost", 9201, "http"));
//        return new RestHighLevelClient(builder);
//    }
//
//
//    @Bean
//    public ElasticsearchRestTemplate elasticsearchTemplate(){
//        return  new ElasticsearchRestTemplate(restHighLevelClient());
//    }
//
//
//}
