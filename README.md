## 项目架构组成
  为 mvvm+ okhttp+ retrofit+ RxJava+ LiveData+ dagger+ 协程RxJava在本文中是多一个使用方式和协程并不冲突 但是有点显得多余，可以自由选择
## 框架简介
-  基于MVVM模式用了 kotlin+协程+retrofit+livedata+DataBinding，是基于Androidx
  主要封装了BaseActivity、BaseFragment、BaseViewModel、BaseLifeViewModel，基于协程和rxjava网络请方式更加方便，可自由选择
-  使用Rxjava 处理不好的话会有内存泄露的风险，这里使用AutoDispose，在Androidx中 activity和fragment中可以直接直接使用，但是在viewmodel中不能，所以在BaseLifeViewModel中是处理rxjava的封装
## 开始使用
### 1.开启databinding，在app文件下build.gradle中引入
    dataBinding {
      enabled true
    }
### 2.依赖库
   目前没有线上引入，可直接下载common 作为库引入，里面的协程可根本实际需求封装
   
### 3.开始开发
   项目中采用了dagger 所以在业务层首先dagger，在common下已经引入了dagger但是在app的build.gradle下要引入dagger的注解插件否则会报错
   具体使用见代码，当然喜欢dagger的可以直接不用
     
    kapt 'com.google.dagger:dagger-compiler:2.17' // 注解处理器
    kapt 'com.google.dagger:dagger-android-processor:2.17'
#### 3.1 
   - activity或者fragment分别继承baseActivity和baseFragment，业务层viewModel继承baseViewMOdel
   
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
  - 用RxJava请求
  ```Java
    fun getRxNews(type: String) {
        repository.getRxNews(type)
            .`as`(auto(this))
            .subscribes({

            },{

            })
   ```
   subscribes是自定义的一个扩展函数
   ```Java
   fun <T> SingleSubscribeProxy<T>.subscribes(onSuccess: (T) -> Unit,
                                            onError: (BaseResponseThrowable)->Unit) {
    ObjectHelper.requireNonNull(onSuccess, "onSuccess is null")
    ObjectHelper.requireNonNull(onError, "onError is null")
    val observer: RequestObserver<T> = RequestObserver(onSuccess, onError)
    subscribe(observer)
}
```
### 4 示列
   代码中是以网易新闻api为接口
   ![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8f7c77be61cb43e18a2c5b232e6dc5f8~tplv-k3u1fbpfcp-zoom-1.image)
### 5 更多介绍
   [掘金](https://juejin.im/post/6873448411727986701)
   [csdn](https://blog.csdn.net/qq_30710615/article/details/108649677)
### 6 最后
   后续会逐渐完善，旨建立一个轻量级的mvvm开发框架，希望会给你带来帮助，也喜欢可以留下你的宝贵意见
   #### 感谢[MVVMLin](https://github.com/AleynP/MVVMLin)
   
   
