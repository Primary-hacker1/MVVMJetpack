package com.common.viewmodel;

public class LiveDataEvent {

    public static final String SUCCESS = "10000";

    /**
     * 登录状态
     *
     * @param LOGIN_SUCCESS
     * @return
     */
    public static final int LOGIN_SUCCESS = 0x01;

    /**
     * 登录错误
     *
     * @param LOGIN_FAIL
     * @return -
     */
    public static final int LOGIN_FAIL = 0x02;

    public static final int ALL_NAME_DATA = 0x03;
    public static final int ALL_NAME_INT = 0x00;
    public static final int CLASS_DATA_NUM = 0x1;
    public static final int CLASS_NAME_DATA = 0x04;

    /**
     * all数据getData,通用字段
     *
     * @param CLASS_DATA
     * @return
     */
    public static final int CLASS_DATA = 0x05;

//    public static final int GET_SMSCODE_SUCCESS = 0x07;

    public static final int GROUP_PRISONER = 0x011;

    public static final int HANDOVER_DATA = 0x08;


    /**
     * 加载进度,进度条显示隐藏
     *
     * @param JUST_PROGRESS_INT,JUST_PROGRESS_VISIBLE
     * @return
     */
    public static final int JUST_PROGRESS_INT = 0x018;
    public static final int JUST_PROGRESS_VISIBLE = 0x019;

    /**
     * 进度条显示隐藏
     *
     * @param FINDCLASSNAME,PROGRESSVISIBER
     * @return
     */
    public static final int EmployeesByNum = 0x05;

    public static final int LOGIN_SUCCESS_FAIL = 0x06;

    /**
     * 巡视点名，随机生成
     *
     * @param ROLL_CALL_DATA
     * @return
     */
    public static final int ROLL_CALL_DATA = 0x15;
    public static final int HANDOVER_INFO = 0x16;

    public static final int TALK_ISDELETE = 0x21;
    public static final int TALK_NUM_DATA = 0x22;

    public static final int UploadCall = 0x23;
    public static final int JUST_SELECT_LOGIN = 0x24;

    public static final int JUST_SELECT_PARENT = 0x25;
    public static final int JUST_SELECT_CHILD = 0x26;

    public static final int JUST_INHERIT_DATA = 0x27;
    public static final int SCORE_FAIL = 0x28;

    /**
     * 接口返回type，判断接口是否成功
     */
    public static final String JUST_SUCCESS = SUCCESS;

    public static final int JUST_SUCCESS_TAG = 10000;

    public static final int JUST_ERROR_FAIL_SUBMIT = 0x30;
    public static final int EMC_LOGIN_SUCCESS = 0x31;

    public static final int EMC_MEETING_FAIL = 0x32;

    public static final int UPDATE_SUCCESS = 0x33;
    public static final int UPDATE_FAIL = 0x34;

    public static final int SCORE_DETAIL_SUCCESS = 0x35;
    public static final int LOGIN_GO = 0x36;

    /**
     * 可判断所有错误
     */
    public static final int JUST_ERROR_FAIL = 0x37;

    /**
     * 只判断数据库的错误
     */
    public static final int ERROR = 0x38;


    public int action;
    public Object object;
    public Object object2;

    public LiveDataEvent(int action, Object object) {
        this.action = action;
        this.object = object;
    }

    public LiveDataEvent(int action, Object object, Object object2) {
        this.action = action;
        this.object = object;
        this.object2 = object2;

    }
}
