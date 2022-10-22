package com.xxxx.crm.vo;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Date;

public class User {
    private Integer id;
    private String userName;
    private String userPwd;
    private String trueName;
    private String email;
    private String phone;
    private Integer isValid;
    private Date createDate;
    private Date updateDate;
    private String roleIds; // 用户对应的角色ID
    public User(){}
    public User(Integer id, String userName, String userPwd, String trueName, String email, String phone, Integer isValid, Date createDate, Date updateDate) {
        this.id = id;
        this.userName = userName;
        this.userPwd = userPwd;
        this.trueName = trueName;
        this.email = email;
        this.phone = phone;
        this.isValid = isValid;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", trueName='" + trueName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", isValid=" + isValid +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", roleIds='" + roleIds + '\'' +
                '}';
    }
}