package com.zarddy.studyandroid.matrix.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.zarddy.library.util.LogUtils;
import com.zarddy.studyandroid.R;

/**
 * TODO 可变形ImageView
 * 参考：
 *      https://blog.csdn.net/lib739449500/article/details/50965122
 *      https://blog.csdn.net/nsgsbs/article/details/44628311
 */
public class MatrixImageView2 extends AppCompatImageView {

    /** 用户缩放，旋转，平移的矩阵 */
    private Matrix mMatrix = new Matrix();

    /** 图片四角触点的坐标 */
    private Point mLTPoint, // 左上
            mRTPoint, // 右上
            mLBPoint, // 左下
            mRBPoint; // 右下

    private Point mControlPoint = new Point();

    /** 触点图标 */
    private Drawable controlDrawable;

    private Context mContext;
    private DisplayMetrics mMetrics;

    public MatrixImageView2(Context context) {
        this(context, null);
    }

    public MatrixImageView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixImageView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        obtainStyledAttributes(attrs); // 获取自定义变量
        initView(); // 初始化控件
    }

    /**
     * TODO 获取自定义变量
     */
    private void obtainStyledAttributes(AttributeSet attrs) {

        mMetrics = getContext().getResources().getDisplayMetrics();

//        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MatrixImageView);
//
//
//        controlDrawable = typedArray.getDrawable(R.styleable.MatrixImageView_controlDrawable);
//
//
//
//        typedArray.recycle();
    }

    /**
     * TODO 初始化控件
     */
    private void initView() {
        LogUtils.i("initView . . . . ");

        if(controlDrawable == null){
            controlDrawable = getContext().getResources().getDrawable(R.drawable.shape_white_circle);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        LogUtils.i("onMeasure . . . . ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
//        controlDrawable.setBounds(mControlPoint.x - mDrawableWidth / 2,
//                mControlPoint.y - mDrawableHeight / 2, mControlPoint.x + mDrawableWidth
//                        / 2, mControlPoint.y + mDrawableHeight / 2);
//        controlDrawable.draw(canvas);

        LogUtils.i("onDraw . . . . ");

//
//        mPath.reset();
//        mPath.moveTo(mLTPoint.x, mLTPoint.y);
//        mPath.lineTo(mRTPoint.x, mRTPoint.y);
//        mPath.lineTo(mRBPoint.x, mRBPoint.y);
//        mPath.lineTo(mLBPoint.x, mLBPoint.y);
//        mPath.lineTo(mLTPoint.x, mLTPoint.y);
//        mPath.lineTo(mRTPoint.x, mRTPoint.y);
//        canvas.drawPath(mPath, mPaint);
//        //画旋转, 缩放图标
//
//        controlDrawable.setBounds(mControlPoint.x - mDrawableWidth / 2,
//                mControlPoint.y - mDrawableHeight / 2, mControlPoint.x + mDrawableWidth
//                        / 2, mControlPoint.y + mDrawableHeight / 2);
//        controlDrawable.draw(canvas);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        LogUtils.i("onLayout . . left . " + left + " . top: " + top + " . right: " + right + " . bottom: " + bottom);

        mLTPoint = new Point(left, top);
        mRTPoint = new Point(right, top);
        mRBPoint = new Point(right, bottom);
        mLBPoint = new Point(left, bottom);
    }
}
