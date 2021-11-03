package com.es.jd.service;

import com.es.jd.bean.ESUser;
import com.es.jd.bean.ESUserLocationSearch;
import com.es.jd.bean.ESUserVo;
import com.es.jd.bean.PeopleNearByVo;
import com.es.jd.config.mq.MqConstant;
import com.es.jd.temp.dao.EsUserDao;
import com.es.jd.temp.entity.EsUserEntity;
import com.es.jd.utils.MqMessageSendUtils;
import com.google.common.collect.Lists;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能描述: Es用户相关Service
 *
 * @Author: zhouzhou
 * @Date: 2020/8/10$ 11:09$
 */
@Service
public class EsUserService {

    @Autowired
    private EsUserDao esUserDao;

    @Autowired
    private MqMessageSendUtils mqMessageSendUtils;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    /**
     * 导入用户
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long importEsUser(EsUserEntity entity) {

        // 第一步首先导入数据库
        int insert = esUserDao.insert(entity);
        // 数据库落库成功后, 发送rocketMq请求,进行ES同步
        if (insert > 0) {
            boolean result = mqMessageSendUtils.sendNormalMessage(MqConstant.Topic.ES_USER_IMPORT, MqConstant.Tag.ES_USER_IMPORT_TAG_INSERT, entity);
            if (result) {
                return entity.getId();
            }
        }
        return null;
    }

    /**
     * 更新用户
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long updateEsUser(EsUserEntity entity) {

        // 第一步首先导入数据库
        int insert = esUserDao.update(entity);
        // 数据库落库成功后, 发送rocketMq请求,进行ES同步
        if (insert > 0) {
            EsUserEntity entity1 = esUserDao.queryById(entity.getId());
            boolean result = mqMessageSendUtils.sendNormalMessage(MqConstant.Topic.ES_USER_IMPORT, MqConstant.Tag.ES_USER_IMPORT_TAG_UPDATE, entity1);
            if (result) {
                return entity.getId();
            }
        }
        return null;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteEsUserById(Long id) {

        // 第一步首先导入数据库
        int delete = esUserDao.deleteById(id);
        // 数据库落库成功后, 发送rocketMq请求,进行ES同步
        if (delete > 0) {
            return mqMessageSendUtils.sendNormalMessage(MqConstant.Topic.ES_USER_IMPORT, MqConstant.Tag.ES_USER_IMPORT_TAG_DELETE, id);
        }
        return null;
    }

    /**
     * 搜索附近的人
     * @param locationSearch
     * @return
     */
    public Page<PeopleNearByVo> queryNearBy(ESUserLocationSearch locationSearch) {

        Integer distance = locationSearch.getDistance();
        Double lat = locationSearch.getLat();
        Double lon = locationSearch.getLon();
        Integer ageGte = locationSearch.getAgeGte();
        Integer ageLt = locationSearch.getAgeLt();
        // 先构建查询条件
        BoolQueryBuilder defaultQueryBuilder = QueryBuilders.boolQuery();
        // 距离搜索条件
        if (distance != null && lat != null && lon != null) {
            defaultQueryBuilder.filter(QueryBuilders.geoDistanceQuery("location")
                    .distance(distance, DistanceUnit.METERS)
                    .point(lat, lon)
            );
        }
        // 过滤年龄条件
        if (ageGte != null && ageLt != null) {
            defaultQueryBuilder.filter(QueryBuilders.rangeQuery("age").gte(ageGte).lt(ageLt));
        }

        // 分页条件
        PageRequest pageRequest = PageRequest.of(0, 10);

        // 地理位置排序
        GeoDistanceSortBuilder sortBuilder = SortBuilders.geoDistanceSort("location", lat, lon);

        //组装条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(defaultQueryBuilder)
                .withPageable(pageRequest)
                .withSort(sortBuilder)
                .build();

        SearchHits<ESUser> searchHits = elasticsearchRestTemplate.search(searchQuery, ESUser.class);
        List<PeopleNearByVo> peopleNearByVos = Lists.newArrayList();
        for (SearchHit<ESUser> searchHit : searchHits) {
            ESUser content = searchHit.getContent();
            ESUserVo esUserVo = new ESUserVo();
            BeanUtils.copyProperties(content, esUserVo);
            PeopleNearByVo peopleNearByVo = new PeopleNearByVo();
            peopleNearByVo.setEsUserVo(esUserVo);
            peopleNearByVo.setDistance((Double) searchHit.getSortValues().get(0));
            peopleNearByVos.add(peopleNearByVo);
        }
        // 组装分页对象
        Page<PeopleNearByVo> peopleNearByVoPage = new PageImpl<>(peopleNearByVos, pageRequest, searchHits.getTotalHits());

        return peopleNearByVoPage;
    }
}
