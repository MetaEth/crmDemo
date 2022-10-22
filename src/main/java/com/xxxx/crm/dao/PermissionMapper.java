package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    //通过用户id查询权限分配
    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);
    //通过角色roleId查询数量
    Integer countPermissionByRoleId(Integer roleId);
    //通过角色roleId删除
    Integer deletePermissionByRoleId(Integer roleId);
    // 通过用户ID查询对应的资源列表（资源权限码）
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);
}