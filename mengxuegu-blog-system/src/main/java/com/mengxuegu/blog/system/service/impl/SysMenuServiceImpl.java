package com.mengxuegu.blog.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mengxuegu.blog.entities.SysMenu;
import com.mengxuegu.blog.system.mapper.SysMenuMapper;
import com.mengxuegu.blog.system.req.SysMenuREQ;
import com.mengxuegu.blog.system.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.enums.ResultEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单信息表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public Result queryList(SysMenuREQ req) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper();
        if(StringUtils.isNotEmpty(req.getName())) {
            wrapper.like("name", req.getName());
        }
        // sort 升序，update_date降序
        wrapper.orderByAsc("sort").orderByDesc("update_date");
        // 获取所有菜单
        List<SysMenu> menuList = baseMapper.selectList(wrapper);
        // 封装树状菜单并响应
        return Result.ok( this.buildTree(menuList) );
    }

    private List<SysMenu> buildTree(List<SysMenu> menuList) {
        // 1. 获取根菜单
        List<SysMenu> rootMenuList = new ArrayList<>();
        for (SysMenu menu : menuList) {
        // 如果 m.parentId 等于 0 就是根菜单
            if (menu.getParentId().equals("0")) {
                rootMenuList.add(menu);
            }
        }
        // 2. 根菜单下的子菜单
        for (SysMenu menu : rootMenuList) {
            childrenMenu(menuList, menu);
        }
        // 3. 返回根菜单，因为根菜单中封装了子菜单
        return rootMenuList;
    }

    /**
     * pid=0,id=1 系统管理》pid=1,id=2 用户管理》pid=2 增、删、改、查
     * pid=0,id=1 系统管理》pid=1,id=3 角色管理》pid=3 增、删、改、查
     * 递归获取子菜单，因为子菜单下还会有子菜单
     * @param menuList 所有菜单（包括目录、菜单、按钮）
     * @param menu 父菜单
     * @return
     */
    private SysMenu childrenMenu(List<SysMenu> menuList, SysMenu menu){
        // 封装菜单的 parentId = id 子菜单集合
        List<SysMenu> children = new ArrayList<>();
        // 每次都迭代所有菜单，判断是否为 menu 的子菜单
        for(SysMenu m: menuList) {
        // 如果 m.parentId 等于 id 则就是它的子菜单
            if( m.getParentId().equals( menu.getId() )) {
        // 是子菜单，则递归去找这个菜单的子菜单
                children.add( childrenMenu(menuList, m) );
            }
        }
        // 封装 menu 的子菜单
        menu.setChildren(children);
        // 每有子菜单时返回
        return menu;
    }

    @Transactional // 进行事务管理
    @Override
    public Result deleteById(String id) {
        //删除当前资源
        baseMapper.deleteById(id);
        // 删除 parent_id = id 的子资源
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        baseMapper.delete(wrapper);
        return Result.ok();
    }

    @Override
    public Result findUserMenuTree(String userId) {
        // 1. 通过用户id查询出拥有的权限（目录、菜单、按钮）
        List<SysMenu> menuList = baseMapper.findByUserId(userId);
        // 当userId不存在时，menuList 是空的；当存在此用户，但是没有分配权限时会有一条空记录，
        if (CollectionUtils.isEmpty(menuList)
                || menuList.get(0) == null) {
        // 没有权限
            return Result.build(ResultEnum.MENU_NO);
        }
        // 2. 获取权限集合中所有的目录、菜单
        List<SysMenu> dirMenuList = Lists.newArrayList();
        // 封装权限按钮标识符
        List<String> buttonList = Lists.newArrayList();
        for (SysMenu menu : menuList) {

            if (menu.getType().equals(1) || menu.getType().equals(2)) {
                dirMenuList.add(menu);
            } else {
                // 按钮标识符
                buttonList.add(menu.getCode());
            }
        }
        // 3. 封装树状菜单
        List<SysMenu> menuTreeList = buildTree(dirMenuList);
        // 4. 封装响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("menuTreeList", menuTreeList);
        data.put("buttonList", buttonList);
        return Result.ok(data);  //menuTreeList
    }

    /**
     * 通过用户id查询拥有权限
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> findByUserId(String userId) {
        // 通过用户id查询拥有权限
        List<SysMenu> menuList = baseMapper.findByUserId(userId);
        // 当userId不存在时，menuList 是空的；当存在此用户，但是没有分配权限时会有一条空记录，
        if (CollectionUtils.isEmpty(menuList)
                || menuList.get(0) == null) {
            // 没有权限
            return null;
        }
        return menuList;
    }

}
