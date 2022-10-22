package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RoleMapper roleMapper;
    /**
     * 查询用户资源模块
     * @param roleId
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId){
        AssertUtil.isTrue(roleId == null,"参数roleId不能为空！");
        //查询用户角色是否存在
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(roleId) == null,"角色不存在！");
        //查询所有资源目录
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        //查询用户的资源module_id
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        System.out.println("treeModelList："+treeModelList);
        System.out.println("permissionIds："+permissionIds);
        //判断角色是否拥有资源ID
        if(permissionIds != null && permissionIds.size()>0){
            //该角色拥有资源ID
            // 循环所有的资源列表，判断用户拥有的资源ID中是否有匹配的，如果有，则设置checked属性为true
            treeModelList.forEach(treeModel -> {
                //数组包含treeModel.getId()
                if(permissionIds.contains(treeModel.getId())){
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelList;
    }
    public Map<String,Object> queryModuleList(){
        Map<String,Object> map = new HashMap<>();
        //查询资源目录
        List<Module> moduleList = moduleMapper.queryModuleList();
        map.put("code",0);
        map.put("msg","");
        map.put("count",moduleList.size());
        map.put("data",moduleList);
        return map;
    }

    /**
     * 添加资源模块
     1. 参数校验
     *      模块名称 moduleName
     *          非空，同一层级下模块名称唯一
     *      地址 url
     *          二级菜单（grade=1），非空且同一层级下不可重复
     *      父级菜单 parentId
     *          一级菜单（目录 grade=0）    -1
     *          二级|三级菜单（菜单|按钮 grade=1或2）    非空，父级菜单必须存在
     *      层级 grade
     *          非空，0|1|2
     *      权限码 optValue
     *          非空，不可重复
     *  2. 设置参数的默认值
     *      是否有效 isValid    1
     *      创建时间createDate  系统当前时间
     *      修改时间updateDate  系统当前时间
     *  3. 执行添加操作，判断受影响的行数
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
        //参数检查
        // grade
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2),"grade参数不合法！");
        //模块名称
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空！");
        //同一级模块下模块名称不相同
        AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName()) != null,"模块名称已经存在！");
        /**
         * 检查层级菜单
         */
        //一级菜单
        if(grade == 0){
            module.setParentId(-1);
        }
        //二级菜单
        if(grade == 1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"URL不能为空！");
            AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl()) != null,"URL已经存在！");
        }
        // 父级菜单 parentId    二级|三级菜单（菜单|按钮 grade=1或2）    非空，父级菜单必须存在
        if (grade != 0) {
            // 非空
            AssertUtil.isTrue(null == module.getParentId(),"parentId父级菜单不能为空！");
            // 父级菜单必须存在 (将父级菜单的ID作为主键，查询资源记录)
            AssertUtil.isTrue(null == moduleMapper.selectByPrimaryKey(module.getParentId()), "请指定正确的父级菜单！");
        }

        //判断授权码opt_value是否存在
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"授权码不能为空！");
        AssertUtil.isTrue(moduleMapper.queryModuleByOptValue(module.getOptValue()) != null,"授权码已经存在！");
        //设置参数
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        //插入数据
        AssertUtil.isTrue(moduleMapper.insertSelective(module) != 1,"添加资源目录失败！");
    }

    /**
     *  1. 参数校验
     *      id
     *          非空，数据存在
     *      层级 grade
     *          非空 0|1|2
     *      模块名称 moduleName
     *          非空，同一层级下模块名称唯一 （不包含当前修改记录本身）
     *      地址 url
     *          二级菜单（grade=1），非空且同一层级下不可重复（不包含当前修改记录本身）
     *      权限码 optValue
     *          非空，不可重复（不包含当前修改记录本身）
     *  2. 设置参数的默认值
     *      修改时间updateDate  系统当前时间
     *  3. 执行更新操作，判断受影响的行数
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        // 参数检验
        AssertUtil.isTrue(module.getId() == null,"id不能为空！");
        // 通过id查询资源对象
        Module temp = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(temp == null,"待更新数据不存在！");
        AssertUtil.isTrue(module.getIsValid() == null || !(module.getIsValid() == 0 || module.getIsValid() == 1),"isValue为空或参数不合法！");
        //父级id
        AssertUtil.isTrue(module.getParentId() == null,"parentId不能为空！");
        AssertUtil.isTrue(moduleMapper.selectByPrimaryKey(module.getParentId()) == null,"parentId参数不正确！");
        //层级 grade
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2),"grade为空或者不存在！");
        // 模块名称非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空！");
        // 通过层级与模块名称查询资源对象
        temp = moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName());
        if (temp != null) {
            AssertUtil.isTrue(!(temp.getId()).equals(module.getId()), "该层级下菜单名已存在！");
        }
        // 地址 url
        if(grade == 1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"URL不能为空！");
            //通过层级与菜单URL查询资源对象
            temp = moduleMapper.queryModuleByGradeAndUrl(module.getGrade(), module.getUrl());
            AssertUtil.isTrue(temp != null,"该层级下菜单URL已存在！");
        }
        //权限码 optValue
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空！");
        //通过权限码查询是否存在
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(temp != null,"权限码已经存在！");
        //设置参数
        module.setUpdateDate(new Date());
        //执行更新
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) != 1,"资源更新失败！");
    }
}
