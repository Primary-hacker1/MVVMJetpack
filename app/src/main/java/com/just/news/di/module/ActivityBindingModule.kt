package com.just.news.di.module

import com.just.news.MainActivity
import com.just.news.di.ActivityScoped
import com.just.news.di.FragmentScoped
import com.just.news.ui.activity.LoginActivity
import com.just.news.ui.fragment.*
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

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newLoginFragment(): LoginFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newLoginActivity(): LoginActivity

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newMainFragment(): MainFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newMeFragment(): MeFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun newSettingFragment(): SettingFragment

}