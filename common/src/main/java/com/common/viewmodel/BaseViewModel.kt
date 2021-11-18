package com.common.viewmodel

import androidx.lifecycle.viewModelScope
import com.common.BaseResponse
import com.common.throwe.BaseResponseThrowable
import com.common.throwe.ThrowableHandler
import kotlinx.coroutines.*

/**
 *create by 2020/5/22
 * ViewModel 基础类
 *@author zt
 */
open class BaseViewModel() : BaseLifeViewModel() {

    protected var tag = BaseViewModel::class.simpleName

    var stateView = StateView()

    private fun launchUi(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch { block() }

    /**
     * 直接获取结果的
     */
    fun <T> async(
        request: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit = {}
    ) {
        launchUi {
            handleRequest(withContext(Dispatchers.IO) {
                request
            }, {
                success(it)
            }, {
                error(it)
            }, {
                complete()//不管成功与否，执行结束调用
            })
        }
    }

    //过滤结果
    fun <T> asyncExecute(
        request: suspend CoroutineScope.() -> BaseResponse<T>,
        success: (T) -> Unit,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit = {}
    ) {
        launchUi {
            handleRequest(withContext(Dispatchers.IO) {
                request
            }, {response->
                executeResponse(response){
                    success(it)
                }
            }, {
                error(it)
            }, {
                complete()
            })
        }
    }

    /**
     * 带loading的请求
     */
    fun <T> async(
        request: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        showDialog: Boolean = true,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit = {
            if (showDialog) {
                stateView.isLoading.value = false
            }
        }

    ) {
        if (showDialog) {
            stateView.isLoading.value = true
        }
        launchUi {
            handleRequest(withContext(Dispatchers.IO) {
                request
            }, {viewModelScope
                success(it)
            }, {
                error(it)
            }, {
                complete()
            })
        }
    }

    private suspend fun <T> handleRequest(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: java.lang.Exception) {
                error(ThrowableHandler.handleThrowable(e))
            } finally {
                complete()
            }
        }
    }

    //过滤返回数据
    private suspend fun <T> executeResponse(
        response: BaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (response.code == 200) success(response.data)
            else throw BaseResponseThrowable(response.code, response.errorMsg)
        }
    }

    var mEventHub: SingleLiveEvent<LiveDataEvent> =
        SingleLiveEvent<LiveDataEvent>()
}

