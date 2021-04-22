package com.justsafe.libview.invite;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static String SERVER_URL = "http://61.153.225.150:12322/neeting";//测试环境
    public final static String ORG_ORG_URL = SERVER_URL + "/depart/list";
    public final static String STAFF_ORG_URL = SERVER_URL + "/user/list";
    public final static String SEARCH_STAFF_URL = SERVER_URL + "/user/list/search";


    public final static String SELECTED_KEY = "selected_key";
    public final static String USER_INFO_KEY = "user_info_key";
    public final static String USERNAME_KEY = "username";
    public final static String MDM_LOGIN_KEY = "mdm_login_key";
    public final static String AUTO_LOGIN_KEY = "auto_login_key";
    public final static String LOGIN_TIME_KEY = "login_time_key";
    //保存最新的成功的会议id
    public final static String NEETINGID_KEY = "neetingId_key";
    //保存最新的成功的会议主持人
    public final static String NEETING_MANAGE = "neeting_manage";
    public static List<Object> all_SELECTED_LIST = new ArrayList<>();//邀请人总数
    public static List<Object> temp_SELECTED_LIST = new ArrayList<>();//当前临时邀请人
    public static final int MEETING_STATUS_NOT_EXISTS = -1;//-1是会议不存在或已结束 1是会议正在进行中
    public static final int MEETING_STATUS_SCHEDULED = 0;// 0是已预约未启动
    public static final int MEETING_STATUS_IN_MEETING = 1;//1是会议正在进行中

    //ScheduleActivity2
    public static final int REQ_NEEING_LIST = 1;
    public static final int REQ_COMMON_NEETING = 2;

}





