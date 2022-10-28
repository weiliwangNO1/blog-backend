package com.mengxuegu.blog.question.controller;


import com.mengxuegu.blog.entities.Replay;
import com.mengxuegu.blog.question.service.IReplayService;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 回答信息表 前端控制器
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-23
 */
@RestController
@RequestMapping("/replay")
public class ReplayController {

    @Autowired
    private IReplayService replayService;

    @ApiImplicitParam(name="id", value="回答评论ID", required=true)
    @ApiOperation("删除回答评论接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        return replayService.deleteById(id);
    }

    @ApiOperation("新增回答信息接口")
    @PostMapping
    public Result add(@RequestBody Replay replay){
        return replayService.add(replay);
    }


}
