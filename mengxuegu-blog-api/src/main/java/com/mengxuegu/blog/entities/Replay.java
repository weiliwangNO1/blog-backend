package com.mengxuegu.blog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 回答信息表
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mxg_replay")
@ApiModel(value="Replay对象", description="回答信息表")
public class Replay implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "-1 表示正常回答，其他值表示是回答的回复")
    private String parentId;

    @ApiModelProperty(value = "回答者id")
    private String userId;

    @ApiModelProperty(value = "回答者用户昵称")
    private String nickName;

    @ApiModelProperty(value = "回答者头像url")
    private String userImage;

    @ApiModelProperty(value = "问题id")
    private String questionId;

    @ApiModelProperty(value = "md问题内容")
    private String mdContent;

    @ApiModelProperty(value = "html问题内容")
    private String htmlContent;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "回答子评论")
    @TableField(exist = false)
    List<Replay> children;



}
