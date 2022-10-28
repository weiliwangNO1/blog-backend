package com.mengxuegu.blog.article.api;

import com.mengxuegu.blog.article.service.ICategoryService;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="分类管理API接口",description = "分类管理API接口，公开接口")
@RestController
@RequestMapping("/api/category")
public class ApiCategoryController {

    @Autowired
    private ICategoryService categoryService;

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
