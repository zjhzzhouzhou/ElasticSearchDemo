package com.es.jd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.es.jd.bean.ESUser;
import com.es.jd.config.mq.MqConstant;
import com.es.jd.config.mq.RocketMqProperties;
import com.es.jd.repository.EsUserRepository;
import com.es.jd.temp.entity.EsUserEntity;
import com.es.jd.utils.BeanUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 功能描述: Mq 消费者
 *
 * @Author: zhouzhou
 * @Date: 2020/8/7$ 17:15$
 */
@Slf4j
@Service
public class MqConsumerService {

    @Autowired
    private RocketMqProperties mqProperties;

    @Autowired
    private EsUserRepository esUserRepository;


    @PostConstruct
    public void consumerTest() throws Exception {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(MqConstant.ConsumeGroup.ES_USER_IMPORT);
        defaultMQPushConsumer.setNamesrvAddr(mqProperties.getNamesrvAddr());
        // * 代表不过滤
        defaultMQPushConsumer.subscribe(MqConstant.Topic.ES_USER_IMPORT, "*");
        defaultMQPushConsumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    byte[] body = msg.getBody();
                    String tags = msg.getTags();
                    String msgId = msg.getMsgId();


                    // 根据标签tag来决定什么操作
                    executeByTags(tags, body);

                } catch (Exception e) {
                    // 对次数进行冲正并且落库 todo 发送告警信息
                    log.warn(String.format("即将导入ES库失败, 失败原因为{%s}", e.getMessage()), e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

        });
        defaultMQPushConsumer.start();
        log.info(String.format("消费者{%s}启动了", MqConstant.Topic.ES_USER_IMPORT));
    }

    private void executeByTags(String tags, byte[] body) {
        switch (tags) {
            case MqConstant.Tag.ES_USER_IMPORT_TAG_INSERT:
                JSONObject insertObject = (JSONObject) JSON.parse(body);
                EsUserEntity insertEsUserRequest = insertObject.toJavaObject(EsUserEntity.class);

                if (insertEsUserRequest != null) {
                    ESUser esUser = BeanUtils.copy(insertEsUserRequest, ESUser.class);
                    List<String> tagList = Lists.newArrayList(insertEsUserRequest.getTags().split("\\|"));
                    esUser.setTags(tagList);
                    esUser.setDesc(insertEsUserRequest.getUserDesc());
                    esUser.setLocation(new GeoPoint(insertEsUserRequest.getLat(),insertEsUserRequest.getLon()));
                    ESUser save = esUserRepository.save(esUser);
                    log.info("导入es库成功" + save);
                }
                break;
            case MqConstant.Tag.ES_USER_IMPORT_TAG_UPDATE:
                JSONObject updateObject = (JSONObject) JSON.parse(body);
                EsUserEntity updateEsUser = updateObject.toJavaObject(EsUserEntity.class);
                if (updateEsUser != null) {
                    ESUser esUser = BeanUtils.copy(updateEsUser, ESUser.class);
                    List<String> tagList = Lists.newArrayList(updateEsUser.getTags().split("\\|"));
                    esUser.setTags(tagList);
                    esUser.setDesc(updateEsUser.getUserDesc());
                    esUser.setLocation(new GeoPoint(updateEsUser.getLon(), updateEsUser.getLat()));
                    ESUser save = esUserRepository.save(esUser);
                    log.info("更新es库成功" + save);
                }
                break;
            case MqConstant.Tag.ES_USER_IMPORT_TAG_DELETE:
                Integer id = (Integer) JSON.parse(body);
                esUserRepository.deleteById(new Long(id));
                log.info("删除es库成功id=" + id);

                break;

        }
    }


}
