package com.justsafe.libview.cornerlabelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.justsafe.libview.R;

public class CornerLabelView extends View {
    private int mHalfWidth;//View宽度的一半
    private Paint mPaint;//角标画笔
    private TextPaint mTextPaint;//文字画笔
    private Path mPath;//角标路径

    private int position;//角标位置，0：右上角、1：右下角、2：左下角、3：左上角
    //角标的显示边长
    private int sideLength = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    //字体大小
    private int textSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
    //字体颜色
    private int textColor = Color.WHITE;
    private String text;
    //角标背景
    private int bgColor = Color.RED;
    //文字到斜边的距离
    private int marginLeanSide = 10;

    public CornerLabelView(Context context) {
        this(context, null);
    }

    public CornerLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CornerLabelView, 0, 0);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.CornerLabelView_position) {
                position = ta.getInt(attr, 0);
            } else if (attr == R.styleable.CornerLabelView_side_length) {
                sideLength = ta.getDimensionPixelSize(attr, sideLength);
            } else if (attr == R.styleable.CornerLabelView_text_size) {
                textSize = ta.getDimensionPixelSize(attr, textSize);
            } else if (attr == R.styleable.CornerLabelView_text_color) {
                textColor = ta.getColor(attr, textColor);
            } else if (attr == R.styleable.CornerLabelView_text) {
                text = ta.getString(attr);
            } else if (attr == R.styleable.CornerLabelView_bg_color) {
                bgColor = ta.getColor(attr, bgColor);
            } else if (attr == R.styleable.CornerLabelView_margin_lean_side) {
                marginLeanSide = ta.getDimensionPixelSize(attr, marginLeanSide);
            }
        }
        ta.recycle();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(bgColor);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(sideLength * 2, sideLength * 2);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(heightSpecSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, widthSpecSize);
        } else if (widthSpecSize != heightSpecSize) {
            int size = Math.min(widthSpecSize, heightSpecSize);
            setMeasuredDimension(size, size);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHalfWidth = Math.min(w, h) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将原点移动到画布中心
        canvas.translate(mHalfWidth, mHalfWidth);
        //根据角标位置旋转画布
        canvas.rotate(position * 90);

        if (sideLength > mHalfWidth * 2) {
            sideLength = mHalfWidth * 2;
        }

        //绘制角标背景
        mPath.moveTo(-mHalfWidth, -mHalfWidth);
        mPath.lineTo(sideLength - mHalfWidth, -mHalfWidth);
        mPath.lineTo(mHalfWidth, mHalfWidth - sideLength);
        mPath.lineTo(mHalfWidth, mHalfWidth);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        //绘制文字前画布旋转45度
        canvas.rotate(90);
        //角标实际高度
        int h1 = (int) (Math.sqrt(2) / 2.0 * sideLength);
        int h2 = (int) -(mTextPaint.ascent() + mTextPaint.descent());
        //文字绘制坐标
        int x = (int) -mTextPaint.measureText(text) / 2 - 30;
        int y;
        if (marginLeanSide >= 0) { //使用clv:margin_lean_side属性时
            if (position == 1 || position == 2) {
                if (h1 - (marginLeanSide - mTextPaint.ascent()) < (h1 - h2) / 2) {
                    y = -(h1 - h2) / 2;
                } else {
                    y = (int) -(h1 - (marginLeanSide - mTextPaint.ascent()));
                }
            } else {
                if (marginLeanSide < mTextPaint.descent()) {
                    marginLeanSide = (int) mTextPaint.descent();
                }

                if (marginLeanSide > (h1 - h2) / 2) {
                    marginLeanSide = (h1 - h2) / 2;
                }
                y = -marginLeanSide;
            }
        } else { //默认情况下
            if (sideLength > mHalfWidth) {
                sideLength = mHalfWidth;
            }
            y = (int) (-Math.sqrt(2) / 2.0 * sideLength + h2) / 2;
        }

        //如果角标在右下、左下则进行画布平移、翻转，已解决绘制的文字显示问题
        if (position == 1 || position == 2) {
            canvas.translate(0, (float) (-Math.sqrt(2) / 2.0 * sideLength));
            canvas.scale(-1, -1);
        }
        //绘制文字
        canvas.drawText(text, x, y, mTextPaint);
    }

    /**
     * 设置角标背景色
     *
     * @param bgColorId
     * @return
     */

    public CornerLabelView setBgColorId(int bgColorId) {
        this.bgColor = getResources().getColor(bgColorId);
        mPaint.setColor(bgColor);
        invalidate();
        return this;
    }

    /**
     * 设置角标背景色
     *
     * @param bgColor
     * @return
     */
    public CornerLabelView setBgColor(int bgColor) {
        mPaint.setColor(bgColor);
        invalidate();
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param colorId
     * @return
     */
    public CornerLabelView setTextColorId(int colorId) {
        this.textColor = getResources().getColor(colorId);
        mTextPaint.setColor(textColor);
        invalidate();
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param color
     * @return
     */
    public CornerLabelView setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
        return this;
    }

    /**
     * 设置文字
     *
     * @param textId
     * @return
     */
    public CornerLabelView setText(int textId) {
        this.text = getResources().getString(textId);
        invalidate();
        return this;
    }

    /**
     * 设置文字
     *
     * @param text
     * @return
     */
    public CornerLabelView setText(String text) {
        this.text = text;
        invalidate();
        return this;
    }
}
