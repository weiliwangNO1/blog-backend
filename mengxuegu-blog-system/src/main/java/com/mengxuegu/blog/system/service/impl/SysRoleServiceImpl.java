package com.mengxuegu.blog.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengxuegu.blog.entities.SysRole;
import com.mengxuegu.blog.system.mapper.SysRoleMapper;
import com.mengxuegu.blog.system.req.SysRoleREQ;
import com.mengxuegu.blog.system.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.util.base.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public Result queryPage(SysRoleREQ req) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper();
        // 条件查询
        if(StringUtils.isNotEmpty(req.getName())) {
            wrapper.like("name", req.getName());
        }
        wrapper.orderByAsc("update_date");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    @Transactional // 事务管理
    @Override
    public Result deleteById(String id) {
        // 1. 通过角色 id 删除角色信息表数据
        baseMapper.deleteById(id);
        // 2. 通过角色 id 删除角色菜单关系表数据
        baseMapper.deleteRoleMenuByRoleId(id);
        return Result.ok();
    }

    @Override
    public Result findMenuIdsById(String id) {
        return Result.ok(baseMapper.findMenuIdsById(id));
    }

    @Override
    public Result saveRoleMenu(String roleId, List<String> menuIds) {
        // 1. 先删除角色菜单关系表数据
        baseMapper.deleteRoleMenuByRoleId(roleId);
        // 2. 再保存新的角色菜单关系表数据
        if(CollectionUtils.isNotEmpty(menuIds)) {
            baseMapper.saveRoleMenu(roleId, menuIds);
        }
        return Result.ok();
    }


}
