package com.mengxuegu.blog.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.blog.article.req.AdvertREQ;
import com.mengxuegu.blog.entities.Advert;
import com.mengxuegu.blog.util.base.Result;

/**
 * <p>
 * 广告信息表 服务类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-23
 */
public interface IAdvertService extends IService<Advert> {

    /**
     * 分页查询广告列表
     * @param req 条件查询
     * @return
     */
    Result queryPage(AdvertREQ req);

    /**
     * 删除广告及OSS图片
     * @param id
     * @return
     */
    Result deleteById(String id);

    /**
     * 查询指定广告位置下的所有广告信息接口，状态是正常的
     * @param position
     * @return
     */
    Result findByPosition(int position);

}
