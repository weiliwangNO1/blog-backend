package com.mengxuegu.blog.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mengxuegu.blog.article.req.LabelREQ;
import com.mengxuegu.blog.entities.Label;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.blog.util.base.Result;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-21
 */
public interface ILabelService extends IService<Label> {

    /**
     * 分页查询标签数据
     * @param req
     * @return
     */
    public Result queryPage(LabelREQ req);

    /**
     * 根据标签id获取对应的标签信息
     * @param id
     * @return
     */
    public Result getLabelById(String id);

    /**
     * 修改标签信息接口
     * @param label
     * @return
     */
    public Result updateLabel(Label label);

    /**
     * 新增标签信息接口
     * @param label
     * @return
     */
    public Result saveLabel(Label label);

    /**
     * 根据标签id删除标签信息
     * @param id
     * @return
     */
    public Result deleteLabel(String id);

}
