package com.mengxuegu.blog.question.controller;


import com.mengxuegu.blog.entities.Question;
import com.mengxuegu.blog.question.req.QuestionUserREQ;
import com.mengxuegu.blog.question.service.IQuestionService;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 问题信息表 前端控制器
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-23
 */
@Api(value = "问题管理接口", description = "问题管理接口，提供crud操作")
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @ApiOperation("修改问题信息接口")
    @PutMapping
    public Result update(@RequestBody Question question) {
        return questionService.updateOrSave(question);
    }

    @ApiOperation("新增问题信息接口")
    @PostMapping
    public Result save(@RequestBody Question question) {
        return questionService.updateOrSave(question);
    }

    @ApiImplicitParam(name="id", value="问题ID", required=true)
    @ApiOperation("删除问题接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        return questionService.deleteById(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value="问题ID", required=true),
            @ApiImplicitParam(name="count", value="点赞数", required=true),
    })
    @ApiOperation("更新点赞数")
    @PutMapping("/thumb/{id}/{count}")
    public Result updateThumhup(@PathVariable("id") String id,
                                @PathVariable("count") int count) {
        return questionService.updateThumhup(id, count);
    }

    @ApiOperation("根据用户ID查询问题列表")
    @PostMapping("/user")
    public Result findListByUserId(@RequestBody QuestionUserREQ req) {
        return questionService.findListByUserId(req);
    }

    @ApiOperation("查询提问总记录数")
    @GetMapping("/total")
    public Result questionTotal() {

        //获取从Securtiy上下文中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户详情数据
        OAuth2AuthenticationDetails detail = (OAuth2AuthenticationDetails) authentication.getDetails();
        System.out.println("detail:" + detail);

        return questionService.getQuestionTotal();
    }

}
