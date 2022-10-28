package com.mengxuegu.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.article.mapper.CategoryMapper;
import com.mengxuegu.blog.article.req.CategoryREQ;
import com.mengxuegu.blog.article.service.ICategoryService;
import com.mengxuegu.blog.entities.Category;
import com.mengxuegu.blog.util.base.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    /**
     * 分页查询文章分类数据
     * @param req
     * @return
     */
    @Override
    public Result queryPage(CategoryREQ req) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(req.getName())) {
            // 文章名称不为空，第一个参数维数据库列名，第二个为需要查询的数据
            wrapper.like("name", req.getName().trim());
        }
        if(req.getStatus() != null) {
            //文章状态不为空
            wrapper.eq("status", req.getStatus());
        }
        //排序
        wrapper.orderByDesc("status").orderByAsc("sort");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    /**
     * 更新文章分类信息
     * @param category
     * @return
     */
    public Result updateCategoryById(Category category) {
        //更新时间
        category.setUpdateDate(new Date());
        this.updateById(category);
        return Result.ok();
    }

    /**
     * 新增一条分类数据
     * @param category
     * @return
     */
    public Result saveCategory(Category category) {
        boolean tag = this.save(category);
        return tag ? Result.ok() : Result.error("新增失败");
    }

    /**
     * 根据分类id删除分类数据
     * @param id
     * @return
     */
    public Result deleteCategory(String id) {
        boolean tag = this.removeById(id);
        return tag ? Result.ok() : Result.error("删除失败");
    }

    /**
     * 获取所有正常状态的分类数据
     * @return
     */
    public Result findAllNormalCatetory() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);  //正常状态的分类数据，1.正常，0.禁用
        return Result.ok(this.baseMapper.selectList(wrapper));
    }

    /**
     * 获取正常分类以及所属的所有标签
     * @return
     */
    public Result findCategoryAndLabel() {

        return Result.ok(this.baseMapper.findCategoryAndLabel());
    }

}
