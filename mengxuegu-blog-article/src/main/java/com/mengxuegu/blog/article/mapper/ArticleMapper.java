package com.mengxuegu.blog.article.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mengxuegu.blog.article.req.ArticleListREQ;
import com.mengxuegu.blog.entities.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章信息表 Mapper 接口
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-22
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据文章id查询文章详情
     * @param id
     * @return
     */
    public Article findArticleById(String id);

    /**
     * 通过文章 id 删除文章标签中间表
     * @param articleId 文章id
     * @return
     */
    public boolean deleteArticleLabel(@Param("articleId") String articleId);

    /**
     * 新增文章标签中间表数据
     * @param articleId
     * @param labelIds
     * @return
     */
    public boolean saveArticleLabel(@Param("articleId")String articleId,
                             @Param("labelIds") List<String> labelIds);

    /**
     * 通过分类id或标签id查询文章列表
     * @param page
     * @param req
     * @return
     */
    IPage<Article> findListByLabelIdOrCategoryId(IPage<Article> page, @Param("req")
            ArticleListREQ req);

    /**
     * 更新文章中与评论表中的用户信息
     * @param req
     * @return
     */
    boolean updateUserInfo(UserInfoREQ req);

}
