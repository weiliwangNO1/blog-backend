package com.mengxuegu.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mengxuegu.blog.article.req.ArticleListREQ;
import com.mengxuegu.blog.article.req.ArticleREQ;
import com.mengxuegu.blog.article.req.ArticleUserREQ;
import com.mengxuegu.blog.entities.Article;
import com.mengxuegu.blog.article.mapper.ArticleMapper;
import com.mengxuegu.blog.article.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.enums.ArticleStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 文章信息表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    /**
     * 分页查询文章列表数据
     * @param req
     * @return
     */
    public Result queryArticlePage(ArticleREQ req) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        //标题不为空
        if(StringUtils.isNotBlank(req.getTitle())) {
            wrapper.like("title", req.getTitle().trim());
        }
        //状态不为空
        if(req.getStatus() != null) {
            wrapper.eq("status", req.getStatus());
        }
        wrapper.orderByDesc("update_date");  //按照更新时间倒序
        IPage<Article> page = this.baseMapper.selectPage(req.getPage(), wrapper);
        return Result.ok(page);
    }

    /**
     * 根据文章id查询文章详情数据和标签信息
     * @param id
     * @return
     */
    public Result findArticleAndLabelById(String id) {

        return Result.ok(this.baseMapper.findArticleById(id));
    }

    /**
     * 新增或修改文章信息表与文章标签中间表
     * @return
     */
    @Transactional //事务管理
    @Override
    public Result updateOrSave(Article article) {
        // 1. id 不为空，是更新操作
        if (StringUtils.isNotEmpty(article.getId())) {
        // 更新：先删除文章标签中间表数据，再新增到中间表
            baseMapper.deleteArticleLabel(article.getId());
        // 设置更新时间
            article.setUpdateDate(new Date());
        }
        // 如果文章是不公开的，直接审核通过。否则待审核
        if (article.getIspublic() == 0) {
            article.setStatus(ArticleStatusEnum.SUCCESS.getCode());
        } else {
            article.setStatus(ArticleStatusEnum.WAIT.getCode());
        }
        // 2. 更新或保存到文章信息表（不能放到最后，因为新增后，要返回新增id到article.id里）
        super.saveOrUpdate(article);
        // 3. 新增到文章标签中间表
        if (CollectionUtils.isNotEmpty(article.getLabelIds())) {
            baseMapper.saveArticleLabel(article.getId(), article.getLabelIds());
        }
        // 返回文章id
        return Result.ok(article.getId());
    }

    /**
     * 修改状态：0: 已删除, 1:未审核，2:审核通过，3：审核未通过，
     * @param id 文章id
     * @param statusEnum 文章状态 ArticleStatusEnum
     * @return
     */
    public Result updateStatus(String id, ArticleStatusEnum statusEnum) {
        Article article = baseMapper.selectById(id);
        article.setStatus(statusEnum.getCode());
        article.setUpdateDate(new Date());
        baseMapper.updateById(article);
        return Result.ok();
    }

    /**
     * 根据用户ID查询公开或未公开的文章列表
     * @param req 包含 userId 和 isPublic
     * @return
     */
    @Override
    public Result findListByUserId(ArticleUserREQ req) {
        if(StringUtils.isBlank(req.getUserId())) {
            return Result.error("无效用户信息");
        }
        QueryWrapper<Article> wrapper = new QueryWrapper();
        wrapper.eq("user_id", req.getUserId());
        if( req.getIsPublic() != null) {
        // 注意没有驼峰形式
            wrapper.eq("ispublic", req.getIsPublic());
        }
        // 排序
        wrapper.orderByDesc("update_date");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    /**
     * 更新点赞数
     * @param id
     * @param count
     * @return
     */
    public  Result updateThumhup(String id, int count) {
        if(count != -1 && count != 1) {
            return Result.error("无效操作");
        }
        if(StringUtils.isBlank(id)) {
            return Result.error("无效操作");
        }
        Article article = baseMapper.selectById(id);
        if(article == null) {
            return Result.error("文章不存在");
        }
        if(article.getThumhup() <= 0 && count == -1) {
            return Result.error("无效操作");
        }
        article.setThumhup( article.getThumhup() + count );
        // 不用设置更新时间，更新时间是编辑后才设置
        baseMapper.updateById(article);
        return Result.ok();
    }

    /**
     * 更新浏览次数
     * @param id
     * @return
     */
    @Override
    public Result updateViewCount(String id){
        if(StringUtils.isBlank(id)) {
            return Result.error("无效操作");
        }
        Article article = baseMapper.selectById(id);
        if(article == null) {
            return Result.error("文章不存在");
        }
        article.setViewCount( article.getViewCount() + 1);
        // 不用设置更新时间，更新时间是编辑后才设置
        baseMapper.updateById(article);
        return Result.ok();
    }

    /**
     * 公开且审核通过的文章列表
     * @param req 分类id 或 标签id，分页
     * @return
     */
    @Override
    public Result findListByLabelIdOrCategoryId(ArticleListREQ req) {
    // 查询文章列表
        return Result.ok( baseMapper.findListByLabelIdOrCategoryId(req.getPage(), req) );
    }

    @Override
    public boolean updateUserInfo(UserInfoREQ req) {
        return baseMapper.updateUserInfo(req);
    }



}
