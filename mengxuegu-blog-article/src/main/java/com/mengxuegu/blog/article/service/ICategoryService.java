package com.mengxuegu.blog.article.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.blog.article.req.CategoryREQ;
import com.mengxuegu.blog.entities.Category;
import com.mengxuegu.blog.util.base.Result;

/**
 * 文章分类service
 * @since  2022-1-21
 * @author wwl
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 分页查询文章分类数据
     * @param req
     * @return
     */
    Result queryPage(CategoryREQ req);


    /**
     * 更新文章分类信息
     * @param category
     * @return
     */
    public Result updateCategoryById(Category category);

    /**
     * 新增一条分类数据
     * @param category
     * @return
     */
    public Result saveCategory(Category category);

    /**
     * 根据分类id删除分类数据
     * @param id
     * @return
     */
    public Result deleteCategory(String id);

    /**
     * 获取所有正常状态的分类数据
     * @return
     */
    public Result findAllNormalCatetory();

    /**
     * 获取正常分类以及所属的所有标签
     * @return
     */
    public Result findCategoryAndLabel();


}
