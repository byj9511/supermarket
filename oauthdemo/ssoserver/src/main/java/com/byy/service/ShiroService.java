/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.byy.service;

import com.byy.entity.SysUserEntity;
import com.byy.entity.SysUserTokenEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * shiro相关接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class ShiroService {
    /**
     * 获取用户权限列表
     */
    public Set<String> getUserPermissions(long userId){
        return null;
    };

    public SysUserTokenEntity queryByToken(String token){
        return null;
    };

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    public SysUserEntity queryUser(Long userId){
        return null;
    };
}
