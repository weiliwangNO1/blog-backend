package com.mengxuegu.blog.util.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * @since  2022-1-22
 * @author wwl
 * OSS配置
 */
@Data
public class AliyunProperties implements Serializable {
    /**
     * 阿里云地域端点
     */
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    /**
     * 存储空间名称
     */
    private String bucketName;
    /**
     * Bucket域名，访问文件时作为URL前缀
     */
    private String bucketDomain;
}

