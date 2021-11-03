package com.es.jd.controller;

import com.es.jd.bean.ISVResultVO;
import com.es.jd.bean.JdContent;
import com.es.jd.utils.SpringContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/8/3$ 10:49$
 */
@Controller
@Api(tags = "nacos配置测试")
@RestController("/nacos")
@RefreshScope
public class TestController {


    @Value("${nacos.test}")
    private String configStr;

    @ApiOperation("返回nacos.test的配置值")
    @GetMapping("/config/{configStr}")
    public ISVResultVO<JdContent> parseContent(@PathVariable("configStr") String str) {
        String annotationStr = "注解Value" + configStr;
        String paramStr = "参数Value" + SpringContextUtil.getPropertiesValue(str);
        JdContent content = new JdContent();
        content.setId("1");
        content.setPrice("12345");
        content.setPName("测试相关");
        content.setPImg(annotationStr);
        return new ISVResultVO<>(content);
    }

}
