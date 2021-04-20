package com.yx.news.ui.activity;


import com.common.base.CommonBaseActivity;
import com.yx.news.R;
import com.yx.news.databinding.ActivitySucceedPrisonBinding;
import com.yx.news.model.Constants;


/**
 * create by 2021/4/20
 * 交班界面
 *
 * @author zt
 */
public class SucceedPrisonActivity extends CommonBaseActivity<ActivitySucceedPrisonBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_succeed_prison;
    }

    @Override
    protected void initView() {
        binding.toolbar.setTitle(Constants.succeed);//标题
//        setTitle(Constants.succeed);
    }

}
