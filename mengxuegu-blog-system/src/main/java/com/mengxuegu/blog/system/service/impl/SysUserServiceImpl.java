package com.mengxuegu.blog.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengxuegu.blog.entities.SysUser;
import com.mengxuegu.blog.feign.IFeignArticleController;
import com.mengxuegu.blog.feign.IFeignQuestionController;
import com.mengxuegu.blog.feign.req.UserInfoREQ;
import com.mengxuegu.blog.system.mapper.SysUserMapper;
import com.mengxuegu.blog.system.req.RegisterREQ;
import com.mengxuegu.blog.system.req.SysUserCheckPasswordREQ;
import com.mengxuegu.blog.system.req.SysUserREQ;
import com.mengxuegu.blog.system.req.SysUserUpdatePasswordREQ;
import com.mengxuegu.blog.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengxuegu.blog.util.base.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private IFeignArticleController feignArticleController;
    @Autowired
    private IFeignQuestionController feignQuestionController;

    @Override
    public Result queryPage(SysUserREQ req) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        // 条件查询
        if(StringUtils.isNotEmpty(req.getUsername())) {
            wrapper.like("username", req.getUsername());
        }
        if(StringUtils.isNotEmpty(req.getMobile())) {
            wrapper.like("mobile", req.getMobile());
        }
        wrapper.orderByDesc("update_date");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    /**
     * 根据用户id查询该用户拥有的角色id集合
     * @param id
     * @return
     */
    @Override
    public Result findRoleIdsById(String id) {
        return Result.ok(baseMapper.findRoleIdsById(id));
    }

    @Transactional //进行事务管理
    @Override
    public Result saveUserRole(String userId, List<String> roleIds) {
        // 1. 先删除用户角色关系表数据
        baseMapper.deleteUserRoleByUserId(userId);
        // 2. 再保存新的用户角色关系数据
        if(CollectionUtils.isNotEmpty(roleIds)) {
            baseMapper.saveUserRole(userId, roleIds);
        }
        return Result.ok();
    }

    @Override
    public Result deleteById(String id) {
        // 将 `is_enabled` 状态更新为 0 表示删除
        SysUser sysUser = baseMapper.selectById(id);
        if(sysUser == null) {
            return Result.error("用户不存在，删除失败");
        }
        sysUser.setIsEnabled(0); // 0 删除，1 可用
        sysUser.setUpdateDate(new Date());
        baseMapper.updateById(sysUser);
        return Result.ok();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Result checkPassword(SysUserCheckPasswordREQ req) {
        if (StringUtils.isEmpty(req.getUserId())) {
            return Result.error("用户ID不能为空,请重试");
        }
        if(StringUtils.isEmpty(req.getOldPassword())) {
            return Result.error("原密码不能为空,请重试");
        }
        SysUser sysUser = baseMapper.selectById(req.getUserId());
        if(sysUser == null) {
            return Result.error("用户不存在,请重试");
        }
        // 不要少了 !
        if(!passwordEncoder.matches(req.getOldPassword(), sysUser.getPassword())) {
            return Result.error("原密码输入错误");
        }
        return Result.ok();
    }

    @Override
    public Result updatePassword(SysUserUpdatePasswordREQ req) {
        if(StringUtils.isEmpty(req.getUserId())) {
            return Result.error("用户ID不能为空,请重试");
        }
        if(StringUtils.isEmpty(req.getNewPassword())) {
            return Result.error("新密码不能为空,请重试");
        }
        if(StringUtils.isEmpty(req.getRepPassword())) {
            return Result.error("确认密码不能为空,请重试");
        }
        if(!req.getNewPassword().equals(req.getRepPassword())) {
            return Result.error("密码不一致,请重试");
        }
        SysUser sysUser = baseMapper.selectById(req.getUserId());
        if(sysUser == null) {
            return Result.error("用户不存在,请重试");
        }
        // 旧密码
        if(StringUtils.isNotEmpty(req.getOldPassword())) {
        // 不要少了 !
            if(!passwordEncoder.matches(req.getOldPassword(), sysUser.getPassword())) {
                return Result.error("原密码输入错误");
            }
        }
        // 新密码加密
        sysUser.setPassword(passwordEncoder.encode(req.getNewPassword()));
        baseMapper.updateById(sysUser);
        return Result.ok();
    }



    @Transactional // 开启事务
    @Override
    public Result update(SysUser sysUser) {
        // 1. 查询原用户信息
        SysUser user = baseMapper.selectById(sysUser.getId());
        if(user == null) {
            return Result.error("更新的用户不存在");
        }
        // 2. 判断更新的信息中昵称和头像是否被改变
        if( !StringUtils.equals(sysUser.getNickName(), user.getNickName())
                || !StringUtils.equals(sysUser.getImageUrl(), user.getImageUrl()) ) {
        // 其中一个不相等，则更新用户信息
        // 2.1 调用文章微服务接口更新用户信息
            UserInfoREQ req =
                    new UserInfoREQ(sysUser.getId(), sysUser.getNickName(),
                            sysUser.getImageUrl());
            feignArticleController.updateUserInfo(req);
        // 2.2 调用问答微服务接口更新用户信息
            feignQuestionController.updateUserInfo(req);
        }
        // 3. 更新用户信息表
        sysUser.setUpdateDate(new Date());
        baseMapper.updateById(sysUser);
        return Result.ok();
    }

    @Override
    public Result getUserTotal() {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        // 帐户是否可用(1 可用，0 删除用户)
        wrapper.eq("is_enabled", 1);
        Integer total = baseMapper.selectCount(wrapper);
        return Result.ok(total);
    }

    @Override
    public Result checkUsername(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        SysUser sysUser = baseMapper.selectOne(wrapper);
        // 查询到则存在，存在 data=true 已被注册，不存在 data=false 未被注册
        return Result.ok( sysUser == null ? false : true );
    }

    @Override
    public Result register(RegisterREQ req) {
        if(StringUtils.isEmpty(req.getUsername())) {
            return Result.error("用户名不能为空，请重试");
        }
        if(StringUtils.isEmpty(req.getPassword())) {
            return Result.error("密码不能为空，请重试");
        }
        if(StringUtils.isEmpty(req.getRepPassword())) {
            return Result.error("确认密码不能为空，请重试");
        }
        if( !StringUtils.equals(req.getPassword(), req.getRepPassword())) {
            return Result.error("两次输入的密码不一致");
        }
        Result result = this.checkUsername(req.getUsername());
        if( (Boolean) result.getData() ) {
            return Result.error("用户已被注册，请更换个用户名");
        }
        // 校验都通过，新增用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUsername( req.getUsername() );
        // 默认昵称和用户名一样
        sysUser.setNickName( req.getUsername() );
        sysUser.setPassword( passwordEncoder.encode(req.getPassword()) );
        // 提交用户信息
        this.save(sysUser);
        return Result.ok();
    }

    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */
    public SysUser findByUsername(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }


}
