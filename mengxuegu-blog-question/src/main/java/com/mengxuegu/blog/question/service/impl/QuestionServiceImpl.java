package com.mengxuegu.blog.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengxuegu.blog.entities.Label;
import com.mengxuegu.blog.entities.Question;
import com.mengxuegu.blog.feign.IFeignArticleController;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import com.mengxuegu.blog.question.mapper.QuestionMapper;
import com.mengxuegu.blog.question.req.QuestionUserREQ;
import com.mengxuegu.blog.question.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.question.service.IReplayService;
import com.mengxuegu.blog.util.base.BaseRequest;
import com.mengxuegu.blog.util.base.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 问题信息表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-23
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private IReplayService replayService;

    @Autowired
    private IFeignArticleController feignArticleController;

    /**
     * 查询热门回答列表
     * @param req
     * @return
     */
    @Override
    public Result findHotList(BaseRequest<Question> req) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        // 状态为 1 和 2
        wrapper.in("status", Arrays.asList(1, 2));
        // 按回复数降序排列，
        wrapper.orderByDesc("reply");
        // 分页查询热门回答列表
        return Result.ok( baseMapper.selectPage(req.getPage(), wrapper) );
    }

    /**
     * 查询最新问答列表
     * @param req
     * @return
     */
    @Override
    public Result findNewList(BaseRequest<Question> req) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.in("status", Arrays.asList(1, 2)); // 非删除问题
        // 按 问题更新时间 降序排列
        wrapper.orderByDesc("update_date");
        return Result.ok( baseMapper.selectPage(req.getPage(), wrapper) );
    }

    /**
     * 分页查询等待回答列表
     * @param req
     * @return
     */
    @Override
    public Result findWaitList(BaseRequest<Question> req) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.in("status", Arrays.asList(1, 2)); // 非删除问题
        // 查询回复数为0，按 问题创建时间 降序排列
        wrapper.eq("reply", 0);
        wrapper.orderByDesc("create_date");
        return Result.ok( baseMapper.selectPage(req.getPage(), wrapper) );
    }

    /**
     * 根据标签id分页查询问题列表
     * @param req 分页相关的对象
     * @param labelId 标签id
     * @return
     */
    @Override
    public Result findListByLabelId(BaseRequest<Question> req, String labelId) {
        if(StringUtils.isEmpty(labelId)) {
            return Result.ok("标签ID不能为空");
        }
        return Result.ok( baseMapper.findListByLabelId(req.getPage(), labelId) );
    }

    @Override
    public Result findById(String id) {
        // 1. 查询问题详情与标签ids
        Question question = baseMapper.findQuestionAndLabelIdsById(id);
        if(question == null) {
            return Result.error("未查询到相关问题信息");
        }
        // TODO 2. Feign 程调用 Article 微服务查询标签信息
        if(CollectionUtils.isNotEmpty( question.getLabelIds() )){
            List<Label> labelList = feignArticleController.getLabelListByIds(question.getLabelIds());
            question.setLabelList(labelList);  //feign调用获取的标签集合
        }
        return Result.ok(question);
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
        Question question = baseMapper.selectById(id);
        if(question == null) {
            return Result.error("问题不存在");
        }
        question.setViewCount( question.getViewCount() + 1);
        // 不用设置更新时间，更新时间是编辑后才设置
        baseMapper.updateById(question);
        return Result.ok();
    }

    @Transactional
    @Override
    public Result updateOrSave(Question question) {
        // 1. id 不为空，是更新操作
        if (StringUtils.isNotEmpty(question.getId())) {
            // 更新：先删除问题标签中间表数据，再新增到中间表
            baseMapper.deleteQuestionLabel(question.getId());
            // 设置更新时间
            question.setUpdateDate(new Date());
        }
        // 2. 更新或保存到文章信息表（不能放到最后，因为新增后，要返回新增id到question.id里）
        super.saveOrUpdate(question);
        // 3. 新增到文章标签中间表
        if(CollectionUtils.isNotEmpty(question.getLabelIds())) {
                baseMapper.saveQuestionLabel(question.getId(), question.getLabelIds());
            }
        // 响应当前操作的 问题id
        return Result.ok(question.getId());
    }

    /**
     * 假删除，通过 问题id 修改状态为 0 ，表示已删除
     * @param id 问题id
     * @return
     */
    public Result deleteById(String id) {
        return this.updateStatus(id, 0); // 0 已删除
    }

    /**
     * 修改状态
     * @param id 问题id
     * @param status 问题状态
     * @return
     */
    public Result updateStatus(String id, Integer status) {
        Question question = baseMapper.selectById(id);
        question.setStatus(status);
        question.setUpdateDate(new Date());
        baseMapper.updateById(question);
        return Result.ok();
    }

    /**
     * 更新点赞数
     * @param id
     * @param count 有增有减
     * @return
     */
    public Result updateThumhup(String id, int count) {
        if(count != -1 && count != 1) {
            return Result.error("无效操作");
        }
        if(StringUtils.isBlank(id)) {
            return Result.error("无效操作");
        }
        Question question = baseMapper.selectById(id);
        if(question == null) {
            return Result.error("问题不存在");
        }
        if(question.getThumhup() <= 0 && count == -1) {
            return Result.error("无效操作");
        }
        question.setThumhup( question.getThumhup() + count );
        // 不用设置更新时间，更新时间是编辑后才设置
        baseMapper.updateById(question);
        return Result.ok();
    }

    /**
     * 根据用户ID查询问题列表
     * @return
     */
    @Override
    public Result findListByUserId(QuestionUserREQ req) {
        if(StringUtils.isEmpty(req.getUserId())) {
            return Result.error("无效用户信息");
        }
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.in("status", Arrays.asList(1, 2));
        // 根据用户id查询
        wrapper.eq("user_id", req.getUserId());
        // 排序
        wrapper.orderByDesc("update_date");
        return Result.ok( baseMapper.selectPage(req.getPage(), wrapper) );
    }

    /**
     * 查询问题总记录数
     * @return
     */
    @Override
    public Result getQuestionTotal() {
        // 查询总提问数
        QueryWrapper<Question> wrapper = new QueryWrapper();
        wrapper.in( "status", Arrays.asList(1, 2) );
        int total = baseMapper.selectCount(wrapper);
        return Result.ok(total);
    }

    /**
     * 更新问题与回答表中的用户信息
     * @param req
     * @return
     */
    @Override
    public boolean updateUserInfo(UserInfoREQ req) {
        return baseMapper.updateUserInfo(req);
    }


}