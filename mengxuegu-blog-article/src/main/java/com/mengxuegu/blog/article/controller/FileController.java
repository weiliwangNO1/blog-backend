package com.mengxuegu.blog.article.controller;

import com.mengxuegu.blog.util.aliyun.AliyunUtil;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.enums.PlatformEnum;
import com.mengxuegu.blog.util.properties.BlogProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片文件控制器
 * @since  2022-1-22
 * @author wwl
 */
@Api(value = "文件管理接口", description = "文件管理接口，上传或删除图片文件")
@RequestMapping("/file")
@RestController
public class FileController {

    @Autowired
    private BlogProperties blogProperties;

    @ApiOperation("上传文件到OSS")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        return AliyunUtil.uploadFileToOss(PlatformEnum.ARTICLE, file,
                blogProperties.getAliyun());
    }

    @ApiOperation("删除OOS服务器的文件")
    @ApiImplicitParam(name="fileUrl", value="要删除的文件URL", required=true)
    @DeleteMapping("/delete")
    public Result delete(@RequestParam(value = "fileUrl", required = false) String fileUrl) {
        return AliyunUtil.delete(fileUrl, blogProperties.getAliyun());
    }


}
