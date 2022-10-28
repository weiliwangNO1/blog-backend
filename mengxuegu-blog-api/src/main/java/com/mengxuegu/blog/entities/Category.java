package com.mengxuegu.blog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文章分类表
 * @author wwl
 * @since  2022-1-21
 */
@ApiModel(value = "Category对象", description = "类别信息表")
@Data   //lombok插件
@TableName("mxg_category")  //mybatis-plus注解，对应数据库表名
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态(1.正常，0.禁用)
     */
    @ApiModelProperty(value = "状态(1.正常，0.禁用)")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "分类下的标签集合")
    @TableField(exist = false)  //表示该字段不在该表里
    private List<Label> labelList;

}
