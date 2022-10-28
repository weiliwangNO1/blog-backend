package com.mengxuegu.blog.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengxuegu.blog.article.mapper.AdvertMapper;
import com.mengxuegu.blog.article.req.AdvertREQ;
import com.mengxuegu.blog.article.service.IAdvertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.entities.Advert;
import com.mengxuegu.blog.util.aliyun.AliyunUtil;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.properties.BlogProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 广告信息表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-23
 */
@Service
public class AdvertServiceImpl extends ServiceImpl<AdvertMapper, Advert> implements IAdvertService {

    /**
     * 分页查询广告列表信息
     * @param req 条件查询
     * @return
     */
    @Override
    public Result queryPage(AdvertREQ req) {
        QueryWrapper<Advert> wrapper = new QueryWrapper<>();
        if(req.getStatus() != null) {
            wrapper.eq("status", req.getStatus());
        }
        if(StringUtils.isNotEmpty(req.getTitle())) {
            wrapper.like("title", req.getTitle());
        }
        wrapper.orderByDesc("status").orderByAsc("sort");
        // 分页对象
        return Result.ok(this.baseMapper.selectPage(req.getPage(), wrapper));
    }

    @Autowired
    private BlogProperties blogProperties;


    /**
     * 根据广告id删除广告
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Result deleteById(String id) {
        // 1. 先通过广告id查询图片url
        String imageUrl = baseMapper.selectById(id).getImageUrl();
        // 2. 先删除表中的广告信息
        baseMapper.deleteById(id);
        // 3. 删除oss上的图片
        if (StringUtils.isNotEmpty(imageUrl)) {
            AliyunUtil.delete(imageUrl, blogProperties.getAliyun());
        }
        return Result.ok();
    }

    /**
     * 查询指定广告位置下的所有广告信息接口，状态是正常的
     * @param position
     * @return
     */
    @Override
    public Result findByPosition(int position) {
        QueryWrapper<Advert> wrapper = new QueryWrapper<>();
        wrapper.eq("position", position);
        wrapper.eq("status", 1); // 正常
        wrapper.orderByAsc("sort"); // 升序
        return Result.ok(baseMapper.selectList(wrapper));
    }


}
