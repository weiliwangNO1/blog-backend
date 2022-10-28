package com.mengxuegu.blog.article.service;

import com.mengxuegu.blog.article.req.ArticleListREQ;
import com.mengxuegu.blog.article.req.ArticleREQ;
import com.mengxuegu.blog.article.req.ArticleUserREQ;
import com.mengxuegu.blog.entities.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.enums.ArticleStatusEnum;

/**
 * <p>
 * 文章信息表 服务类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-22
 */
public interface IArticleService extends IService<Article> {

    /**
     * 分页查询文章列表数据
     * @param req
     * @return
     */
    public Result queryArticlePage(ArticleREQ req);

    /**
     * 根据文章id查询文章详情数据和标签信息
     * @param id
     * @return
     */
    public Result findArticleAndLabelById(String id);

    /**
     * 修改或新增
     * @param article
     * @return
     */
    Result updateOrSave(Article article);

    /**
     * 修改状态：0: 已删除, 1:未审核，2:审核通过，3：审核未通过，
     * 参考 ArticleStatusEnum
     * @param id 文章id
     * @param statusEnum 文章状态 ArticleStatusEnum
     * @return
     */
    Result updateStatus(String id, ArticleStatusEnum statusEnum);

    /**
     * 根据用户ID查询公开或未公开的文章列表
     * @param req 包含 userId 和 isPublic
     * @return
     */
    Result findListByUserId(ArticleUserREQ req);

    /**
     * 更新点赞数
     * @param id
     * @param count
     * @return
     */
    Result updateThumhup(String id, int count);

    /**
     * 更新浏览次数
     * @param id
     * @return
     */
    Result updateViewCount(String id);

    /**
     * 公开且审核通过的文章列表
     * @param req 分类id 或 标签id，分页
     * @return
     */
    Result findListByLabelIdOrCategoryId(ArticleListREQ req);

    /**
     * 更新文章与评论表中的用户信息
     * @param req
     * @return
     */
    boolean updateUserInfo(UserInfoREQ req);


}
