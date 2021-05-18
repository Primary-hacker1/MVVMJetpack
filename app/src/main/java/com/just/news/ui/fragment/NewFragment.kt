package com.just.news.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import com.common.base.CommonBaseFragment
import com.just.news.R
import com.just.news.databinding.FragmentNewBinding
import com.just.news.di.FragmentScoped
import com.just.news.model.Constants.news
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

    private fun initToolbar() {
        binding.toolbar.title = news//标题
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE
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