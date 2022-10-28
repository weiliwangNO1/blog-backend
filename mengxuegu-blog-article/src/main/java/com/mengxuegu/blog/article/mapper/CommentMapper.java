package com.mengxuegu.blog.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengxuegu.blog.entities.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论信息表 Mapper 接口
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-22
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 通过文章id级联查询所有回复信息
     * @param articleId
     * @return
     */
    List<Comment> findByArticleId(@Param("articleId") String articleId);


}
