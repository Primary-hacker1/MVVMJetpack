package com.common.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
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
import java.io.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 *create by 2019/12/30
 * 扩展函数基类
 *@author zt
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

fun Fragment.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (TextUtils.isEmpty(msg)) {
        return
    }
    Toast.makeText(activity, msg!!, duration).show()
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

fun<T> Single<T>.async()=this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

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
 * 类似java->runOnUiThread
 */
val mainThread by lazy {
    Handler(Looper.getMainLooper())
}

fun onUI(callback: () -> Unit) {
    mainThread.post(callback)
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

fun View.setNoRepeatListener(interval: Long = 500, action: (view: View) -> Unit) {
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


/**
 * @author lollipop
 * @date 2020/6/9 23:17
 * 通用的全局工具方法
 */
object CommonUtil {

    /**
     * 全局的打印日志的关键字
     */
    var logTag = "WindowsLauncher"

    /**
     * 异步线程池
     */
    private val threadPool: Executor by lazy {
        Executors.newScheduledThreadPool(2)
    }

    /**
     * 主线程的handler
     */
    private val mainThread: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    /**
     * 异步任务
     */
    fun <T> doAsync(task: Task<T>) {
        threadPool.execute(task.runnable)
    }

    /**
     * 主线程
     */
    fun <T> onUI(task: Task<T>) {
        mainThread.post(task.runnable)
    }

    /**
     * 延迟任务
     */
    fun <T> delay(delay: Long, task: Task<T>) {
        mainThread.postDelayed(task.runnable, delay)
    }

    /**
     * 移除任务
     */
    fun <T> remove(task: Task<T>) {
        mainThread.removeCallbacks(task.runnable)
    }

    /**
     * 将一组对象打印合并为一个字符串
     */
    fun print(value: Array<out Any>): String {
        if (value.isEmpty()) {
            return ""
        }
        val iMax = value.size - 1
        val b = StringBuilder()
        var i = 0
        while (true) {
            b.append(value[i].toString())
            if (i == iMax) {
                return b.toString()
            }
            b.append(" ")
            i++
        }
    }

    /**
     * 包装的任务类
     * 包装的意义在于复用和移除任务
     * 由于Handler任务可能造成内存泄漏，因此在生命周期结束时，有必要移除任务
     * 由于主线程的Handler使用了全局的对象，移除不必要的任务显得更为重要
     * 因此包装了任务类，以任务类为对象来保留任务和移除任务
     */
    class Task<T>(
        private val target: T,
        private val err: ((Throwable) -> Unit) = {},
        private val run: T.() -> Unit
    ) {

        val runnable = Runnable {
            try {
                run(target)
            } catch (e: Throwable) {
                err(e)
            }
        }

        fun cancel() {
            remove(this)
        }

        fun run() {
            doAsync(this)
        }

        fun sync() {
            onUI(this)
        }

        fun delay(time: Long) {
            delay(time, this)
        }
    }

}

/**
 * 用于创建一个任务对象
 */
inline fun <reified T> T.task(
    noinline err: ((Throwable) -> Unit) = {},
    noinline run: T.() -> Unit
) = CommonUtil.Task(this, err, run)

/**
 * 异步任务
 */
inline fun <reified T> T.doAsync(
    noinline err: ((Throwable) -> Unit) = {},
    noinline run: T.() -> Unit
): CommonUtil.Task<T> {
    val task = task(err, run)
    CommonUtil.doAsync(task)
    return task
}

/**
 * 主线程
 */
inline fun <reified T> T.onUI(
    noinline err: ((Throwable) -> Unit) = {},
    noinline run: T.() -> Unit
): CommonUtil.Task<T> {
    val task = task(err, run)
    CommonUtil.onUI(task)
    return task
}

/**
 * 延迟任务
 */
inline fun <reified T> T.delay(
    delay: Long,
    noinline err: ((Throwable) -> Unit) = {},
    noinline run: T.() -> Unit
): CommonUtil.Task<T> {
    val task = task(err, run)
    CommonUtil.delay(delay, task)
    return task
}

/**
 * 一个全局的打印Log的方法
 */
inline fun <reified T : Any> T.log(vararg value: Any) {
    Log.d(CommonUtil.logTag, "${this.javaClass.simpleName} -> ${CommonUtil.print(value)}")
}


/**
 * 对一个输入框关闭键盘
 */
fun EditText.closeBoard() {
    //拿到InputMethodManager
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let { imm ->
        //如果window上view获取焦点 && view不为空
        if (imm.isActive) {
            //拿到view的token 不为空
            windowToken?.let { token ->
                imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}

/**
 * 对一个activity关闭键盘
 */
fun Activity.closeBoard() {
    //拿到InputMethodManager
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let { imm ->
        //如果window上view获取焦点 && view不为空
        if (imm.isActive) {
            currentFocus?.let { focus ->
                //拿到view的token 不为空
                focus.windowToken?.let { token ->
                    //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                    imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        }
    }
}

/**
 * 对一个颜色值设置它的透明度
 * 只支持#AARRGGBB格式排列的颜色值
 */
fun Int.changeAlpha(a: Int): Int {
    return this and 0xFFFFFF or ((a % 256) shl 24)
}

/**
 * 以浮点数的形式，以当前透明度为基础，
 * 调整颜色值的透明度
 */
fun Int.changeAlpha(f: Float): Int {
    return this.changeAlpha(((this ushr 24) * f).toInt().range(0, 255))
}

/**
 * 将一个浮点数，以dip为单位转换为对应的像素值
 */
val Float.dp2px: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this,
            Resources.getSystem().displayMetrics
        )
    }

/**
 * 将一个浮点数，以sp为单位转换为对应的像素值
 */
val Float.sp2px: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, this,
            Resources.getSystem().displayMetrics
        )
    }

/**
 * 将一个整数，以dip为单位转换为对应的像素值
 */
val Int.dp2px: Int
    get() {
        return this.toFloat().dp2px.toInt()
    }

/**
 * 将一个整数，以sp为单位转换为对应的像素值
 */
val Int.sp2px: Int
    get() {
        return this.toFloat().sp2px.toInt()
    }

/**
 * 将一个整数作为id来寻找对应的颜色值，
 * 如果找不到或者发生了异常，那么将会返回白色
 */
fun Int.findColor(context: Context): Int {
    try {
        return ContextCompat.getColor(context, this)
    } catch (e: Throwable) {
    }
    return Color.WHITE
}

/**
 * 将一个整数作为id来寻找对应的颜色值，
 * 如果找不到或者发生了异常，那么将会返回白色
 */
fun Int.findColor(view: View): Int {
    return this.findColor(view.context)
}

/**
 * 如果当前整数是0，那么获取回调函数中的值作为返回值
 * 否则返回当前
 */
fun Int.zeroTo(value: () -> Int): Int {
    return if (this == 0) {
        value()
    } else {
        this
    }
}

/**
 * 对一个浮点数做范围约束
 */
fun Float.range(min: Float, max: Float): Float {
    if (this < min) {
        return min
    }
    if (this > max) {
        return max
    }
    return this
}

/**
 * 将一个字符串转换为颜色值
 * 只接受1～8位0～F之间的字符
 */
fun String.parseColor(): Int {
    val value = this
    if (value.isEmpty()) {
        return 0
    }
    return when (value.length) {
        1 -> {
            val v = (value + value).toInt(16)
            Color.rgb(v, v, v)
        }
        2 -> {
            val v = value.toInt(16)
            Color.rgb(v, v, v)
        }
        3 -> {
            val r = value.substring(0, 1)
            val g = value.substring(1, 2)
            val b = value.substring(2, 3)
            Color.rgb(
                (r + r).toInt(16),
                (g + g).toInt(16),
                (b + b).toInt(16)
            )
        }
        4, 5 -> {
            val a = value.substring(0, 1)
            val r = value.substring(1, 2)
            val g = value.substring(2, 3)
            val b = value.substring(3, 4)
            Color.argb(
                (a + a).toInt(16),
                (r + r).toInt(16),
                (g + g).toInt(16),
                (b + b).toInt(16)
            )
        }
        6, 7 -> {
            val r = value.substring(0, 2).toInt(16)
            val g = value.substring(2, 4).toInt(16)
            val b = value.substring(4, 6).toInt(16)
            Color.rgb(r, g, b)
        }
        8 -> {
            val a = value.substring(0, 2).toInt(16)
            val r = value.substring(2, 4).toInt(16)
            val g = value.substring(4, 6).toInt(16)
            val b = value.substring(6, 8).toInt(16)
            Color.argb(a, r, g, b)
        }
        else -> {
            Color.WHITE
        }
    }
}

/**
 * 对一个输入框做回车事件监听
 */
inline fun <reified T : EditText> T.onActionDone(noinline run: T.() -> Unit) {
    this.imeOptions = EditorInfo.IME_ACTION_DONE
    this.setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE
            || event.keyCode == KeyEvent.KEYCODE_ENTER
        ) {
            run.invoke(this)
            true
        } else {
            false
        }
    }
}

/**
 * 尝试将一个字符串转换为整形，
 * 如果发生了异常或者为空，那么将会返回默认值
 */
fun String.tryInt(def: Int): Int {
    try {
        if (TextUtils.isEmpty(this)) {
            return def
        }
        return this.toInt()
    } catch (e: Throwable) {
    }
    return def
}

/**
 * 以一个View为res来源获取指定id的颜色值
 */
fun View.getColor(id: Int): Int {
    return ContextCompat.getColor(this.context, id)
}

/**
 * 一个整形的范围约束
 */
fun Int.range(min: Int, max: Int): Int {
    if (this < min) {
        return min
    }
    if (this > max) {
        return max
    }
    return this
}

/**
 * 从Context中尝试通过名字获取一个drawable的id
 */
fun Context.findDrawableId(name: String): Int {
    var icon = findId(name, "drawable")
    if (icon != 0) {
        return icon
    }
    icon = findId(name, "mipmap")
    return icon
}

/**
 * 从Context中尝试通过名字获取一个指定类型的资源id
 */
fun Context.findId(name: String, type: String): Int {
    return resources.getIdentifier(name, type, packageName)
}

/**
 * 尝试通过一个id获取对应的资源名
 */
fun Context.findName(id: Int): String {
    return resources.getResourceName(id)
}

/**
 * 从context中获取当前应用的版本名称
 */
fun Context.versionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}

/**
 * 从context中获取当前应用的版本名称
 */
fun Context.versionCode(): Long {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    if (versionThen(Build.VERSION_CODES.P)) {
        return packageInfo.longVersionCode
    }
    return packageInfo.versionCode.toLong()
}

/**
 * 将一段文本写入一个文件中
 * 它属于IO操作，这是一个耗时的任务，
 * 需要在子线程中执行
 */
fun String.writeTo(file: File) {
    try {
        if (file.exists()) {
            file.delete()
        } else {
            file.parentFile?.mkdirs()
        }
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        val buffer = ByteArray(2048)
        try {
            inputStream = ByteArrayInputStream(this.toByteArray(Charsets.UTF_8))
            outputStream = FileOutputStream(file)
            var length = inputStream.read(buffer)
            while (length >= 0) {
                outputStream.write(buffer, 0, length)
                length = inputStream.read(buffer)
            }
            outputStream.flush()
        } catch (ee: Throwable) {
            ee.printStackTrace()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

inline fun <reified T : ViewBinding> Activity.lazyBind(): Lazy<T> = lazy { bind() }

inline fun <reified T : ViewBinding> Fragment.lazyBind(): Lazy<T> = lazy { bind() }

inline fun <reified T : ViewBinding> View.lazyBind(): Lazy<T> = lazy { bind() }

inline fun <reified T : ViewBinding> Activity.bind(): T {
    return this.layoutInflater.bind()
}

inline fun <reified T : ViewBinding> Fragment.bind(): T {
    return this.layoutInflater.bind()
}

inline fun <reified T : ViewBinding> View.bind(): T {
    return LayoutInflater.from(this.context).bind()
}

inline fun <reified T : ViewBinding> ViewGroup.bind(attach: Boolean = false): T {
    return LayoutInflater.from(this.context).bind(this, attach)
}

inline fun <reified T : ViewBinding> LayoutInflater.bind(
    parent: ViewGroup? = null,
    attach: Boolean = false
): T {
    val layoutInflater: LayoutInflater = this
    val bindingClass = T::class.java
    if (parent == null) {
        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        val invokeObj = inflateMethod.invoke(null, layoutInflater)
        if (invokeObj is T) {
            return invokeObj
        }
    } else {
        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val invokeObj = inflateMethod.invoke(null, layoutInflater, parent, false)
        if (invokeObj is T) {
            if (attach) {
                parent.addView(invokeObj.root)
            }
            return invokeObj
        }
    }
    throw InflateException("Cant inflate ViewBinding ${bindingClass.name}")
}

inline fun <reified T : ViewBinding> View.withThis(inflate: Boolean = false): Lazy<T> = lazy {
    val bindingClass = T::class.java
    val view: View = this
    if (view is ViewGroup && inflate) {
        val bindMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        val bindObj = bindMethod.invoke(null, LayoutInflater.from(context), view, true)
        if (bindObj is T) {
            return@lazy bindObj
        }
    } else {
        val bindMethod = bindingClass.getMethod(
            "bind",
            View::class.java
        )
        val bindObj = bindMethod.invoke(null, view)
        if (bindObj is T) {
            return@lazy bindObj
        }
    }
    throw InflateException("Cant inflate ViewBinding ${bindingClass.name}")
}

inline fun <reified T : Any> Fragment.identityCheck(ctx: Context? = null, run: (T) -> Unit) {
    // 父碎片第一优先
    if (check(parentFragment, run)) {
        return
    }
    // 指定对象第二优先
    if (check(ctx, run)) {
        return
    }
    // 默认上下文最后
    if (check(context, run)) {
        return
    }
}

inline fun <reified T : Any> check(ctx: Any? = null, run: (T) -> Unit): Boolean {
    if (ctx is T) {
        run(ctx)
        return true
    }
    return false
}

inline fun <T : View> T.visibleOrGone(boolean: Boolean, onVisible: (T.() -> Unit) = {}) {
    visibility = if (boolean) {
        View.VISIBLE
    } else {
        View.GONE
    }
    if (boolean) {
        onVisible.invoke(this)
    }
}

inline fun <T : View> T.visibleOrInvisible(boolean: Boolean, onVisible: (T.() -> Unit) = {}) {
    visibility = if (boolean) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
    if (boolean) {
        onVisible.invoke(this)
    }
}

fun View.tryVisible() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

fun View.tryInvisible() {
    if (this.visibility != View.INVISIBLE) {
        this.visibility = View.INVISIBLE
    }
}

fun View.onClick(callback: (View) -> Unit) {
    this.setOnClickListener(callback)
}

fun Int.smallerThen(o: Int): Int {
    if (this > o) {
        return o
    }
    return this
}

fun Int.biggerThen(o: Int): Int {
    if (this < o) {
        return o
    }
    return this
}

fun versionThen(target: Int): Boolean {
    return Build.VERSION.SDK_INT >= target
}

fun View.findRootGroup(filter: (ViewGroup) -> Boolean): ViewGroup? {
    var parent: ViewParent?
    var view: View? = this
    var group: ViewGroup? = null
    do {
        parent = view?.parent
        if (parent is ViewGroup && filter(parent)) {
            group = parent
        }
        view = if (parent is View) {
            parent
        } else {
            null
        }
    } while (parent != null)
    return group
}

/**
 * 是否已经被销毁
 */
fun LifecycleOwner.isDestroyed() = lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED)

/**
 * 是否已经被创建
 */
fun LifecycleOwner.isCreated() = lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)

/**
 * 是否已经被初始化
 */
fun LifecycleOwner.isInitialized() = lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)

/**
 * 是否已经开始运行
 */
fun LifecycleOwner.isStarted() = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

/**
 * 是否已经可见
 */
fun LifecycleOwner.isResumed() = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)