package com.mengxuegu.blog.article.controller;


import com.mengxuegu.blog.article.req.ArticleREQ;
import com.mengxuegu.blog.article.req.ArticleUserREQ;
import com.mengxuegu.blog.article.service.IArticleService;
import com.mengxuegu.blog.entities.Article;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.enums.ArticleStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章信息表 前端控制器
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-22
 */
@Api(value = "文章管理接口", description = "文章管理接口，提供crud操作")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    /**
     * 分页查询文章数据
     * @param req
     * @return
     */
    @ApiOperation(value = "分页查询文章数据")
    @PostMapping(value = "/search")
    public Result queryArticlePage(@RequestBody ArticleREQ req) {
        return this.articleService.queryArticlePage(req);
    }

    /**
     * 根据文章id查询文章详情和标签信息
     * @param id
     * @return
     */
    @ApiOperation(value = "据文章id查询文章详情和标签信息")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    @GetMapping(value = "/{id}")
    public Result findArticleAndLabelById(@PathVariable("id") String id) {
        return this.articleService.findArticleAndLabelById(id);
    }

    @ApiOperation("修改文章信息接口")
    @PutMapping
    public Result updateArticle(@RequestBody Article article) {
        return articleService.updateOrSave(article);
    }

    @ApiOperation("新增文章信息接口")
    @PostMapping
    public Result saveArticle(@RequestBody Article article) {
        return articleService.updateOrSave(article);
    }

    @ApiImplicitParam(name="id", value="文章ID", required=true)
    @ApiOperation("删除文章接口")
    @DeleteMapping("/{id}")
    public Result deleteArticle(@PathVariable("id") String id) {
    // 假删除，通过文章 id 修改状态为 0 ，表示已删除
        return articleService.updateStatus(id, ArticleStatusEnum.DELETE);
    }

    @ApiImplicitParam(name="id", value="文章ID", required=true)
    @ApiOperation("文章审核通过接口")
    @GetMapping(value = "/audit/success/{id}")
    public Result successArticle(@PathVariable("id") String id) {
        return articleService.updateStatus(id, ArticleStatusEnum.SUCCESS);
    }

    @ApiImplicitParam(name="id", value="文章ID", required=true)
    @ApiOperation("文章审核不通过接口")
    @GetMapping(value = "/audit/fail/{id}")
    public Result failArticle(@PathVariable("id") String id) {
        return articleService.updateStatus(id, ArticleStatusEnum.FAIL);
    }


    @ApiOperation("根据用户ID查询公开或未公开的文章列表")
    @PostMapping("/user")
    public Result findListByUserId(@RequestBody ArticleUserREQ req) {
        return articleService.findListByUserId(req);
    }

    /**
     * 更新点赞数
     * @param id
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value="文章ID", required=true),
            @ApiImplicitParam(name="count", value="点赞数", required=true),
    })
    @ApiOperation("更新点赞数")
    @PutMapping("/thumb/{id}/{count}")
    public Result updateThumhup(@PathVariable("id") String id,
                                @PathVariable("count") int count) {
        return articleService.updateThumhup(id, count);
    }



}
