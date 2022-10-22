package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 多条件营销机会查询
     * 如果flag的值不为空，且值为1，则表示当前查询的是客户开发计划；否则查询营销机会数据
     * @param saleChanceQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){
        //如果flag的值不为空，且值为1，则表示当前查询的是客户开发计划；否则查询营销机会数据
//        if(flag != null && flag ==1){
//            saleChanceQuery.setState(StateStatus.STATED.getType());
//            // 设置指派人（当前登录用户的ID）
//            // 从cookie中获取当前登录用户的ID
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            saleChanceQuery.setAssignMan(userId);
//        }
//
//        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
        return saleChanceService.queryByParamsForTable(saleChanceQuery);
    }
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    /**
     *添加营销机会计划
     * @param saleChance
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(HttpServletRequest request,SaleChance saleChance){
        // 从Cookie中获取当前登录的用户名
        String userName = CookieUtil.getCookieValue(request,"userName");
        System.out.println("Cookie name:"+userName);
        System.out.println("对象："+saleChance);
        // 设置用户名到营销机会对象
        saleChance.setCreateMan(userName);
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功！");
    }

    /**
     * 删除营销机会（可以批量删除）
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会数据删除成功！");
    }
    /**
     * 更新营销机会
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        // 调用Service层的添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }
}
