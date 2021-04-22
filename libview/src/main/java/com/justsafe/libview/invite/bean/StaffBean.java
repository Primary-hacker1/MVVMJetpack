package com.justsafe.libview.invite.bean;

import java.io.Serializable;

public class StaffBean implements Serializable {

    private String username;//一般是手机号
    private String name;//姓名
    private String departName;
    private String departNum;//手机号
    private String departJinghao;//警号
    private String departCornet;//组织信息
    private String departCode;
    private String departPath;

    public String getDepartJinghao() {
        return departJinghao;
    }

    public void setDepartJinghao(String departJinghao) {
        this.departJinghao = departJinghao;
    }

    public String[] getDepartPath1() {
        return departPath.split("-");
    }

    public String getDepartPath() {
        return departPath;
    }

    public void setDepartPath(String departPath) {
        this.departPath = departPath;
    }


    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getDepartNum() {
        return departNum;
    }

    public void setDepartNum(String departNum) {
        this.departNum = departNum;
    }

    public String getDepartCornet() {
        return departCornet;
    }

    public void setDepartCornet(String departCornet) {
        this.departCornet = departCornet;
    }

    @Override
    public String toString() {
        return "StaffBean{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", departName='" + departName + '\'' +
                ", departNum='" + departNum + '\'' +
                ", departCornet='" + departCornet + '\'' +
                ", departCode='" + departCode + '\'' +
                ", departPath='" + departPath + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}

