package com.common.base

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

abstract class CommonBaseFragment<VB : ViewDataBinding> : Fragment() {

    protected val TAG: String = CommonBaseFragment::class.java.simpleName

    private var mContext: Context? = null


    private lateinit var _binding: VB

    protected val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isViewCreated = true
        _binding = getViewBinding(inflater, container)
        return _binding.root
    }

    /**
     * 控件是否初始化完成
     */
    private var isViewCreated: Boolean = false

    /**
     * 是否加载过数据
     */
    private var isComplete: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
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


    protected abstract fun loadData()

    protected abstract fun initView()

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /**
     * @param token - 获取InputMethodManager，隐藏软键盘
     */
    open fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im = mContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}