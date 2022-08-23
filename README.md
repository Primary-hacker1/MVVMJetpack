[![Platform][1]][2] [![GitHub license][3]][4]  [![GitHub license][5]][6]

# 👕👕👕:组件化快速开发专属框架！

- **common架构设计
- **基于MVVM模式集成谷歌官方推荐的JetPack组件库：LiveData、ViewModel、Lifecycle、Navigation、Retrofit、 RxJava、
hilt组件,增加flow持续更新最新**
- **使用kotlin语言，添加大量拓展函数，简化代码**
- **加入Retrofit网络请求,协程，帮你简化各种操作，让你快速请求网络**

## 演示Demo

![对比图](https://github.com/Primary-hacker1/MVVMJetpack/blob/main/aat/compared.png)

- **已经使用3年多时间优化和踩bug，现已经可以完全实现单一vm和单activity模式，
快速开发，冗余代码几乎为0，解耦5.0分，之后会一直更新为了更高阶开发身法努力！**

## 1.如何集成

- **1.1 在root's build.gradle中加入Jitpack仓库**

``` gradle
buildscript {
        repositories {
        ...
        mavenCentral()
    }
}

allprojects{
        repositories {
        ...
        mavenCentral()
    }
}
```

## 2.继承基类

一般我们项目中都会有一套自己定义的符合业务需求的基类 ***CommonBaseActivity/CommonBaseFragment***，
所以我们的基类需要**继承本框架的Base类**

**Activity：**

``` kotlin
@AndroidEntryPoint
class LoginActivity : CommonBaseActivity<ActivityLoginBinding>() {//布局ID

private val viewModel by viewModels<MainViewModel>()//委托

companion object {
/**
* @param context -
*/
fun startJUSTLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
    }
}

override fun initView() {

}

override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

}
```

## 3.编写一个登录功能

- **3.1 编写fragment_login.xml界面后转换成 databind 布局（鼠标停在根布局，Alt+Enter 点击提示 Convert to data binding
layout即可）**

``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto">

<data>

</data>

<LinearLayout
android:orientation="vertical"
android:layout_width="match_parent"
android:layout_height="match_parent">

....

</LinearLayout>
</layout>
```

- **3.2 创建ViewModel类继承BaseViewModel**

``` kotlin
@HiltViewModel
class MainViewModel @Inject constructor(
        private var repository: UserRepository,//网络注入
        private var plantDao: PlantRepository//数据库注入
    ) : BaseViewModel() {
}
```

- **3.3 创建 LoginFragment
继承基类传入相关泛型,第一个泛型为你创建的LoginViewModel,第二个泛型为ViewDataBind，保存fragment_login.xml后databinding会生成一个FragmentLoginBinding类。（如果没有生成，试着点击Build->Clean Project）**

``` kotlin
@AndroidEntryPoint
class LoginFragment : CommonBaseFragment<FragmentLoginBinding>() {

private val viewModel by viewModels<NewViewModel>()

override fun initView() {

}

/**
* 懒加载
*/
override fun loadData() {

}

override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentLoginBinding.inflate(inflater, container, false)
}
```

## 4.网络请求（Retrofit+协程或Rxjava）

- **4.1 新建请求配置类继承 BaseNetworkApi 示例：**

``` kotlin
class UserRepository @Inject internal constructor(private val apiService: BaseApiService) {
/**
* 协程请求
*/
suspend fun getNews(type: String): NewResponse = apiService.getNews(type)

/**
* rxjava请求
*/
fun getRxNews(type: String)=apiService.getRxNews(type).async()

}
```

- **4.2返回和传递实体例如:**

``` kotlin
data class NewResponse(
    val data: List<Data>,
    var code: String,
    var mesage: String
)
```

**该示例格式是 [玩Android Api](https://www.wanandroid.com/blog/show/2)返回的数据格式，如果errorCode等于200 请求成功，否则请求失败
作为开发者的角度来说，我们主要是想得到脱壳数据-data，且不想每次都判断errorCode==0请求是否成功或失败
这时我们可以用livedata把数据结果从viewModel传递给activity，实现相关方法：**

``` kotlin

var itemNews: ObservableList<Data> = ObservableArrayList()

/**
*@param type 协程请求->直接获取结果的
*/
fun getDates(type: String) {

viewModelScope.launch {//协程
    val plants: MutableList<Plant> = ArrayList()
    val plant = Plant("1", "格调架构的数据库操作", "", 6, 7, "http//：.png")
    plants.add(plant)
    plantDao.insertAll(plants)
}

async({ repository.getNews(type) }, {
        itemNews.clear()
        itemNews.addAll(it.data)
}, {
it.printStackTrace()
}, {

})
```

- **4.3
在ViewModel中发起请求，所有请求都是在viewModelScope中启动，请求会发生在IO线程，最终回调在主线程上，当页面销毁的时候，请求会统一取消，不用担心内存泄露的风险，框架做了2种请求使用方式**

**1、将请求数据包装给ResultState，在Activity/Fragment中去监听ResultState拿到数据做处理**
**2、 直接在当前ViewModel中拿到请求结果**

``` kotlin
viewModel.mEventHub.observe(this) {//观察者
when (it.action) {
        LiveDataEvent.LOGIN_SUCCESS -> {//成功
            LogUtils.e(TAG + it.any)
        }
        LiveDataEvent.LOGIN_FAIL -> {//失败
            LogUtils.e(TAG + it.any)
        }
    }
}

```

### 注意：使用该请求方式时需要注意，如果该ViewModel并不是跟Activity/Fragment绑定的泛型ViewModel，而是

val mainViewModel : MainViewModel by viewModels()
或者 val mainViewModel : MainViewModel by activityViewModels()
获取的 如果请求时要弹出loading，你需要观察isLoading状态：

### stateView.isLoading.value = true

## 4.4 开启打印日志开关
``` kotlin
LogUtils.e()//可以快速定位错误代码的位置，单机连接跳转至错误代码
ThinkerLogger.getInstance().init(UriConfig.LOG_PATH)//日志输出本地
```

## 5.获取ViewModel

- **5.1我们的activity/fragment会有多个ViewModel，按传统的写法感觉有点累**

``` kotlin
val mainViewModel = ViewModelProvider(this,
ViewModelProvider.AndroidViewModelFactory(application)).get(MainViewModel::class.java)
```

## 6.写了一些常用的拓展函数

base类里有基本常用的扩展函数

``` kotlin
fun Activity.toast(msg: Int?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg!!, duration).show()
}

fun Fragment.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (TextUtils.isEmpty(msg)) {
        return
    }
    Toast.makeText(activity, msg!!, duration).show()
}
```

## 联系

- QQ交流群：750467053 
![QQ交流群](https://github.com/Primary-hacker1/MVVMJetpack/blob/main/aat/qrcode_1660787905159.png)


