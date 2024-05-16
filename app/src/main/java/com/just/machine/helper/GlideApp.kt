package com.just.machine.helper

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.just.news.R

object GlideApp {
    /*
     *加载图片(默认)
     */
    fun loadImage(url: String?, imageView: ImageView) {
        val options = RequestOptions().centerCrop().placeholder(R.mipmap.logo) //占位图
            .error(R.mipmap.logo) //错误图
            // .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .fitCenter()

        Glide.with(imageView.context.applicationContext).load(url).apply(options).into(imageView)
    }

    fun loadImage(context: Context?, url: String?, imageView: ImageView?) {
        val options = RequestOptions().centerCrop().placeholder(R.mipmap.logo) //占位图
            .error(R.mipmap.logo) //错误图
            // .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .fitCenter()

        Glide.with(context!!).load(url).apply(options).into(imageView!!)
    }

    fun loadImage(context: Context?, url: Bitmap?, imageView: ImageView?) {
        val options = RequestOptions().centerCrop().placeholder(R.mipmap.logo) //占位图
            .error(R.drawable.ic_launcher_background) //错误图
            // .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()

        Glide.with(context!!).load(url).apply(options).into(imageView!!)
    }

    fun loadImage(context: Context?, url: Int, imageView: ImageView?) {
        val options = RequestOptions().centerCrop().placeholder(R.mipmap.logo) //占位图
            .error(R.drawable.ic_launcher_background) //错误图
            // .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()

        Glide.with(context!!).load(url).apply(options).into(imageView!!)
    }
}
