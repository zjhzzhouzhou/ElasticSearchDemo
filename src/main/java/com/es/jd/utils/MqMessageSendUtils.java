package com.es.jd.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:Mq 消息发送工具类
 *
 * @author: zhouzhou
 * Date: 2020-05-25
 * Time: 2:19 PM
 */
@Component
@Slf4j
public class MqMessageSendUtils {

    @Autowired(required = false)
    private DefaultMQProducer rocketMqProducer;


    /**
     * private RocketMqProperties mqProperties;
     * 发送普通消息
     *
     * @param topic
     * @param tag
     * @param body
     * @param <T>
     * @return
     */
    public <T> boolean sendNormalMessage(String topic, String tag, T body) {

        Message message = new Message(topic, tag, JSON.toJSONBytes(body));

        try {
            SendResult sendResult = rocketMqProducer.send(message);
            if (sendResult != null) {
                log.info("mq 消息发送成功" + sendResult);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.warn("mq 消息发送失败", e);
            return false;
        }

    }

    /**
     * 发送普通延时消息
     *
     * @param topic
     * @param tag
     * @param body
     * @param <T>
     * @param delay 毫秒
     * @return
     */
    public <T> boolean sendNormalDelayMessage(String topic, String tag, T body, Long delay) {

        Message message = new Message(topic, tag, JSON.toJSONBytes(body));
        // 默认延时 10s
        // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        message.setDelayTimeLevel(3);
        try {
            SendResult sendResult = rocketMqProducer.send(message);
            if (sendResult != null) {
                log.info("mq 延时消息发送成功" + sendResult);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.warn("mq 延时消息发送失败", e);
            return false;
        }
    }


}
