package com.mengxuegu.blog.system.api;

import com.mengxuegu.blog.system.req.RegisterREQ;
import com.mengxuegu.blog.system.service.ISysUserService;
import com.mengxuegu.blog.util.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @since  2022-1-25
 * @author wwl
 */
@Api(value = "用户管理接口", description = "用户管理接口,不用通过身份认证即可调用接口")
@RestController
@RequestMapping("/api/user")
public class ApiSysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @ApiImplicitParam(name = "username", value = "用户名", required = true)
    @ApiOperation("校验用户名是否存在")
    @GetMapping("/username/{username}")
    public Result checkUsername(@PathVariable("username") String username) {
        return sysUserService.checkUsername(username);
    }

    @ApiOperation("注册用户接口")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterREQ req) {
        return sysUserService.register(req);
    }

}
