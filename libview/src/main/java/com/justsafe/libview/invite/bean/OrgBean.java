package com.justsafe.libview.invite.bean;

public class OrgBean {

    private String departCode;
    private String departName;
    private int empCount;
    private String departPath;

    public String[] getDepartPath() {
        return departPath.split("-");
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

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public int getEmpCount() {
        return empCount;
    }

    public void setEmpCount(int empCount) {
        this.empCount = empCount;
    }

    @Override
    public String toString() {
        return "OrgBean{" +
                "departCode='" + departCode + '\'' +
                ", departName='" + departName + '\'' +
                ", empCount=" + empCount +
                ", departPath='" + departPath + '\'' +
                '}';
    }
}
