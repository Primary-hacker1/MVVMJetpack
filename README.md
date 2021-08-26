[![Platform][1]][2] [![GitHub license][3]][4]  [![GitHub license][5]][6]

[1]:https://img.shields.io/badge/platform-Android-blue.svg

[2]:https://github.com/hegaojian/JetpackMvvm

[3]:https://img.shields.io/github/release/hegaojian/JetpackMvvm.svg

[4]:https://github.com/hegaojian/JetpackMvvm/releases/latest

[5]:https://img.shields.io/badge/license-Apache%202-blue.svg

[6]:https://github.com/hegaojian/JetpackMvvm/blob/master/LICENSE

# :JetPackMvvm

- **基于MVVM模式集成谷歌官方推荐的JetPack组件库：LiveData、ViewModel、Lifecycle、Navigation、Retrofit、 RxJava、
  dagger2组件**
- **使用kotlin语言，添加大量拓展函数，简化代码**
- **加入Retrofit网络请求,协程，帮你简化各种操作，让你快速请求网络**

## 演示Demo

- **已经使用2年多时间优化和踩bug，现已经可以完全实现单一vm和单activity模式，快速开发，冗余代码几乎为0，解耦5.0分，之后会一直更新为了更高阶开发身法努力！**

[comment]: <> (#### 效果图展示)

[comment]: <> (![项目效果图]&#40;https://upload-images.jianshu.io/upload_images/9305757-818106225dd01e65.gif?imageMogr2/auto-orient/strip&#41;)

[comment]: <> (#### APK下载：)

[comment]: <> (- [Github下载]&#40;https://github.com/hegaojian/JetpackMvvm/releases/download/1.1.8/app-release.apk&#41;)

[comment]: <> (- [firm下载&#40;推荐&#41;]&#40;http://d.6short.com/v9q7&#41;)

[comment]: <> (- 扫码下载&#40;推荐&#41;)

[comment]: <> (![]&#40;https://upload-images.jianshu.io/upload_images/9305757-4999111e26d5e93a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240&#41;)

## 1.如何集成

- **1.1 在root's build.gradle中加入Jitpack仓库**

``` gradle
buildscript {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- **1.3 在app's build.gradle中，android 模块下开启DataBinding(如果你不想用DataBinding,请忽略这一步)**

``` gradle
AndroidStudio 4.0 以下版本------>
android {
    ...
    dataBinding {
        enabled = true 
    }
}

AndroidStudio 4.0及以上版本 ------>
android {
    ...
   buildFeatures {
        dataBinding = true
    }
}
 
```

## 2.继承基类

一般我们项目中都会有一套自己定义的符合业务需求的基类 ***CommonBaseActivity/CommonBaseFragment***，所以我们的基类需要**继承本框架的Base类**

**Activity：**

``` kotlin 
abstract class CommonBaseActivity<VB : ViewDataBinding>(
    private val layout: (LayoutInflater) -> VB//布局ID，省掉getlayout
) : AppCompatActivity() {

    protected val tag: String = CommonBaseActivity::class.java.simpleName//tag

    protected val binding by lazy { layout(layoutInflater) }//委托

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    protected abstract fun initView()//布局初始化

}
```

**Fragment：**

``` kotlin
abstract class CommonBaseFragment<VB : ViewDataBinding>(private val layout: (LayoutInflater) -> VB) : Fragment() {

    protected val TAG: String = CommonBaseFragment::class.java.canonicalName

    private var mContext: Context? = null

    protected val binding by lazy { layout(layoutInflater) }

    /**
     * 控件是否初始化完成
     */
    private var isViewCreated: Boolean = false

    /**
     * 是否加载过数据
     */
    private var isComplete: Boolean = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isViewCreated = true
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        lazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoad()
        }
    }

    protected open fun navigate(view: View, id: Int) {
        Navigation.findNavController(view).navigate(id)
    }

    protected open fun navigate(view: View, id: Int, bundle: Bundle?) {
        Navigation.findNavController(view).navigate(id, bundle)
    }

    /**
     * 懒加载
     */
    private fun lazyLoad() {
        if (userVisibleHint && isViewCreated && !isComplete) {
            //可见 或者 控件初始化完成 就 加载数据
            loadData()

            isComplete = true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        isComplete = false
    }

    protected abstract fun initView()

    protected abstract fun loadData()

}
```

## 3.编写一个登录功能

- **3.1 编写fragment_login.xml界面后转换成 databind 布局（鼠标停在根布局，Alt+Enter 点击提示 Convert to data binding
  layout即可）**

``` xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/tools">
    <data>
       
    </data>
    <LinearLayout>
       ....
    </LinearLayout>
 </layout>   
```

- **3.2 创建ViewModel类继承BaseViewModel**

``` xml
class LoginActivity : CommonBaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    @Inject
    lateinit var viewModel: NewViewModel

    companion object {
        /**
         * @param context -
         */
        fun startJUSTLoginActivity(context: Context) {
            if (BaseUtil.isFastDoubleClick()) return
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initView() {

    }

}
```

- **3.3 创建LoginFragment
  继承基类传入相关泛型,第一个泛型为你创建的LoginViewModel,第二个泛型为ViewDataBind，保存fragment_login.xml后databinding会生成一个FragmentLoginBinding类。（如果没有生成，试着点击Build->
  Clean Project）**

``` kotlin
@FragmentScoped
class LoginFragment : CommonBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    @Inject
    lateinit var viewModel: NewViewModel

    override fun initView() {
        binding.btnLogin.setOnClickListener {
            MainActivity.startMainActivity(context)
        }
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }
}
```

## 4.网络请求（Retrofit+协程或Rxjava）

- **4.1 新建请求配置类继承 BaseNetworkApi 示例：**

``` kotlin
          @FragmentScoped
          class NewFragment : CommonBaseFragment<FragmentNewBinding>() 
          
          
          class NewViewModel @Inject constructor(application: Application) : BaseViewModel(application) 
   - 网络请求的使用我们可以协程请求
   
           @GET("/nc/article/headline/{id}/0-10.html")
           suspend fun getNews(@Path("id") id : String?): NewResponses
           
         
  ```Java
      fun getNews(type: String) {
        async({ repository.getNews(type) }
            , {
                itemNews.clear()
                itemNews.addAll(it.list)
            }, {
                it.printStackTrace()
            }, {

            })
    }
 
  ```

  ```用RxJava请求
  ```Java
    fun getRxNews(type: String) {
        repository.getRxNews(type)
            .`as`(auto(this))
            .subscribes({

            },{

            })
   ```

  ```subscribes是自定义的一个扩展函数
   ```Java
   fun <T> SingleSubscribeProxy<T>.subscribes(onSuccess: (T) -> Unit,
                                            onError: (BaseResponseThrowable)->Unit) {
    ObjectHelper.requireNonNull(onSuccess, "onSuccess is null")
    ObjectHelper.requireNonNull(onError, "onError is null")
    val observer: RequestObserver<T> = RequestObserver(onSuccess, onError)
    subscribe(observer)
}
```

- **4.2返回和传递实体例如:**

``` kotlin
data class Tag(
    val data: List,
    val errorCode: String,
    val errorMsg: String
)
```

该示例格式是 [玩Android Api](https://www.wanandroid.com/blog/show/2)返回的数据格式，如果errorCode等于200 请求成功，否则请求失败
作为开发者的角度来说，我们主要是想得到脱壳数据-data，且不想每次都判断errorCode==0请求是否成功或失败
这时我们可以用livedata把数据结果从viewModel传递给activity，实现相关方法：

``` kotlin
class NewViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var repository: UserRepository

    fun isSave() {
        val mDataMap: MutableMap<String, String> = HashMap()
        mDataMap[creator] = ProfileManager.getInstance().mdm
        async({ repository.getSave(mDataMap) }, { bean ->
            if (bean.code == SEND) {
                mEventHub.value = LiveDataEvent(
                    IsSave,
                    bean.data.status,
                    bean.data.list
                )
            } else {
                mEventHub.value = LiveDataEvent(
                    LOGIN_FAIL,
                    bean.msg
                )
            }

        }, {
            if (ThrowableHandler.isNetworkAvailable(getApplication())) {
                mEventHub.setValue(
                    LiveDataEvent(
                        LOGIN_FAIL,
                        wifeFeedBack
                    )
                )
            } else {
                mEventHub.setValue(
                    LiveDataEvent(
                        LOGIN_FAIL,
                        "服务器异常，请联系管理员"
                    )
                )
            }
        }, {
        })
    }
}
```

- **4.3
  在ViewModel中发起请求，所有请求都是在viewModelScope中启动，请求会发生在IO线程，最终回调在主线程上，当页面销毁的时候，请求会统一取消，不用担心内存泄露的风险，框架做了2种请求使用方式**

**1、将请求数据包装给ResultState，在Activity/Fragment中去监听ResultState拿到数据做处理**
**2、 直接在当前ViewModel中拿到请求结果**

``` kotlin
public class LoginFragment1 extends CommonBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    @Inject
    NewViewModel viewModel;

    @Override
    protected void loadData() {//懒加载

    }

    @Override
    protected void initView() {
        initObserve();
    }

    private void initObserve() {//liveData的观察数据变化监听
        viewModel.getEventHub().observe(this,
                liveDataEvent -> {
                    if (liveDataEvent.action == LiveDataEvent.LOGIN_SUCCESS) {//登录成功跳转主页
                        Activity activity = getActivity();
                        Context context = getContext();
                        if (context != null) {
                            if (activity != null) {
                                if (ProfileManager.getInstance().getUserId().equals("")) {
                                    LoginActivity1.startJUSTLoginActivity(context);
                                } else {
                                    MainActivity.Companion.startMainActivity(context);
                                }
                                activity.overridePendingTransition(R.anim.slide_left_in,
                                        R.anim.slide_right_out);
                                activity.finish();
                            }
                        }
                    } else if (liveDataEvent.action == LiveDataEvent.LOGIN_FAIL) {//登陆失败返回
                        ToastUtils.showShort(liveDataEvent.object.toString());
                    }
                });
    }
}
fragment同上
```

### 注意：使用该请求方式时需要注意，如果该ViewModel并不是跟Activity/Fragment绑定的泛型ViewModel，而是

val mainViewModel:MainViewModel by viewModels()
或者 val mainViewModel：MainViewModel by activityViewModels()
获取的 如果请求时要弹出loading，你需要观察isLoading状态：

### stateView.isLoading.value = true

## 4.4 开启打印日志开关

LogUtils.e()可以快速定位错误代码的位置，单机连接跳转至错误代码

## 5.获取ViewModel

- **5.1我们的activity/fragment会有多个ViewModel，按传统的写法感觉有点累**

``` kotlin
 val mainViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application)).get(MainViewModel::class.java)
```

## 6.写了一些常用的拓展函数

``` kotlin
   base类里有基本常用的扩展函数
   fun Activity.toast(msg: Int?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg!!, duration).show()
}
```

## 感谢

- [Jetpack-MVVM-Best-Practice](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice)
- [重学安卓](https://xiaozhuanlan.com/kunminx?rel=8184827882)
  [MVVMLin](https://github.com/AleynP/MVVMLin)

## 联系

- QQ交流群：773699239

## License

``` license
 Copyright 2021, zhangtao(张涛)       
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at 
 
       http://www.apache.org/licenses/LICENSE-2.0 

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
   
