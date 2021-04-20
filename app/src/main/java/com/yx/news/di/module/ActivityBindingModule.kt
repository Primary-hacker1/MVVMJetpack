package com.yx.news.di.module

import com.yx.news.MainActivity
import com.yx.news.di.ActivityScoped
import com.yx.news.di.FragmentScoped
import com.yx.news.ui.activity.LoginActivity
import com.yx.news.ui.activity.SucceedPrisonActivity
import com.yx.news.ui.fragment.LoginFragment
import com.yx.news.ui.fragment.NewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newFragment(): NewFragment

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun succeedActivity(): SucceedPrisonActivity

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newLoginFragment(): LoginFragment
}