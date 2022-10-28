package com.mengxuegu.blog.system.controller;


import com.mengxuegu.blog.entities.SysMenu;
import com.mengxuegu.blog.system.req.SysMenuREQ;
import com.mengxuegu.blog.system.service.ISysMenuService;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 菜单信息表 前端控制器
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
@Api(value = "菜单管理接口", description = "菜单管理接口，提供crud操作")
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired //不要少了注解
    private ISysMenuService sysMenuService;

    @ApiOperation("根据菜单名称查询列表接口")
    @PostMapping("/search")
    public Result search(@RequestBody SysMenuREQ req) {
        return sysMenuService.queryList(req);
    }

    @ApiOperation("通过菜单ID删除菜单接口")
    @ApiImplicitParam(name="id", value="菜单ID", required=true)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        sysMenuService.deleteById(id);
        return Result.ok();
    }

    @ApiOperation("新增菜单信息接口")
    @PostMapping // 请求地址 /menu
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }
    @ApiOperation("查询菜单详情接口")
    @ApiImplicitParam(name="id", value="菜单ID", required=true)
    @GetMapping("/{id}")
    public Result view(@PathVariable("id") String id){
        return Result.ok(sysMenuService.getById(id));
    }

    @ApiOperation("修改菜单信息接口")
    @PutMapping // 请求地址 /menu
    public Result update(@RequestBody SysMenu sysMenu) {
        // 更新时间
        sysMenu.setUpdateDate(new Date());
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation("通过用户ID查询拥有的权限菜单树")
    @ApiImplicitParam(name="userId", value="用户ID", required=true)
    @GetMapping("/user/{userId}") // /menu/user/1
    public Result findUserMenuTree(@PathVariable("userId") String userId) {
        return sysMenuService.findUserMenuTree(userId);
    }

}
