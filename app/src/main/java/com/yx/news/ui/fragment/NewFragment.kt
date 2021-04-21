package com.yx.news.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.yx.news.R
import com.yx.news.databinding.FragmentNewBinding
import com.yx.news.di.FragmentScoped
import com.yx.news.ui.adapter.NewAdapter
import com.yx.news.ui.viewmodel.NewViewModel
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

    override fun getLayout(): Int {
        return R.layout.fragment_new
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initView() {
//        val type = arguments!!.getString("type", "")
//        viewModel.getRxNews(type)
//        binding.viewModel = viewModel
//        binding.recycleView.layoutManager = LinearLayoutManager(activity)
//        binding.recycleView.adapter = adapter
    }

    override fun loadData() {

    }
}