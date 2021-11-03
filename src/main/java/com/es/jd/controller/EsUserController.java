package com.es.jd.controller;


import com.es.jd.bean.*;
import com.es.jd.repository.EsUserRepository;
import com.es.jd.service.EsUserService;
import com.es.jd.temp.entity.EsUserEntity;
import com.es.jd.utils.RandomCodeUtil;
import com.es.jd.utils.ThreadPoolUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/7/30$ 14:35$
 */
@Api(tags = "es用户测试")
@RestController
@RequestMapping("/es/user")
public class EsUserController {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private EsUserRepository esUserRepository;

    @Autowired
    private EsUserService esUserService;


    @ApiOperation("录入测试")
    @PostMapping("/content/test-insert")
    public Long importEsUser(Long num) {

        for (int i = 0; i < num; i++) {
            ThreadPoolUtil.execute(() -> {
                EsUserEntity esUser = generateRandomMockerUser();
                esUserService.importEsUser(esUser);
            });

        }
        return num;
    }

    // mock随机用户数据
    private EsUserEntity generateRandomMockerUser() {
        // 120.247589,30.306362
        EsUserEntity esUserEntity = new EsUserEntity();
        int age = new Random().nextInt(20) + 5;
        esUserEntity.setAge(age);
        boolean flag = age % 2 > 0;
        esUserEntity.setName(flag ? RandomCodeUtil.getRandomChinese("0") : RandomCodeUtil.getRandomChinese("1"));
        esUserEntity.setTags(flag ? "善良|Java|帅气" : "可爱|稳重|React");
        esUserEntity.setUserDesc(flag ? "大闹天宫,南天门守卫, 擅长编程, 烹饪" : "天空守卫,擅长编程,睡觉");
        String latRandNumber = RandomCodeUtil.getRandNumberCode(4);
        String lonRandNumber = RandomCodeUtil.getRandNumberCode(4);
        esUserEntity.setLon(Double.valueOf("120.24" + latRandNumber));
        esUserEntity.setLat(Double.valueOf("30.30" + lonRandNumber));
        return esUserEntity;
    }

    @ApiOperation("更新测试")
    @PostMapping("/content/test-update")
    public Long updateEsUser(@RequestBody EsUserEntity esUser) {
        return esUserService.updateEsUser(esUser);
    }

    @ApiOperation("删除测试")
    @PostMapping("/content/test-delete/{id}")
    public Boolean deleteEsUser(@PathVariable("id") Long id) {
        return esUserService.deleteEsUserById(id);
    }


    @RequestMapping(value = "/save-doc", method = RequestMethod.POST)
    @ApiOperation("保存文档")
    public ESUser saveEsDoc(@RequestBody ESUser esUser) {
        ESUser result = esUserRepository.index(esUser);
        return result;
    }


    @RequestMapping(value = "/exist-doc", method = RequestMethod.GET)
    @ApiOperation("根据名字查询文档")
    public Object existDoc(Long id) {
        return esUserRepository.existsById(id);
    }

    @RequestMapping(value = "/delete-all-doc", method = RequestMethod.GET)
    @ApiOperation("清空所有文档")
    public Object existDoc() {
        esUserRepository.deleteAll();
        return true;
    }

    //---------------- 复杂查询 ------------------

    @RequestMapping(value = "/query-doc/nearBy", method = RequestMethod.POST)
    @ApiOperation("根据坐标点搜索附近的人")
    public Page<PeopleNearByVo> queryNearBy(@RequestBody ESUserLocationSearch locationSearch) {
        return esUserService.queryNearBy(locationSearch);
    }

    @RequestMapping(value = "/query-doc/complex", method = RequestMethod.POST)
    @ApiOperation("根据名字查询文档")
    public Object queryByName(@RequestBody ESUserSearch esUserSearch) {
        String desc = esUserSearch.getDesc();
        List<String> tags = esUserSearch.getTags();
        String name = esUserSearch.getName();
        Integer ageGte = esUserSearch.getAgeGte();
        Integer ageLt = esUserSearch.getAgeLt();
        // 先构建查询条件
        BoolQueryBuilder defaultQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(desc)) {
            defaultQueryBuilder.should(QueryBuilders.termQuery("desc", desc));
        }
        if (StringUtils.isNotBlank(name)) {
            defaultQueryBuilder.should(QueryBuilders.matchQuery("name", name));
        }
        if (!CollectionUtils.isEmpty(tags)) {
            for (String tag : tags) {
                //标签必须全满足
                defaultQueryBuilder.must(QueryBuilders.termQuery("tags", tag));
            }
        }
        if (ageGte != null && ageLt != null) {
            defaultQueryBuilder.filter(QueryBuilders.rangeQuery("age").gte(ageGte).lt(ageLt));
        }
        // 分页条件
        PageRequest pageRequest = PageRequest.of(0, 10);
        // 高亮条件
        HighlightBuilder highlightBuilder = getHighlightBuilder("desc", "tags");
        // 排序条件
        FieldSortBuilder ageSortBuilder = SortBuilders.fieldSort("age").order(SortOrder.ASC);
        ScoreSortBuilder scoreSortBuilder = new ScoreSortBuilder();

        //组装条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(defaultQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(pageRequest)
                .withSort(ageSortBuilder)
                .withSort(scoreSortBuilder)
                .build();

        SearchHits<ESUser> searchHits = elasticsearchRestTemplate.search(searchQuery, ESUser.class);

        // 高亮字段映射
        List<ESUserVo> userVoList = Lists.newArrayList();
        for (SearchHit<ESUser> searchHit : searchHits) {
            ESUser content = searchHit.getContent();
            ESUserVo esUserVo = new ESUserVo();
            BeanUtils.copyProperties(content, esUserVo);
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            for (String highlightField : highlightFields.keySet()) {
                if (StringUtils.equals(highlightField, "tags")) {
                    esUserVo.setHighLightTags(highlightFields.get(highlightField));
                } else if (StringUtils.equals(highlightField, "desc")) {
                    esUserVo.setHighLightDesc(highlightFields.get(highlightField));
                }

            }
            userVoList.add(esUserVo);
        }

        // 组装分页对象
        Page<ESUserVo> userPage = new PageImpl<>(userVoList, pageRequest, searchHits.getTotalHits());

        return userPage;

    }


    // 设置高亮字段
    private HighlightBuilder getHighlightBuilder(String... fields) {
        // 高亮条件
        HighlightBuilder highlightBuilder = new HighlightBuilder(); //生成高亮查询器
        for (String field : fields) {
            highlightBuilder.field(field);//高亮查询字段
        }
        highlightBuilder.requireFieldMatch(false);     //如果要多个字段高亮,这项要为false
        highlightBuilder.preTags("<span style=\"color:red\">");   //高亮设置
        highlightBuilder.postTags("</span>");
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段

        return highlightBuilder;
    }


}
