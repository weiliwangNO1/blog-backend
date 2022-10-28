package com.mengxuegu.blog.article.req;


import com.mengxuegu.blog.entities.Advert;
import com.mengxuegu.blog.util.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 广告查询条件
 * @since  2022-1-23
 * @author wwl
 */
@Data
@Accessors(chain = true)
@ApiModel(value="AdvertREQ对象", description="广告查询条件")
public class AdvertREQ extends BaseRequest<Advert> {

    @ApiModelProperty(value = "广告标题")
    private String title;

    @ApiModelProperty(value="状态(1:正常，0:禁用)")
    private Integer status;
}
