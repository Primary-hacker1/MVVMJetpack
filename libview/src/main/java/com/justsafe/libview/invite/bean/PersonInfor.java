package com.justsafe.libview.invite.bean;


/**
 * Created by zhangrj on 2020/2/19.
 {"msgs":"","content":{"username":"13522214444","departCode":"XXXXXXXX","name":"jack","departName":"嘉兴嘉赛","newIdCard":"233250","isCreateMeeting":""},"status":200}
 */

public class PersonInfor {

    private String username;//手机号
    private String departCode;//部门编号
    private String name;//人员姓名

    private String departName;//部门名称
    private String departPath;//部门路径
    private String newIdCard;//身份证
    private String ctccCornet;//短号
    private String policeNo;//警号
    private String isCreateMeeting;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
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

    public String getNewIdCard() {
        return newIdCard;
    }

    public void setNewIdCard(String newIdCard) {
        this.newIdCard = newIdCard;
    }

    public String getIsCreateMeeting() {
        return isCreateMeeting;
    }

    public void setIsCreateMeeting(String isCreateMeeting) {
        this.isCreateMeeting = isCreateMeeting;
    }

    public String getDepartPath() {
        return departPath;
    }

    public void setDepartPath(String departPath) {
        this.departPath = departPath;
    }

    public String getCtccCornet() {
        return ctccCornet;
    }

    public void setCtccCornet(String ctccCornet) {
        this.ctccCornet = ctccCornet;
    }

    public String getPoliceNo() {
        return policeNo;
    }

    public void setPoliceNo(String policeNo) {
        this.policeNo = policeNo;
    }

    @Override
    public String toString() {
        return "PersonInfor{" +
                "username='" + username + '\'' +
                ", departCode='" + departCode + '\'' +
                ", name='" + name + '\'' +
                ", departName='" + departName + '\'' +
                ", departPath='" + departPath + '\'' +
                ", newIdCard='" + newIdCard + '\'' +
                ", ctccCornet='" + ctccCornet + '\'' +
                ", policeNo='" + policeNo + '\'' +
                ", isCreateMeeting='" + isCreateMeeting + '\'' +
                '}';
    }
}

