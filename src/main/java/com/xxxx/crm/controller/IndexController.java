package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private PermissionMapper permissionMapper;
    /**
     * 系统登入
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /**
     * 系统欢迎界面
     * @return
     */
    @RequestMapping("welcome")
    public String weclome(){
        return "welcome";
    }

    /**
     * 后端管理界面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //获取cookie中用户的Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象，设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);
        //查询用户的授权码并设置到session作用域
        List<String> permissions = permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
        // 将集合设置到session作用域中
        request.getSession().setAttribute("permissions", permissions);
        /**
         * 测试获取session里面的数据
         */
        HttpServletRequest hsrequest = (HttpServletRequest)request;
        HttpSession session = hsrequest.getSession(true);
        //通过session获取数据
        String[] names = session.getValueNames();
        for(int i = 0;i < names.length;i++){
            System.out.println("session数据-->"+names[i] + "：" + session.getValue(names[i]));
        }
        return "main";
    }
}
