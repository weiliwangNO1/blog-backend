package com.mengxuegu.blog.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengxuegu.blog.entities.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单信息表 Mapper 接口
 * </p>
 *
 * @author 维利博客-weiliwang.top
 * @since 2022-01-25
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询指定用户ID的所拥有的权限（目录、菜单、按钮）
     * @param userId 用户id
     * @return
     */
    List<SysMenu> findByUserId(@Param("userId") String userId);

}
