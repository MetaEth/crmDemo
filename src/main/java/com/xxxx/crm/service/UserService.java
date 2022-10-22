package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
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
public class UserService  extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 添加用户
     *  1. 参数校验
     *      用户名userName     非空，唯一性
     *      邮箱email          非空
     *      手机号phone        非空，格式正确
     *  2. 设置参数的默认值
     *      isValid           1
     *      createDate        系统当前时间
     *      updateDate        系统当前时间
     *      默认密码            123456 -> md5加密
     *  3. 执行添加操作，判断受影响的行数
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //检验参数
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone());
        //检验用户名是否存在
        User temp = userMapper.queryUserByName(user.getUserName());
        AssertUtil.isTrue(temp != null,"用户名已被使用，请更改用户名！");
        //默认密码
        user.setUserPwd("123456");
        //设置状态
        user.setIsValid(1);
        //时间
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //插入数据
        System.out.println("主键id:"+user.getId());
        AssertUtil.isTrue(userMapper.insertSelective(user) != 1,"添加用户失败！");
        /**
         * 执行关联管理
         * user.getId是插入数据返回的id
         */
        relationUserRole(user.getId(),user.getRoleIds());
    }

    private void relationUserRole(Integer userId, String roleIds) {
        // 通过用户ID查询角色记录
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        if(count > 0){
            // 如果角色记录存在，则删除该用户对应的角色记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"用户角色删除失败！");
        }
        System.out.println("count:"+count);
        // 判断角色记录是否存在
        if(!StringUtils.isBlank(roleIds)){
            // 将用户角色数据设置到集合中，执行批量添加
            List<UserRole> userRoleList = new ArrayList<>();
            //将角色ID字符串转换成数组
            String[] roleIdsArray = roleIds.split(",");
            //遍历数组，得到对应的用户角色对象，并设置到集合中
            for(String roleId:roleIdsArray){
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                //添加集合
                userRoleList.add(userRole);
            }
            // 批量添加用户角色记录
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != roleIdsArray.length,"用户角色插入失败!");
        }


    }

    /**
     * 更新用户
     *  1. 参数校验
     *      判断用户ID是否为空，且数据存在
     *      用户名userName     非空，唯一性
     *      邮箱email          非空
     *      手机号phone        非空，格式正确
     *  2. 设置参数的默认值
     *      updateDate        系统当前时间
     *  3. 执行更新操作，判断受影响的行数
     * @param user
     */
    /**
     * 删除用户
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids){
        AssertUtil.isTrue(ids == null || ids.length == 0,"待删除记录不存在！");
        //执行删除
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length,"用户删除出现失败！");
        // 遍历用户ID的数组
        for(Integer userId:ids){
            // 通过用户ID查询对应的用户角色记录
            Integer count  = userRoleMapper.countUserRoleByUserId(userId);
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!= count,"用户角色删除失败！");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //检验更新的id
        AssertUtil.isTrue(null == user.getId(), "待更新id不能为空");
        AssertUtil.isTrue(userMapper.selectByPrimaryKey(user.getId()) == null,"待更新记录不存在！");
        //检验参数
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone());
        //检查用户名是否一致
        User temp = userMapper.queryUserByName(user.getUserName());
        AssertUtil.isTrue(temp != null && user.getId() != temp.getId(),"用户名已被使用，请更改用户名！");
        user.setUpdateDate(new Date());
        //执行更新
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1,"用户更新失败！");
        /**
         * 执行关联管理
         * user.getId是插入数据返回的id
         */
        relationUserRole(user.getId(),user.getRoleIds());
    }
    /**
     * 添加用户参数检验
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParams(String userName, String email, String phone) {
        //检验用户名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //检验邮箱
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空！");
        //检验号码
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号码不能为空！");
        //检验手机号码格式
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正确！");
    }

    /**
     * 用户登入
     * @param userName
     * @param userPwd
     */
    public UserModel userLogin(String userName,String userPwd){
        //1,判断用户参数是否为空
        checkLoginParams(userName,userPwd);
        //2,查询用户是否存在
        User user = userMapper.queryUserByName(userName);
        //3,判断用户对象是否为空
        AssertUtil.isTrue(user == null,"用户姓名不正确");
        //4,判断密码是否正确
        checkUserPwd(userPwd,user.getUserPwd());
        //5,返回用户构建对象
        return buildUserInfo(user);
    }

    /**
     * 登录成功的用户对象
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //加密userIdStr
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 密码判断
     * @param userPwd
     * @param userPwd1
     */
    private void checkUserPwd(String userPwd, String userPwd1) {
        //将客户端的密码加密
        userPwd = Md5Util.encode(userPwd);
        //判断密码是否正确
        AssertUtil.isTrue(!userPwd.contentEquals(userPwd1),"用户密码不正确");
    }

    /**
     * 验证用户名和密码
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName,String userPwd) {
        System.out.println("用户名:"+userName+",密码:"+userPwd);
        //验证用户名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }
    //添加事务
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId, String oldPwd, String newPwd, String repeatPwd){
        //通过用户id查询用户的记录，返回对象
        User user = userMapper.selectByPrimaryKey(userId);
        //判断用户记录是否存在
        AssertUtil.isTrue(user == null,"待更新记录不存在！");
        //密码检验参数
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        //设置用户新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //更新用户密码
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "修改密码失败！");
    }

    /**
     * 密码参数检验
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空！");
        //判断原始密码是否正确
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确！");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空！");
        //判断新密码与原始密码是否一致（不允许新密码和原始密码一致）
        AssertUtil.isTrue(user.getUserPwd().equals(Md5Util.encode(newPwd)),"新密码不能与原始密码相同！");
        //判断确认密码是否为空
        AssertUtil.isTrue(!StringUtils.isBlank(repeatPwd),"确认密码不能为空！");
        //判断确认密码和新密码是否一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致！");
    }
}
