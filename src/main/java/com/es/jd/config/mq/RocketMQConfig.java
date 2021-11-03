package com.es.jd.config.mq;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * Created by xiaoxudong on 2019/3/22
 * @author zhouzhou
 */
@Component
public class RocketMQConfig {
    Logger log = LoggerFactory.getLogger(RocketMQConfig.class);

    @Autowired
    private RocketMqProperties mqProperties;

    // Spring应用上下文环境
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 初始化生产者
     *
     * @return
     */
    @Bean
    public DefaultMQProducer defaultProducer() throws Exception {
        Object environment = applicationContext.getEnvironment().getProperty("rocketmq.namesrvAddr");

        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(MqConstant.ConsumeGroup.ES_USER_IMPORT);
        // 设置NameServer的地址
        producer.setNamesrvAddr(mqProperties.getNamesrvAddr());
        // 设置发送消息超时时间
        producer.setSendMsgTimeout(mqProperties.getSendMsgTimeoutMillis());
        // 设置重试次数
        producer.setRetryTimesWhenSendFailed(mqProperties.getReconsumeTimes());
        // 启动Producer实例
        producer.start();

        return producer;
    }

}