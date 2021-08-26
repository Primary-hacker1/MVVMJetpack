package com.common.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.common.network.RequestObserver
import com.common.throwe.BaseResponseThrowable
import com.uber.autodispose.SingleSubscribeProxy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.schedulers.Schedulers


/**
 *create by 2019/12/30
 * 扩展函数基类
 *@author yx
 */
fun Activity.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (TextUtils.isEmpty(msg)) {
        return
    }
    Toast.makeText(this, msg, duration).show()
}

fun Activity.toast(msg: Int?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg!!, duration).show()
}

fun <T> Single<T>.single(): Single<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

//fun<T> AutoDisposeConverter<T>.ad(pr: ScopeProvider): AutoDisposeConverter<T>? = AutoDispose.autoDisposable<T>(pr)

fun Activity.navigateToActivity(c: Class<*>) {
    val intent = Intent()
    intent.setClass(this, c)
    startActivity(intent)
}

fun Activity.navigateToActivity(c: Class<*>, bundle: Bundle) {
    val intent = Intent()
    intent.setClass(this, c)
    intent.putExtra("bundle", bundle)
    startActivity(intent)
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Single<T>.subscribes(
    onSuccess: (T) -> Unit,
    onError: (BaseResponseThrowable) -> Unit
): Disposable {
    ObjectHelper.requireNonNull(onSuccess, "onSuccess is null")
    ObjectHelper.requireNonNull(onError, "onError is null")
    val observer: RequestObserver<T> = RequestObserver(onSuccess, onError)
    subscribe(observer)
    return observer
}

fun <T> SingleSubscribeProxy<T>.subscribes(
    onSuccess: (T) -> Unit,
    onError: (BaseResponseThrowable) -> Unit
) {
    ObjectHelper.requireNonNull(onSuccess, "onSuccess is null")
    ObjectHelper.requireNonNull(onError, "onError is null")
    val observer: RequestObserver<T> = RequestObserver(onSuccess, onError)
    subscribe(observer)
}

//跳转类
inline fun <reified T : Activity> Context.startActivity(action: Intent.() -> Unit) {
    var intent = Intent(this, T::class.java)
    action(intent)
    this.startActivity(intent)
}


/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}


/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrGone(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrInvisible(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 将view转为bitmap
 */
@Deprecated("use View.drawToBitmap()")
fun View.toBitmap(scale: Float = 1f, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
    if (this is ImageView) {
        if (drawable is BitmapDrawable) return (drawable as BitmapDrawable).bitmap
    }
    this.clearFocus()
    val bitmap = createBitmapSafely(
        (width * scale).toInt(),
        (height * scale).toInt(),
        config,
        1
    )
    if (bitmap != null) {
        Canvas().run {
            setBitmap(bitmap)
            save()
            drawColor(Color.WHITE)
            scale(scale, scale)
            this@toBitmap.draw(this)
            restore()
            setBitmap(null)
        }
    }
    return bitmap
}

fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
    try {
        return Bitmap.createBitmap(width, height, config)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        if (retryCount > 0) {
            System.gc()
            return createBitmapSafely(width, height, config, retryCount - 1)
        }
        return null
    }
}


/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L

fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}

fun AppCompatActivity.clickNoRepeat(interval: Long = 500): Boolean {
    val currentTime = System.currentTimeMillis()
    if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
        return true
    }
    lastClickTime = currentTime
    return false
}

fun DialogFragment.clickNoRepeat(interval: Long = 500): Boolean {
    val currentTime = System.currentTimeMillis()
    if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
        return true
    }
    lastClickTime = currentTime
    return false
}


fun Any?.notNull(notNullAction: (value: Any) -> Unit, nullAction1: () -> Unit) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction1.invoke()
    }
}








