package com.common.viewmodel


class LiveDataEvent {

    companion object {
        const val SUCCESS: Int = 200

        /**
         * 登录状态
         *
         * @param LOGIN_SUCCESS
         * @return
         */
        const val LOGIN_SUCCESS: Int = 0x01


        /**
         * 登录错误
         *
         * @param LOGIN_FAIL
         * @return -
         */
        const val LOGIN_FAIL: Int = 0x02

        /**
         * 可判断所有错误
         */
        val JUST_ERROR_FAIL: Int = 0x37
    }

    var action = 0
    var any: Any? = null
    var anyOne: Any? = null


    //有参次构造器
    constructor(action:Int,any:Any){
        this.action=action
        this.any=any
    }

    //有双参次构造器
    constructor(action:Int,any:Any,anyOne: Any?){
        this.action=action
        this.any=any
        this.anyOne=anyOne
    }
}