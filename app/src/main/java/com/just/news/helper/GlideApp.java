package com.just.news.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.just.news.R;


public class GlideApp {
    /*
     *加载图片(默认)
     */
    public static void loadImage(String url, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.icon) //占位图
                .error(R.mipmap.icon) //错误图
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .fitCenter();

        Glide.with(imageView.getContext().getApplicationContext()).load(url).apply(options).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.icon) //占位图
                .error(R.mipmap.icon) //错误图
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .fitCenter();

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadImage(Context context, Bitmap url, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.icon) //占位图
                .error(R.drawable.ic_launcher_background) //错误图
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate();

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void loadImage(Context context, int url, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.icon) //占位图
                .error(R.drawable.ic_launcher_background) //错误图
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate();

        Glide.with(context).load(url).apply(options).into(imageView);
    }

}
