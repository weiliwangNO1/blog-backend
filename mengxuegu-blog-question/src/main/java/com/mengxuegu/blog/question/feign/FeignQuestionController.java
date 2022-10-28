package com.mengxuegu.blog.question.feign;

import com.mengxuegu.blog.feign.IFeignQuestionController;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import com.mengxuegu.blog.question.service.IQuestionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "被远程调用的问题微服务接口", description = "被远程调用的问题微服务接口")
@RestController // 不要少了
public class FeignQuestionController implements IFeignQuestionController {

    @Autowired
    private IQuestionService questionService;

    @Override
    public boolean updateUserInfo(UserInfoREQ req) {
        return questionService.updateUserInfo(req);
    }

}
