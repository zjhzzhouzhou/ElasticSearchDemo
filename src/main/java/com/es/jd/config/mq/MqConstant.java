package com.es.jd.config.mq;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/8/7$ 16:09$
 */
public class MqConstant {


    /**
     * top
     */
    public static class Topic {

        /**
         * 稿件录入
         */
        public static final String ES_USER_IMPORT = "ES_USER_IMPORT";

    }


    /**
     * TAG
     */
    public static class Tag {

        public static final String ES_USER_IMPORT_TAG_INSERT = "ES_USER_IMPORT_TAG_INSERT";
        public static final String ES_USER_IMPORT_TAG_UPDATE = "ES_USER_IMPORT_TAG_UPDATE";
        public static final String ES_USER_IMPORT_TAG_DELETE = "ES_USER_IMPORT_TAG_DELETE";
    }


    /**
     * consumeGroup 消费者
     */
    public static class ConsumeGroup {

        public static final String ES_USER_IMPORT = "GID_ES_USER_IMPORT";

    }

}
