package com.just.news.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.just.news.R
import com.just.news.databinding.FragmentNewBinding
import com.just.news.di.FragmentScoped
import com.just.news.model.Constants.succeed
import com.just.news.ui.adapter.NewAdapter
import com.just.news.ui.viewmodel.NewViewModel
import javax.inject.Inject

/**
 *create by 2020/6/19
 *@author zt
 */
@FragmentScoped
class NewFragment : CommonBaseFragment<FragmentNewBinding>() {

    @Inject
    lateinit var viewModel: NewViewModel

    private val adapter by lazy {
        NewAdapter(viewModel.itemNews, R.layout.item_new, 0)
    }

    fun newInstance(type: String): NewFragment {
        val args = Bundle()
        args.putString("type", type)
        val fragment = NewFragment()
        fragment.arguments = args
        return fragment
    }

    private fun initToolbar() {
        binding.toolbar.title = succeed//标题
        binding.toolbar.ivTitleBack.setOnClickListener {//返回
            val navController =  findNavController()//fragment返回数据处理
            navController.previousBackStackEntry?.savedStateHandle?.set("key","傻逼")
            navController.popBackStack()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_new
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initView() {
        initToolbar()
//        val type = arguments!!.getString("type", "")
//        viewModel.getRxNews(type)
//        binding.viewModel = viewModel
//        binding.recycleView.layoutManager = LinearLayoutManager(activity)
//        binding.recycleView.adapter = adapter
    }

    override fun loadData() {

    }
}