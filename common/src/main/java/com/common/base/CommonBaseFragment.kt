package com.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import dagger.android.support.AndroidSupportInjection

abstract class CommonBaseFragment<VB : ViewDataBinding> : Fragment() {

    protected val TAG: String = CommonBaseFragment::class.java.simpleName

    private var mContext: Context? = null

    /**
     * 控件是否初始化完成
     */
    private var isViewCreated: Boolean = false

    /**
     * 是否加载过数据
     */
    private var isComplete: Boolean = false

    lateinit var binding: VB
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
        binding = DataBindingUtil.inflate(inflater, getLayout(), null, false)
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

    @LayoutRes
    abstract fun getLayout(): Int

    protected abstract fun initView()

    protected abstract fun loadData()

}