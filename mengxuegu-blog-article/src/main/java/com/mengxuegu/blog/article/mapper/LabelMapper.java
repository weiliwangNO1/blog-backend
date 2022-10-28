package com.mengxuegu.blog.article.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mengxuegu.blog.article.req.LabelREQ;
import com.mengxuegu.blog.entities.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-21
 */
public interface LabelMapper extends BaseMapper<Label> {

    /**
     * 分页查询标签数据
     * @param page
     * @param req
     * @return
     */
    IPage<Label> queryPage(IPage<Label> page, @Param("req") LabelREQ req);

}
