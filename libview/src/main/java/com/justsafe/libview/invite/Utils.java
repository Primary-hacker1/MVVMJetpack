package com.justsafe.libview.invite;

import com.justsafe.libview.invite.bean.OrgBean;
import com.justsafe.libview.invite.bean.StaffBean;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";



    public static boolean deleteStaff(StaffBean sb) {
        for (int i = 0; i < Constant.temp_SELECTED_LIST.size(); i++) {
            if (Constant.temp_SELECTED_LIST.get(i) instanceof StaffBean) {
                if (sb.getName().equals(((StaffBean) Constant.temp_SELECTED_LIST.get(i)).getName())) {
                    Constant.temp_SELECTED_LIST.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean deleteStaff1(StaffBean sb) {
        for (int i = 0; i < Constant.all_SELECTED_LIST.size(); i++) {
            if (Constant.all_SELECTED_LIST.get(i) instanceof StaffBean) {
                if (sb.getUserName().equals(((StaffBean) Constant.all_SELECTED_LIST.get(i)).getUserName())) {
                    Constant.all_SELECTED_LIST.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean deleteOrg(OrgBean ob) {
        for (int i = 0; i < Constant.temp_SELECTED_LIST.size(); i++) {
            if (Constant.temp_SELECTED_LIST.get(i) instanceof OrgBean) {
                if (ob.getDepartCode().equals(((OrgBean) Constant.temp_SELECTED_LIST.get(i)).getDepartCode())) {
                    Constant.temp_SELECTED_LIST.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public static void deleteStaffByOrg(OrgBean ob) {
        for (int i = 0; i < Constant.temp_SELECTED_LIST.size(); i++) {
            if (Constant.temp_SELECTED_LIST.get(i) instanceof StaffBean) {
                if (ob.getDepartCode().equals(((StaffBean) Constant.temp_SELECTED_LIST.get(i)).getDepartCode())) {
                    Constant.temp_SELECTED_LIST.remove(i);
                }
            }
        }
    }

    public static int getSelectedCount(OrgBean ob) {
        int count = 0;
        String curDCode = ob.getDepartCode();
        int empCount = ob.getEmpCount();
        List<Object> tempList = new ArrayList<>();

        for (int i = 0; i < Constant.temp_SELECTED_LIST.size(); i++) {
            if (Constant.temp_SELECTED_LIST.get(i) instanceof OrgBean) {
                boolean isMatch = false;
                String[] departPath = ((OrgBean) (Constant.temp_SELECTED_LIST.get(i))).getDepartPath();
                for (int j = 0; j < departPath.length; j++) {
                    if (curDCode.equals(departPath[j])) {
                        isMatch = true;
                        count += ((OrgBean) Constant.temp_SELECTED_LIST.get(i)).getEmpCount();
                        break;
                    }
                }
                if (!isMatch) {
                    tempList.add(Constant.temp_SELECTED_LIST.get(i));
                }
            } else if (Constant.temp_SELECTED_LIST.get(i) instanceof StaffBean) {
                boolean isMatch = false;
                String[] sdpath = ((StaffBean) Constant.temp_SELECTED_LIST.get(i)).getDepartPath1();
                for (int j = 0; j < sdpath.length; j++) {
                    if (curDCode.equals(sdpath[j])) {
                        isMatch = true;
                        count++;
                        break;
                    }
                }
                if (!isMatch) {
                    tempList.add(Constant.temp_SELECTED_LIST.get(i));
                }
            }
        }
        //删除重复数据，来自于检索界面
//        if (empCount != 0 && count == empCount) {
//            Constant.TEMP_SELECED_LIST = tempList;
//            Constant.TEMP_SELECED_LIST.add(ob);
//        }
        return count;
    }


}


