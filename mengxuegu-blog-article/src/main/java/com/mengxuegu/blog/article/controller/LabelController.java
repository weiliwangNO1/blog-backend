package com.mengxuegu.blog.article.controller;


import com.mengxuegu.blog.article.req.LabelREQ;
import com.mengxuegu.blog.article.service.ILabelService;
import com.mengxuegu.blog.entities.Label;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-21
 */
@Api(value = "标签管理接口", description = "标签管理接口，提供crud操作")
@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private ILabelService labelService;

    @ApiOperation(value = "分页查询标签列表数据")
    @PostMapping(value = "/search")
    public Result queryLabelPage(@RequestBody LabelREQ req) {
        return this.labelService.queryPage(req);
    }

    @ApiOperation(value = "查询标签详情接口")
    @ApiImplicitParam(name = "id", value = "标签id", required = true)
    @GetMapping(value = "/{id}")
    public Result view(@PathVariable("id") String id) {
        return this.labelService.getLabelById(id);
    }

    @ApiOperation(value = "修改标签信息接口")
    @PutMapping
    public Result update(@RequestBody Label label) {
        return this.labelService.updateLabel(label);
    }

    @ApiOperation(value = "新增标签信息接口")
    @PostMapping
    public Result save(@RequestBody Label label) {
        return this.labelService.saveLabel(label);
    }

    @ApiOperation(value = "删除标签接口")
    @ApiImplicitParam(name = "id", value = "标签id", required = true)
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") String id) {
        return this.labelService.deleteLabel(id);
    }

}
