package com.es.jd.http;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/10/20$ 15:40$
 */
public class RateLimiterDemo {

    public void test(){
        RateLimiter rateLimiter = RateLimiter.create(100);
    }
}
