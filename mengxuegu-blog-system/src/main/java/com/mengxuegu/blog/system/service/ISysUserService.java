package com.mengxuegu.blog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengxuegu.blog.entities.SysUser;
import com.mengxuegu.blog.system.req.RegisterREQ;
import com.mengxuegu.blog.system.req.SysUserCheckPasswordREQ;
import com.mengxuegu.blog.system.req.SysUserREQ;
import com.mengxuegu.blog.system.req.SysUserUpdatePasswordREQ;
import com.mengxuegu.blog.util.base.Result;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 条件分页查询用户列表
     * @param req 条件查询
     * @return
     */
    Result queryPage(SysUserREQ req);

    /**
     * 根据用户id查询该用户拥有的角色id集合
     * @param id
     * @return
     */
    Result findRoleIdsById(String id);

    /**
     * 新增用户角色关系数据到 sys_user_role
     * @param userId 用户id
     * @param roleIds 角色ids集合
     */
    Result saveUserRole(String userId, List<String> roleIds);

    /**
     * 假删除，将 `is_enabled` 状态更新为 `0` 表示删除
     * @param id
     * @return
     */
    Result deleteById(String id);

    /**
     * 校验原密码是否正确
     * @param req
     * @return
     */
    Result checkPassword(SysUserCheckPasswordREQ req);
    /**
     * 修改用户密码
     * @param req
     */
    Result updatePassword(SysUserUpdatePasswordREQ req);

    /**
     * 更新用户信息
     * @return
     */
    Result update(SysUser sysUser);

    /**
     * 统计总用户数
     * @return
     */
    Result getUserTotal();

    /**
     * 校验用户是否存在
     * @return
     */
    Result checkUsername(String username);

    /**
     * 提交用户注册信息
     * @param req
     * @return
     */
    Result register(RegisterREQ req);

    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */
    SysUser findByUsername(String username);

}
