package com.mengxuegu.blog.article.req;

import com.mengxuegu.blog.entities.Article;
import com.mengxuegu.blog.util.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文章查询条件
 * @since 2022-1-22
 * @author wwl
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ArticleREQ对象", description = "文章查询条件")
public class ArticleREQ  extends BaseRequest<Article> {

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "状态(0: 已删除, 1:未审核，2:已审核, 3.未通过)")
    private Integer status;
}
