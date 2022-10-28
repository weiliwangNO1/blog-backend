package com.mengxuegu.blog.article.api;

import com.mengxuegu.blog.article.req.ArticleListREQ;
import com.mengxuegu.blog.article.service.IArticleService;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "文章管理API接口", description = "文章管理API接口，不需要通过身份认证就可以访问")
@RequestMapping("/api/article")
@RestController
public class ApiArticleController {

    @Autowired
    private IArticleService articleService;

    /**
     * 根据文章id查询文章详情和标签信息
     * @param id
     * @return
     */
    @ApiOperation(value = "据文章id查询文章详情和标签信息")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    @GetMapping(value = "/{id}")
    public Result findArticleAndLabelById(@PathVariable("id") String id) {
        Result r = this.articleService.findArticleAndLabelById(id);
        System.out.println(r);
        return r;
        //return this.articleService.findArticleAndLabelById(id);
    }

    /**
     * 更新浏览次数
     *
     * @param id
     * @return
     */
    @ApiImplicitParam(name = "id", value = "文章ID", required = true)
    @ApiOperation("更新浏览次数")
    @PutMapping("/viewCount/{id}")
    public Result updateViewCount(@PathVariable("id") String id) {
        return articleService.updateViewCount(id);
    }

    @ApiOperation("公开且已审核的文章列表接口")
    @PostMapping("/list")
    public Result list(@RequestBody ArticleListREQ req) {
        return articleService.findListByLabelIdOrCategoryId(req);
    }


}
