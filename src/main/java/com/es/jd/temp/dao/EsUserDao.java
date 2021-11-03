package com.es.jd.temp.dao;

import com.es.jd.temp.entity.EsUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (EsUser)表数据库访问层
 *
 * @author makejava
 * @since 2020-08-10 14:24:00
 */
@Mapper
@Repository
public interface EsUserDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EsUserEntity queryById(Long id);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param esUserEntity 实例对象
     * @return 对象列表
     */
    List<EsUserEntity> queryAll(EsUserEntity esUserEntity);

    /**
     * 新增数据
     *
     * @param esUserEntity 实例对象
     * @return 影响行数
     */
    int insert(EsUserEntity esUserEntity);

    /**
     * 修改数据
     *
     * @param esUserEntity 实例对象
     * @return 影响行数
     */
    int update(EsUserEntity esUserEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}