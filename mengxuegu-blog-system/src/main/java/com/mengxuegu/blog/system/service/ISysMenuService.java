package com.mengxuegu.blog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.blog.entities.SysMenu;
import com.mengxuegu.blog.system.req.SysMenuREQ;
import com.mengxuegu.blog.util.base.Result;

import java.util.List;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 条件查询菜单列表
     * @param req 条件查询
     * @return
     */
    Result queryList(SysMenuREQ req);

    /**
     * 通过菜单资源id删除权限数据
     * @param id 菜单资源id
     * @return
     */
    Result deleteById(String id);

    /**
     * 通过用户id查询拥有的权限菜单树
     * @param userId 用户id
     * @return
     */
    Result findUserMenuTree(String userId);

    /**
     * 通过用户id查询拥有权限
     * @param userId
     * @return
     */
    List<SysMenu> findByUserId(String userId);

}
