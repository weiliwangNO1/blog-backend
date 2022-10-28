package com.mengxuegu.blog.system.controller;


import com.mengxuegu.blog.entities.SysRole;
import com.mengxuegu.blog.system.req.SysRoleREQ;
import com.mengxuegu.blog.system.service.ISysRoleService;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
@Api(value = "角色管理接口", description = "角色管理接口，提供crud操作")
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @ApiOperation(value = "根据角色名称查询列表接口")
    @PostMapping("/search")
    public Result search(@RequestBody SysRoleREQ req) {
        return sysRoleService.queryPage(req);
    }

    @ApiOperation("新增角色信息接口")
    @PostMapping // 请求地址 /role
    public Result save(@RequestBody SysRole sysRole) {
        sysRoleService.save(sysRole);
        return Result.ok();
    }

    @ApiOperation("查询角色详情接口")
    @ApiImplicitParam(name="id", value="角色ID", required=true)
    @GetMapping("/{id}")
    public Result view(@PathVariable("id") String id){
        return Result.ok(sysRoleService.getById(id));
    }
    @ApiOperation("修改角色信息接口")
    @PutMapping // 请求地址 /role
    public Result update(@RequestBody SysRole sysRole) {
        // 更新时间
        sysRole.setUpdateDate(new Date());
        sysRoleService.updateById(sysRole);
        return Result.ok();
    }


    @ApiOperation("通过角色ID删除角色接口")
    @ApiImplicitParam(name="id", value="角色ID", required=true)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id){
        return sysRoleService.deleteById(id);
    }

    @ApiOperation("根据角色id查询拥有的菜单ids接口")
    @ApiImplicitParam(name="id", value="角色ID", required=true)
    @GetMapping("/{id}/menu/ids") // 请求路径 /role/{id}/menu/ids
    public Result findMenuIdsById(@PathVariable("id") String id) {
        return sysRoleService.findMenuIdsById(id);
    }

    // allowMultiple=true 表示数组格式的参数,dataType="String" 表示数组中参数的类型
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value="角色ID", required=true),
            @ApiImplicitParam(name="menuIds", value="菜单id集合",
                    required=true, allowMultiple=true, dataType = "String")
    })
    @ApiOperation("新增角色菜单权限数据接口")
    @PostMapping("/{id}/menu/save")
    public Result saveRoleMenu(@PathVariable("id") String id, @RequestBody List<String>
            menuIds) {
        return sysRoleService.saveRoleMenu(id, menuIds);
    }

}
