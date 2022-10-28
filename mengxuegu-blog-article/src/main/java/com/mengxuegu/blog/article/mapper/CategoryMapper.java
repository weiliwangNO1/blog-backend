package com.mengxuegu.blog.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengxuegu.blog.entities.Category;

import java.util.List;

/**
 * 文章分类mapper接口
 * @since 2022-1-21
 * @author wwl
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询正常状态下的所有分类和以及分类下所有的标签
     * @return
     */
    public List<Category> findCategoryAndLabel();

}
