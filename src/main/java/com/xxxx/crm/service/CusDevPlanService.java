package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;
    /**
     * 多条件查询客户开发计划
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        //得到对应分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        //分页列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加客户开发计划项数据
     *  1. 参数校验
     *      营销机会ID   非空，数据存在
     *      计划项内容   非空
     *      计划时间     非空
     *  2. 设置参数的默认值
     *      是否有效    默认有效
     *      创建时间    系统当前时间
     *      修改时间    系统当前时间
     *  3. 执行添加操作，判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        checkCusDevPlanParams(cusDevPlan);
        //设置有效
        cusDevPlan.setIsValid(1);
        //设置时间
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        //插入数据
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1,"添加营销计划失败！");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    /**
     * 更新客户开发计划
     *  1. 参数校验
     *      计划项ID     非空，数据存在
     *      营销机会ID   非空，数据存在
     *      计划项内容   非空
     *      计划时间     非空
     *  2. 设置参数的默认值
     *      修改时间    系统当前时间
     *  3. 执行更新操作，判断受影响的行数
     */
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        Integer Id = cusDevPlan.getId();
        System.out.println("数据："+cusDevPlanMapper.selectByPrimaryKey(Id));
        AssertUtil.isTrue(Id == null || cusDevPlanMapper.selectByPrimaryKey(Id) == null,"待更新的id不存在或为空");
        // 计划项内容   非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划内容不能为空！");
        // 计划时间     非空
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(), "计划时间不能为空！");
        //设置时间
        cusDevPlan.setUpdateDate(new Date());
        //更新数据
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"数据更新失败！");
    }

    /**
     * 删除客户开发计划
     * @param id
     */
    public void deleteCusDevPlan(Integer id){
        System.out.println("数据："+cusDevPlanMapper.selectByPrimaryKey(id));
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(id == null || cusDevPlan == null,"删除的id不存在或为空！");
        //更新时间
        cusDevPlan.setUpdateDate(new Date());
        cusDevPlan.setIsValid(0);
        //执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"客户计划删除失败！");
    }
    /**
     *  参数校验
     *      营销机会ID   非空，数据存在
     *      计划项内容   非空
     *      计划时间     非空
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        Integer sId = cusDevPlan.getSaleChanceId();
        //System.out.println("cusDevPlan："+cusDevPlan.toString());
        //System.out.println("数据："+saleChanceMapper.selectByPrimaryKey(sId));
        AssertUtil.isTrue( sId== null || saleChanceMapper.selectByPrimaryKey(sId) == null,"添加营销计划失败，参数出错！");
        // 计划项内容   非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划内容不能为空！");
        // 计划时间     非空
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(), "计划时间不能为空！");
    }
}
