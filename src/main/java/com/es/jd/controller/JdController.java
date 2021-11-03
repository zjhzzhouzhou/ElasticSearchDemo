package com.es.jd.controller;

import com.es.jd.bean.ESUser;
import com.es.jd.bean.JdContent;
import com.es.jd.bean.JdSearchRequest;
import com.es.jd.config.mq.MqConstant;
import com.es.jd.service.JdService;
import com.es.jd.utils.MqMessageSendUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.lucene.util.fst.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/8/3$ 10:49$
 */
@Controller
@Api(tags = "京东相关ctl")
@RestController("/jd")
public class JdController {

    @Autowired
    private JdService jdService;



    @ApiOperation("通过关键词爬虫")
    @GetMapping("/content/{keyword}")
    public Boolean parseContent(@PathVariable("keyword") String keyword){
        return jdService.parseContent(keyword);
    }

    @ApiOperation("通过关键词爬虫")
    @GetMapping("/content/search")
    public Page<JdContent> searchContent(JdSearchRequest searchRequest){
        return jdService.searchContent(searchRequest);
    }


}
