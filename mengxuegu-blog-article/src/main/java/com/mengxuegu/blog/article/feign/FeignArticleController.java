package com.mengxuegu.blog.article.feign;

import com.mengxuegu.blog.article.service.IArticleService;
import com.mengxuegu.blog.article.service.ILabelService;
import com.mengxuegu.blog.entities.Label;
import com.mengxuegu.blog.feign.IFeignArticleController;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "远程调用的文章微服务接口", description = "远程调用的文章微服务接口")
@RestController
public class FeignArticleController implements IFeignArticleController {

    @Autowired
    private ILabelService labelService;

    @Autowired
    private IArticleService articleService;

    /**
     * 根据标签ids查询对应的标签信息
     * @param labelIds
     * @return
     */
    @Override
    public List<Label> getLabelListByIds(List<String> labelIds) {
        //通过标签ids批量查询标签信息，mybatis-plus提供
        return this.labelService.listByIds(labelIds);
    }

    @Override
    public boolean updateUserInfo(UserInfoREQ req) {
        return articleService.updateUserInfo(req);
    }

}
