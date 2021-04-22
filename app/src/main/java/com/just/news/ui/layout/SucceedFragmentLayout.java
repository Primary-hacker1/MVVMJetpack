package com.just.news.ui.layout;

import android.content.Context;
import android.util.AttributeSet;


import com.common.base.BaseFrameLayout;
import com.just.news.databinding.FragmelayoutSucceedBinding;
import com.just.news.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SucceedFragmentLayout extends BaseFrameLayout<FragmelayoutSucceedBinding> {


    public SucceedFragmentLayout(@NotNull Context context) {
        super(context);
    }

    public SucceedFragmentLayout(@NotNull Context context, @NotNull AttributeSet attributes) {
        super(context, attributes);
    }

    public SucceedFragmentLayout(@NotNull Context context, @NotNull AttributeSet attributes, int i) {
        super(context, attributes, i);
    }

    public void initView() {
        initData();//第一次进来初始化
    }

    /**
     * @param bean -有交班记录进来
     */
    public void setData(List bean) {

    }

    private void initData() {
        binding.saveImageMatrix.setText("hellow");
    }

    /**
     * @param bean -选择人员返回进来
     * @param bean -手机号
     * @param bean -姓名
     * @param bean -警号
     * @param bean -组织
     */
    private void initAdd(List bean) {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragmelayout_succeed;
    }
}
