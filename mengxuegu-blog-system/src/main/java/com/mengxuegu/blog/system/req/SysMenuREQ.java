package com.mengxuegu.blog.system.req;

import com.mengxuegu.blog.entities.SysMenu;
import com.mengxuegu.blog.util.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value="SysMenuREQ对象", description="菜单列表查询条件")
public class SysMenuREQ  implements Serializable {  //extends BaseRequest<SysMenu>  菜单不分页

    @ApiModelProperty(value = "菜单名称")
    private String name;

}

