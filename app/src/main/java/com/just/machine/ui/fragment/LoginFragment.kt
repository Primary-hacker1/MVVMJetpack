package com.just.machine.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.just.machine.model.DataStoreHelper
import com.just.machine.ui.activity.MainActivity
import com.just.news.databinding.FragmentLoginBinding
import com.just.machine.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 *create by 2024/6/19
 * 登录界面
 *@author zt
 */
@AndroidEntryPoint
class LoginFragment : CommonBaseFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun initView() {


        //防止多次点击创建多个setNoRepeatListener
        binding.btnLogin.setNoRepeatListener {

            if (binding.atvLoginAccount.text.toString().isEmpty()) {
                toast("请输入账户！")
                return@setNoRepeatListener
            }

            if (binding.atvLoginPassword.text.toString().isEmpty()) {
                toast("请输入密码！")
                return@setNoRepeatListener
            }

            lifecycleScope.launch {//存储ds
                DataStoreHelper.getInstance().saveUserName(binding.atvLoginAccount.text.toString())
            }

            lifecycleScope.launch {//存储ds
                DataStoreHelper.getInstance().saveUserName(binding.atvLoginPassword.text.toString())
            }

            MainActivity.startMainActivity(context)
            activity?.finish()
        }
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)
}