package com.mengxuegu.blog.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengxuegu.blog.entities.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户id查询此用户拥有的角色ids
     * @param id 用户id
     * @return 角色 id 集合
     */
    List<String> findRoleIdsById(@Param("id") String id);

    /**
     * 通过用户 id 删除用户角色关系表数据
     * @param userId 用户id
     * @return
     */
    boolean deleteUserRoleByUserId(@Param("userId") String userId);
    /**
     * 新增用户角色关系数据到 sys_user_role
     * @param userId 用户id
     * @param roleIds 角色ids集合
     * @return
     */
    boolean saveUserRole(@Param("userId") String userId, @Param("roleIds") List<String>
            roleIds);


}
