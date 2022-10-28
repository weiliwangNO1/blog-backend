package com.mengxuegu.blog.article.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mengxuegu.blog.article.req.LabelREQ;
import com.mengxuegu.blog.entities.Label;
import com.mengxuegu.blog.article.mapper.LabelMapper;
import com.mengxuegu.blog.article.service.ILabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.util.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-21
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements ILabelService {


    /**
     * 分页查询标签数据
     * @param req
     * @return
     */
    @Override
    public Result queryPage(LabelREQ req) {
        //分页查询条件
        IPage<Label> page = this.baseMapper.queryPage(req.getPage(), req);
        return Result.ok(page);
    }

    /**
     * 根据标签id获取对应的标签信息
     * @param id
     * @return
     */
    public Result getLabelById(String id) {
        return Result.ok(this.getById(id));
    }

    /**
     * 修改标签信息接口
     * @param label
     * @return
     */
    public Result updateLabel(Label label) {
        label.setUpdateDate(new Date());
        boolean tag = this.updateById(label);
        return tag ? Result.ok() : Result.error("更新失败");
    }

    /**
     * 新增标签信息接口
     * @param label
     * @return
     */
    public Result saveLabel(Label label) {
        boolean tag = this.save(label);
        return tag ? Result.ok() : Result.error("保存失败");
    }

    /**
     * 根据标签id删除标签信息
     * @param id
     * @return
     */
    public Result deleteLabel(String id) {
        boolean tag = this.removeById(id);
        return tag ? Result.ok() : Result.error("删除标签失败");
    }

}
