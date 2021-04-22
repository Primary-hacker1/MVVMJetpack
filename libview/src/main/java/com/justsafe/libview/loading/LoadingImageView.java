package com.justsafe.libview.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.appcompat.widget.AppCompatImageView;

import com.justsafe.libview.R;


/**
 * 加载小菊花
 */
public class LoadingImageView extends AppCompatImageView {


    private RotateAnimation rotateAnimation = null;

    public LoadingImageView(Context context) {
        super(context);
        init(context);
    }


    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {

    }


    private RotateAnimation rotate() {
        RotateAnimation rotateAnimation;
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setStartTime(0);
        rotateAnimation.setDuration(1500);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatCount(Integer.MAX_VALUE);
        return rotateAnimation;
    }

    public void show() {
        this.setVisibility(VISIBLE);
        setBackgroundResource(R.drawable.ic_loading);
        rotateAnimation = rotate();
        this.setAnimation(rotateAnimation);
    }

    public void dismiss() {
        if (rotateAnimation != null) {
            rotateAnimation.reset();
        }
        setBackground(null);
        this.setVisibility(GONE);

    }



}
