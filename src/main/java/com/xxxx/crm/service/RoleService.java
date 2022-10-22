package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    /**
     * 通过用户ID，查询用户所拥有的的角色
     * @param userId
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        //检查参数
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null,"角色名称已经存在！");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //插入数据
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1,"角色插入失败！");
    }

    /**
     * 更新角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        //检查参数
        AssertUtil.isTrue(role.getId() == null,"待更新id不能为空！");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(role.getId()) == null,"待更新数据不存在！");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null && role.getId() != temp.getId(),"角色名称已经存在！");
        role.setUpdateDate(new Date());
        //插入数据
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1,"数据更新失败！");
    }

    /**
     * 删除角色
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        System.out.println("roleId："+roleId);
        AssertUtil.isTrue(roleId == null,"请填写删除的roleId!");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(roleId) == null,"待删除数据不存在！");
        Role role = new Role();
        role.setId(roleId);
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1,"删除用户角色失败！");
    }

    /**
     * 添加角色授权
     * @param roleId
     * @param mIds
     */
    public void addGrant(Integer roleId, Integer[] mIds){
        AssertUtil.isTrue(roleId == null,"参数roleId不能为空！");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(roleId) == null,"角色不存在！");
        // 1. 通过角色ID查询对应的权限记录
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId) != count,"授权删除出现失败！");
            //删除userId相等的所有数据
        }
        //添加权限
        if(mIds.length>0 && mIds != null){
            //定义Permisson集合批量插入数据
            List<Permission> permissionList = new ArrayList<>();

            for(Integer mId:mIds){
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                // 将对象设置到集合中
                permissionList.add(permission);

            }
            //执行批量添加
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList) != permissionList.size(),"授权失败！");
        }
    }
}
