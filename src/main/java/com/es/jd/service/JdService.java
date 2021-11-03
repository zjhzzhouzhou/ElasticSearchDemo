package com.es.jd.service;

import com.es.jd.bean.JdContent;
import com.es.jd.bean.JdSearchRequest;
import com.es.jd.repository.JdContentRepository;
import com.es.jd.utils.HtmlParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述:
 *
 * @Author: zhouzhou
 * @Date: 2020/8/3$ 10:49$
 */
@Service
public class JdService {

    @Autowired
    private JdContentRepository jdContentRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    /**
     * @param keyword
     * @return
     */
    public Boolean parseContent(String keyword) {
        List<JdContent> contents = HtmlParseUtil.parseJdProduct(keyword);
        jdContentRepository.saveAll(contents);
        return true;
    }

    public Page<JdContent> searchContent(JdSearchRequest searchRequest) {

        String pName = searchRequest.getPName();
        int pageNo = searchRequest.getPageNo();
        int pageSize = searchRequest.getPageSize();
        // 初始化起始页
        pageNo = pageNo <= 1 ? 1 : pageNo;
        // 先构建查询条件
        BoolQueryBuilder defaultQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(pName)) {
            defaultQueryBuilder.should(QueryBuilders.termQuery("pName", pName));
        }
        // 分页条件
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize);
        // 高亮条件
        HighlightBuilder highlightBuilder = new HighlightBuilder(); //生成高亮查询器
        highlightBuilder.field("pName");      //高亮查询字段
        highlightBuilder.requireFieldMatch(false);     //如果要多个字段高亮,这项要为false
        highlightBuilder.preTags("<span style=\"color:red\">");   //高亮设置
        highlightBuilder.postTags("</span>");

        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段
        // 排序条件
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("price").order(SortOrder.DESC);
        //组装条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(defaultQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(pageRequest)
                .withSort(sortBuilder).build();

        Page<JdContent> page = jdContentRepository.search(searchQuery);
        SearchHits<JdContent> search = elasticsearchRestTemplate.search(searchQuery, JdContent.class);


        return page;


    }
}
