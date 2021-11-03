package com.es.jd.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 功能描述:用于发送请求
 *
 * @Author: zhouzhou
 * @Date: 2020/10/10$ 11:13$
 */
public class HttpUtils {

    private static final Logger levelLogger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 发送post请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String sendPost(String url,Map<String,String> paramMap){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建post请求
        HttpPost httpPost = new HttpPost(url);

        String jsonString = JSON.toJSONString(paramMap);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            levelLogger.info("响应状态为:" + response.getStatusLine());
            return EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            throw new RuntimeException("调用失败", e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送post请求
     * @param url
     * @return
     */
    public static String sendGet(String url){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建post请求
        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            levelLogger.info("响应状态为:" + response.getStatusLine());
            return EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            throw new RuntimeException("调用失败", e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
