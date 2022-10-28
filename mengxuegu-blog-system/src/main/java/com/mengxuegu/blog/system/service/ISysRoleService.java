package com.mengxuegu.blog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.blog.entities.SysRole;
import com.mengxuegu.blog.system.req.SysRoleREQ;
import com.mengxuegu.blog.util.base.Result;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 条件分页查询角色列表
     * @param req 条件查询
     * @return
     */
    Result queryPage(SysRoleREQ req);

    /**
     * 1. 通过id删除角色信息表数据
     * 2. 通过id删除角色权限关系表数据
     * @param id 角色id
     * @return
     */
    Result deleteById(String id);

    /**
     * 根据角色id查询此角色拥有的权限菜单 ids
     * @param id 角色id
     */
    Result findMenuIdsById(String id);

    /**
     * 新增角色菜单权限数据到 sys_role_menu
     * @param roleId 角色id
     * @param menuIds 菜单ids集合
     */
    Result saveRoleMenu(String roleId, List<String> menuIds);

}
