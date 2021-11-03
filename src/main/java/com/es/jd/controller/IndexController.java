package com.es.jd.controller;

import com.es.jd.bean.ESUser;
import com.es.jd.bean.JdContent;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/7/31$ 16:52$
 */
@RestController
public class IndexController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @RequestMapping(path = {"/", "index"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/create-index", method = RequestMethod.POST)
    @ApiOperation("创建索引")
    public Object createEsIndex(String name) {
        boolean index = false;
        boolean putMapping = false;
        if (StringUtils.equals("jd", name)) {
            index = elasticsearchRestTemplate.indexOps(JdContent.class).create();
            putMapping = elasticsearchRestTemplate.indexOps(JdContent.class).putMapping();

        } else if (StringUtils.equals("user", name)) {
            index = elasticsearchRestTemplate.indexOps(ESUser.class).create();
            putMapping = elasticsearchRestTemplate.indexOps(ESUser.class).putMapping();

        }

        System.out.println("创建索引结果是" + index);
        return index && putMapping;
    }

    @RequestMapping(value = "/update-index", method = RequestMethod.POST)
    @ApiOperation("更新索引")
    public Object updateEsUserIndex() {
        boolean deleteIndex = elasticsearchRestTemplate.indexOps(ESUser.class).putMapping();
        System.out.println("更新索引结果是" + deleteIndex);
        return deleteIndex;
    }

    @RequestMapping(value = "/delete-index", method = RequestMethod.POST)
    @ApiOperation("删除索引")
    public Object deleteEsIndex() {
        boolean deleteIndex = elasticsearchRestTemplate.indexOps(JdContent.class).delete();
        System.out.println("删除索引结果是" + deleteIndex);
        return deleteIndex;
    }

    @RequestMapping(value = "/exist-index", method = RequestMethod.POST)
    @ApiOperation("是否存在索引")
    public Object existEsIndex() {
        boolean existsIndex = elasticsearchRestTemplate.indexExists(ESUser.class);
        System.out.println("是否存在的结果是" + existsIndex);
        return existsIndex;
    }
}
