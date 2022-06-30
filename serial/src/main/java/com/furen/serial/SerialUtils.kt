package com.furen.serial

import com.furen.serial.utils.LiveDataBus
import com.roy.www.serialport.OnSerialCallback
import com.roy.www.serialport.SerialHelper

object SerialUtils {
    fun openSerial(port: String, barrage: Int) {
        val isOpen: Boolean = SerialHelper.getInstance().open(
            port,
            barrage,
            object : OnSerialCallback {
                /**
                 * @param bytes 串口接收到的字节数据
                 * */
                override fun onReceiveNewBytes(bytes: ByteArray?) {
                    LiveDataBus.get().with("").value = bytes//观察者发送数据
                }

                override fun onSerialState(b: Boolean) {

                }
            })
    }


    /**
     * @param data 串口发送数据
     * */
    fun sendSerial(data: String) {
        SerialHelper.getInstance().transferHexString(data)
    }

    /**
     * @param data 关闭串口
     * */
    fun closeSerial(data: String) {
        SerialHelper.getInstance().close()
    }
}