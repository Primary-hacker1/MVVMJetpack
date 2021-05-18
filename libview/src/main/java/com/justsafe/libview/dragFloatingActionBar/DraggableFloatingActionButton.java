package com.justsafe.libview.dragFloatingActionBar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DraggableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {

    private String TAG = "DraggableFloatingActionButton";
    private double mLastX, mLastY;
    private double mOriginX, mOriginY;

    private OnClickListener onClickListener;

    public DraggableFloatingActionButton(@NonNull Context context) {
        super(context);
        initView();
    }


    public DraggableFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public DraggableFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }


    private void initView() {
        setOnTouchListener(this);
    }


    /**
     * 处理拖拽事件
     *
     * @param v
     * @param event
     */
    private void handleDragging(View v, MotionEvent event) {
        double x = event.getRawX();
        double y = event.getRawY();
        Log.d(TAG, "===========handleDraging: " + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = x;
            mLastY = y;
            mOriginX = x;
            mOriginY = y;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            double dx = x - mLastX;
            double dy = y - mLastY;
            Log.d(TAG, "============handleDraging: dx==" + dx + ",dy==" + dy);
            //startAnimation(dx, dy);
            //moveMethod1(dx, dy);
            moveMethod2(dx, dy);
            mLastX = x;
            mLastY = y;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isClickArea(x,y)){
                if (onClickListener!=null){
                    onClickListener.onClick(v);
                }
                Log.d(TAG, "============handleDraging: ACTION_。。。。。。。。。。。。。。。==");

            }
            Log.d(TAG, "============handleDraging: ACTION_UP==");

        }
    }

    private boolean isClickArea(double x, double y) {

        double x_diff = Math.abs(x - mOriginX);
        double y_diff = Math.abs(y - mOriginY);
        if (x_diff < 10 && y_diff < 10) {
            return true;
        } else {
            return false;
        }
    }

    //根据属性动画的原理
    private void moveMethod2(double dx, double dy) {
        setTranslationX((float) (getTranslationX() + dx));
        setTranslationY((float) (getTranslationY() + dy));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        handleDragging(v,event);
        return true;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);

        void onDragging(View v);
    }
}
