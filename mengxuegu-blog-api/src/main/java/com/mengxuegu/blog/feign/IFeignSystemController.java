package com.mengxuegu.blog.feign;

import com.mengxuegu.blog.entities.SysMenu;
import com.mengxuegu.blog.entities.SysUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// value 指定是哪个微服务接口， path 是在 Feign 调用时会加上此前缀，对应实现类服务的 context-path
@FeignClient(value="system-server", path = "/system")
public interface IFeignSystemController {

    @ApiImplicitParam(name="username", value="用户名", required=true)
    @ApiOperation("Feign接口-通过用户名查询用户信息")
    @GetMapping("/api/feign/user/{username}")
    SysUser findUserByUsername(@PathVariable("username") String username);

    @ApiImplicitParam(name="username", value="用户ID", required=true)
    @ApiOperation("Feign接口-通过用户id查询拥有权限")
    @GetMapping("/api/feign/menu/{userId}")
    List<SysMenu> findMenuByUserId(@PathVariable("userId") String userId);

}
