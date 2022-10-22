package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    /**
     * 查询所有的用户角色(只需要id和name)
     * List<Role>
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    /**
     * 通过名字查询
     * @param roleName
     * @return
     */
    public Role selectByRoleName(String roleName);
}