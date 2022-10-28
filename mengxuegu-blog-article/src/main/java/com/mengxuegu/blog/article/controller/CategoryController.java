package com.mengxuegu.blog.article.controller;

import com.mengxuegu.blog.article.req.CategoryREQ;
import com.mengxuegu.blog.article.service.ICategoryService;
import com.mengxuegu.blog.entities.Category;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章分类前端控制器
 * @since  2022-1-21
 * @author wwl
 */
@Api(value = "分类管理接口", description = "分类管理接口，提供crud操作")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    /**
     * 分页查询文章分类数据
     * @param req
     * @return
     */
    @ApiOperation(value = "根据分类名称和分类状态分页查询分类数据")
    @PostMapping(value = "/search")
    public Result search(@RequestBody CategoryREQ req) {
        return this.categoryService.queryPage(req);
    }

    /**
     * 通过类别id查询类别详情数据
     * @param id
     * @return
     */
    @ApiOperation(value = "查询类别详情接口")
    @ApiImplicitParam(name = "id" , value = "类别id", required = true, dataType = "String")
    @GetMapping(value = "/{id}")
    public Result view(@PathVariable(value = "id") String id) {
        return Result.ok(this.categoryService.getById(id));
    }

    /**
     * 更新分类信息
     * @param category
     * @return
     */
    @ApiOperation(value = "修改类别信息接口")
    @PutMapping
    public Result update(@RequestBody Category category) {
        return this.categoryService.updateCategoryById(category);
    }

    /**
     * 新增一条分类数据
     * @param category
     * @return
     */
    @ApiOperation(value = "新增一条类别信息接口")
    @PostMapping
    public Result saveCategory (@RequestBody Category category) {
        return this.categoryService.saveCategory(category);
    }

    /**
     * 根据分类id删除分类数据
     * @param id
     * @return
     */
    @ApiOperation(value = "根据分类id删除分类数据")
    @ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public Result deleteCategory(@PathVariable("id") String id) {
        return this.categoryService.deleteCategory(id);
    }

    /**
     * 获取所有正常分类数据
     * @return
     */
    @ApiOperation(value = "获取所有正常分类数据接口")
    @GetMapping(value = "/list")
    public Result findAllNormalCatetory() {
        return this.categoryService.findAllNormalCatetory();
    }

    @ApiOperation(value = "获取所有正常的分类以及所属的所有标签")
    @GetMapping("/label/list")
    public Result findCategoryAndLabel() {
        return this.categoryService.findCategoryAndLabel();
    }

}
