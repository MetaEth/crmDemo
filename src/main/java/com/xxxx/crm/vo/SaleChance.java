package com.xxxx.crm.vo;


import java.util.Date;

public class SaleChance {
    private Integer id;
    private String chanceSource;
    private String customerName;
    private Integer cgjl;
    private String overview;
    private String linkMan;
    private String linkPhone;
    private String description;
    private String createMan;
    private String assignMan;
    private Date assignTime;
    private Integer state;
    private Integer devResult;
    private Integer isValid;
    private Date createDate;
    private Date updateDate;
    private String uname;    //  分配人
    public SaleChance(){}

    public SaleChance(Integer id, String chanceSource, String customerName, Integer cgjl, String overview, String linkMan, String linkPhone, String description, String createMan, String assignMan, Date assignTime, Integer state, Integer devResult, Integer isValid, Date createDate, Date updateDate, String uname) {
        this.id = id;
        this.chanceSource = chanceSource;
        this.customerName = customerName;
        this.cgjl = cgjl;
        this.overview = overview;
        this.linkMan = linkMan;
        this.linkPhone = linkPhone;
        this.description = description;
        this.createMan = createMan;
        this.assignMan = assignMan;
        this.assignTime = assignTime;
        this.state = state;
        this.devResult = devResult;
        this.isValid = isValid;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.uname = uname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChanceSource() {
        return chanceSource;
    }

    public void setChanceSource(String chanceSource) {
        this.chanceSource = chanceSource;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCgjl() {
        return cgjl;
    }

    public void setCgjl(Integer cgjl) {
        this.cgjl = cgjl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(String assignMan) {
        this.assignMan = assignMan;
    }

    public Date getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "SaleChance{" +
                "id=" + id +
                ", chanceSource='" + chanceSource + '\'' +
                ", customerName='" + customerName + '\'' +
                ", cgjl=" + cgjl +
                ", overview='" + overview + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", linkPhone='" + linkPhone + '\'' +
                ", description='" + description + '\'' +
                ", createMan='" + createMan + '\'' +
                ", assignMan='" + assignMan + '\'' +
                ", assignTime=" + assignTime +
                ", state=" + state +
                ", devResult=" + devResult +
                ", isValid=" + isValid +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", uname='" + uname + '\'' +
                '}';
    }
}
