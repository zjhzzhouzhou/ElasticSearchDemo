package com.es.jd.config.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by xiaoxudong on 2019/3/22
 */
@Data
@Component
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMqProperties {
 

    private String namesrvAddr;
    private Integer sendMsgTimeoutMillis;

    /**
     * 失败重试次数
     */
    private Integer reconsumeTimes;

 
}