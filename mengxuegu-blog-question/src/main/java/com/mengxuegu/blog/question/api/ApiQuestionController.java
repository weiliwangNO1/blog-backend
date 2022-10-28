package com.mengxuegu.blog.question.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengxuegu.blog.entities.Question;
import com.mengxuegu.blog.question.service.IQuestionService;
import com.mengxuegu.blog.util.base.BaseRequest;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "问答管理API接口", description = "问答管理API接口，不需要通过身份认证即可调用接口")
@RestController
@RequestMapping("/api/question")
public class ApiQuestionController {

    @Autowired
    private IQuestionService questionService;

    @ApiOperation("分页查询热门问答列表接口")
    @PostMapping("/hot") // /api/question/hot
    public Result findHostList(@RequestBody BaseRequest<Question> req) {
        return questionService.findHotList(req);
    }

    @ApiOperation("分页查询最新问答列表")
    @PostMapping("/new")
    public Result findNewList(@RequestBody BaseRequest<Question> req) {
        System.out.println("2022");
        return questionService.findNewList(req);
    }

    @ApiOperation("分页查询等待回答列表")
    @PostMapping("/wait")
    public Result findWaitList(@RequestBody BaseRequest<Question> req) {
        return questionService.findWaitList(req);
    }

    @ApiImplicitParam(name="labelId", value="标签ID", required=true)
    @ApiOperation("根据标签ID分页查询问答列表接口")
    @PostMapping("/list/{labelId}")
    public Result findListByLabelId(@RequestBody BaseRequest<Question> req,
                                    @PathVariable("labelId") String labelId) {
        return questionService.findListByLabelId(req, labelId);
    }

    @ApiOperation("查询问题详情接口")
    @ApiImplicitParam(name="id", value="问题ID", required=true)
    @GetMapping("/{id}")
    public Result view(@PathVariable("id") String id) {
        return questionService.findById(id);
    }

    /**
     * 更新浏览次数
     * @param id
     * @return
     */
    @ApiImplicitParam(name="id", value="问题ID", required=true)
    @ApiOperation("更新浏览次数")
    @PutMapping("/viewCount/{id}")
    public Result updateViewCount(@PathVariable("id") String id) {
        return questionService.updateViewCount(id);
    }


}
