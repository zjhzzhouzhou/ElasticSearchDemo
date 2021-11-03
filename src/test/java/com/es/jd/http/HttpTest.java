package com.es.jd.http;

import com.alibaba.fastjson.JSON;
import com.es.jd.bean.ISVResultVO;
import com.es.jd.bean.JdContent;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/9/30$ 10:32$
 */
public class HttpTest {

    private static  final String URL = "http://47.92.71.236/";

    /**
     * GET---无参测试
     *
     * @date 2018年7月13日 下午4:18:50
     */
    @Test
    public void doGetTestOne() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:8082/config/test");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            String contentStr = EntityUtils.toString(responseEntity);
            ISVResultVO<JdContent> result = convertToObject(contentStr, JdContent.class);
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * 转换类
     * @param content
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> ISVResultVO<T> convertToObject(String content, Class<T> clazz){
        ISVResultVO ISVResultVO = JSON.parseObject(content, ISVResultVO.class);
        Object data = ISVResultVO.getData();
        if (data != null  ){
            T convertData = (T) data;
            ISVResultVO.setData(convertData);
        }
        return ISVResultVO;
    }
}
