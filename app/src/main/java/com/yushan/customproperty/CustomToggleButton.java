package com.yushan.customproperty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by beiyong on 2017-5-11.
 */

public class CustomToggleButton extends View {

    private OnStateChangedListener mOnStateChangedListener;
    /**
     * 滑块的背景图片
     */
    private Bitmap slideBackground;
    /**
     * 滑块icon
     */
    private Bitmap slideIcon;
    private int slideBackgroundWidth;
    private int slideBackgroundHeight;
    private int slideIconWidth;
    private int slideIconHeight;
    private float left;
    /**
     * 滑块left位置的最大值
     */
    private int slideIconMaxLeft;
    private final Boolean isOpen;
    private final int slideBackgroundId;
    private final int slideIconId;

    public CustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 读取自定义属性
        String nameSpace = "http://schemas.android.com/apk/res-auto";

        isOpen = attrs.getAttributeBooleanValue(nameSpace, "state", false);
        setState(isOpen);

        slideBackgroundId = attrs.getAttributeResourceValue(nameSpace, "slide_background", -1);
        slideIconId = attrs.getAttributeResourceValue(nameSpace, "slide_icon", -1);
        if (slideBackgroundId != -1 && slideIconId != -1) {
            setSlideBackground(slideBackgroundId, slideIconId);
        }

        Log.e("yushan", isOpen + ":" + slideBackgroundId + ":" + slideIconId);
    }

    /**
     * 设置按钮图片
     *
     * @param slideBackgroundId 滑块的背景图片
     * @param slideIconId       滑块icon
     */
    public void setSlideBackground(int slideBackgroundId, int slideIconId) {
        slideBackground = BitmapFactory.decodeResource(getResources(), slideBackgroundId);
        slideIcon = BitmapFactory.decodeResource(getResources(), slideIconId);
        slideBackgroundWidth = slideBackground.getWidth();
        slideBackgroundHeight = slideBackground.getHeight();

        slideIconWidth = slideIcon.getWidth();
        slideIconHeight = slideIcon.getHeight();

        slideIconMaxLeft = slideBackgroundWidth - slideIconWidth;
    }

    /**
     * 设置开关按钮的状态
     *
     * @param state true为打开，false为关闭
     */
    public void setState(boolean state) {
        if (state) {
            left = slideIconMaxLeft;
        } else {
            left = 0;
        }
        invalidate();
    }

    public void setOnStateChangedListener(OnStateChangedListener mOnStateChangedListener) {
        this.mOnStateChangedListener = mOnStateChangedListener;
    }

    /**
     * 状态改变的监听器
     */
    public interface OnStateChangedListener {
        /**
         * 状态改变时，回调方法
         *
         * @param state
         */
        void onStateChanged(boolean state);
    }

    /**
     * 测量View的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 设置View的测量的宽和高，设置MyToggleButton的大小和滑块背景图片一样的大小
        setMeasuredDimension(slideBackgroundWidth, slideBackgroundHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 把背景画到ToggleButton的左上角
        canvas.drawBitmap(slideBackground, 0, 0, null);

        // 预防超出范围
        if (left < 0) {
            left = 0;
        } else if (left > slideIconMaxLeft) {
            // 滑块往右移动时，滑块left的最大值 = 背景宽 – 滑块宽
            left = slideIconMaxLeft;
        }

        Log.e("yushan", "" + left);
        canvas.drawBitmap(slideIcon, left, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 滑动的时候计算滑块left = 触摸位置event.getX() – 滑块宽 / 2
                left = event.getX() - slideIconWidth / 2;
                break;
            case MotionEvent.ACTION_MOVE:
                // 滑动的时候计算滑块left = 触摸位置event.getX() – 滑块宽 / 2
                left = event.getX() - slideIconWidth / 2;
                break;
            case MotionEvent.ACTION_UP:
                // 手指松开时，计算滑块应该滑到最左边，还是滑到最右边：
                if (event.getX() < slideBackgroundWidth / 2) {
                    // 如果手指抬起的位置 < 背景宽 / 2，把滑块滑到最左边
                    left = 0;
                    mOnStateChangedListener.onStateChanged(false);
                } else {
                    // 否则滑到最右边
                    left = slideIconMaxLeft;
                    mOnStateChangedListener.onStateChanged(true);
                }
                break;
        }
        invalidate();
        return true;
    }

}
