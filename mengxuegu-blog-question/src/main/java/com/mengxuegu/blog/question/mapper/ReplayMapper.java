package com.mengxuegu.blog.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengxuegu.blog.entities.Replay;

import java.util.List;

/**
 * <p>
 * 回答信息表 Mapper 接口
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-23
 */
public interface ReplayMapper extends BaseMapper<Replay> {

    /**
     * 通过问题id递归查询所有回答
     * @param questionId 问题id
     * @return
     */
    List<Replay> findByQuestionId(String questionId);

}
