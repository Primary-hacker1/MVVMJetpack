package com.common.throwe

/**
 * create by 2020/5/15
 *
 * @author yx
 */
class BaseResponseThrowable : Throwable {
    var code: Int
    var errMsg: String
    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        this.code = code
        this.errMsg = msg
    }

    constructor(code: Int, errMsg: String) {
        this.code = code
        this.errMsg = errMsg
    }


}