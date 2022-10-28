package com.mengxuegu.blog.feign;


import com.mengxuegu.blog.entities.Label;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "article-server", path = "/article")
public interface IFeignArticleController {

    @ApiImplicitParam(allowMultiple = true, dataType = "String",
    name = "ids", value = "标签id集合", required = true)
    @ApiOperation(value = "Feign接口-根据标签ids查询对应的标签信息")
    @GetMapping(value = "/api/feign/label/list/{ids}") //@PathVariable一定要指定ids
    public List<Label> getLabelListByIds(@PathVariable("ids") List<String> labelIds);

    @ApiOperation("Feign接口-更新文章表和评论表中的用户信息")
    @PutMapping("/feign/article/user") // 请求方式 put
    boolean updateUserInfo(@RequestBody UserInfoREQ req);

}
