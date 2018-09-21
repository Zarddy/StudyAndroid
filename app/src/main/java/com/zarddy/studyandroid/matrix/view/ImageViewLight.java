package com.zarddy.studyandroid.matrix.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * TODO 原来配灯图片控件
 */
public class ImageViewLight extends AppCompatImageView {

    public ImageViewLight(Context context) {
        super(context);
        init();
    }

    public ImageViewLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageViewLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 移动触发灯饰
    int screenWidth;
    int screenHeight;
    int lastX;
    int lastY;

    /** 按下时的位置控件相对屏幕左上角的位置X */
    private int startDownX;
    /** 按下时的位置控件距离屏幕左上角的位置Y */
    private int startDownY;

    int defaultWidth = 150;//灯的默认宽度
    int defaultHeight = 150;//灯的默认高度

    public void init() {
        // 灯饰-移动的范围
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;

        this.setAdjustViewBounds(true);
        this.setClickable(true);
        this.setScaleType(ScaleType.FIT_CENTER);
        this.setBackgroundColor(0xA1B2C3);
        this.setAdjustViewBounds(true);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //计算灯的宽高比
        float bl = defaultWidth / defaultHeight;
        //设置灯的宽度为屏幕的5分之1
        defaultWidth = screenWidth/5;
        //根据宽高比计算灯的高度
        defaultHeight = (int)(screenWidth/5*bl);

        params.width = defaultWidth;
        params.height = defaultHeight;
        params.setMargins(screenWidth/2 -  defaultWidth/2 , 40, 0, 0); // 屏幕居中
        this.setLayoutParams(params);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startDownX = lastX = (int) event.getRawX();
                startDownY = lastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 每次移动都要设置其layout，不然由于父布局可能嵌套listview，当父布局发生改变重绘（如下拉刷新时）则移动的view会回到原来的位置
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getLeft();
                params.topMargin = getTop();
                params.width = getWidth();
                params.height = getHeight();
                params.setMargins(getLeft(), getTop(), 0, 0);
                setLayoutParams(params);
                break;
        }

        return super.onTouchEvent(event);
    }

    public void updateImgScale(float bl,Boolean jia_jian ) {
        defaultWidth = (int)(defaultWidth*bl);
        defaultHeight = (int)(defaultHeight*bl);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.width = defaultWidth;
        params.height = defaultHeight;
        params.setMargins(getLeft(), getTop(), 0, 0);
        this.setLayoutParams(params);

    }
}
