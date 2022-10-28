package com.mengxuegu.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengxuegu.blog.article.mapper.CommentMapper;
import com.mengxuegu.blog.article.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.entities.Comment;
import com.mengxuegu.blog.util.base.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论信息表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-22
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Override
    public Result findByArticleId(String articleId){
        if(StringUtils.isBlank(articleId)) {
            return Result.error("文章ID不能为空");
        }
        List<Comment> list = baseMapper.findByArticleId(articleId);
        return Result.ok(list);
    }

    /**
     * 通过评论id递归删除
     * @param id 评论id
     * @return
     */
    @Transactional // 进行事务管理
    @Override
    public Result deleteById(String id) {
        if (StringUtils.isBlank(id)) {
            return Result.error("评论ID不能为空");
        }
        // 要删除的所有评论id
        ArrayList<String> ids = new ArrayList<>();
        //先把要删除的一级评论id放入到集合中
        ids.add(id);
        //递归的子评论 id 加入到集合中
        this.getIds(ids, id);
        //批量删除集合中的id
        baseMapper.deleteBatchIds(ids);
        return Result.ok();
    }

    /**
     * 递归方法
     * @param ids 要删除的所有评论id
     * @param parentId
     */
    private void getIds(List<String> ids, String parentId) {

        //查询子评论的对象
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        List<Comment> commentList = baseMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(commentList)) {
            for(Comment comment: commentList ) {
                String id = comment.getId();
                ids.add(id);
                // 递归继续查询子评论id
                this.getIds(ids, id);
            }
        }
    }


}
