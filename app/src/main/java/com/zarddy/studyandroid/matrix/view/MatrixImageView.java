package com.zarddy.studyandroid.matrix.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zarddy.library.util.BitmapUtils;
import com.zarddy.library.util.GraphicsUtils;
import com.zarddy.library.util.LogUtils;
import com.zarddy.library.util.ScreenUtils;
import com.zarddy.studyandroid.R;
import com.zarddy.studyandroid.opencv.OpenCVHelper;
import com.zarddy.studyandroid.opencv.entity.PointMargin;

/**
 * TODO 可以变形的ImageView
 * 参考：
 * https://blog.csdn.net/lib739449500/article/details/50965122
 * https://blog.csdn.net/nsgsbs/article/details/44628311
 */
public class MatrixImageView extends View {

    /** 图片的最大缩放比例 */
    public static final float MAX_SCALE = 4.0f;
    /** 图片的最小缩放比例 */
    public static final float MIN_SCALE = 0.3f;

    /**
     * 一些默认的常量
     */
    public static final int DEFAULT_FRAME_PADDING = 8;
    public static final int DEFAULT_FRAME_WIDTH = 0;
    public static final float DEFAULT_SCALE = 1.0f;
    public static final float DEFAULT_DEGREE = 0;
    public static final boolean DEFAULT_EDITABLE = true;

    /** 用于旋转缩放的Bitmap */
    private Bitmap mBitmap;

    /** 当前控件的中心坐标，该点的坐标相对于其父类布局 */
    private PointF mCenterPoint = new PointF();

    /**
     * View的宽度和高度，随着图片的旋转而变化(不包括控制旋转，缩放图片的宽高)
     */
    private int mViewWidth, mViewHeight;

    /** 图片的旋转角度 */
    private float mOriginalDegree = DEFAULT_DEGREE;
    private float mCurDegree = mOriginalDegree; // 当前角度

    /** 图片的缩放比例 */
    private float mScale = DEFAULT_SCALE;

    /** 用于缩放，旋转，平移的矩阵 */
    private Matrix mMatrix = new Matrix();

    /** 距离父类布局的左间距 */
    private int mViewPaddingLeft;

    /** 距离父类布局的上间距 */
    private int mViewPaddingTop;

    /**
     * 图片四个点坐标，用于缩放，旋转的控制点的坐标
     */
    private Point mLTPoint;
    private Point mRTPoint;
    private Point mRBPoint;
    private Point mLBPoint;
    private List<Point> mControllerPoints = new ArrayList<>();

    /** 用于缩放，旋转的图标 */
    private Drawable controlDrawable;

    /** 缩放，旋转图标的宽和高 */
    private int mControllerDrawableWidth, mControllerDrawableHeight;

    /** 初始状态 */
    private static final int STATUS_INIT = 0;
    /** 拖动状态 */
    private static final int STATUS_DRAG = 1;
    /** 旋转或者放大状态 */
    private static final int STATUS_ROTATE_ZOOM = 2;
    /** 变形状态 */
    private static final int STATUS_TRANSFORM = 3;

    /** 当前所处的状态 */
    private int mStatus = STATUS_INIT;

    /** 外边框与图片之间的间距, 单位是dip */
    private int framePadding = DEFAULT_FRAME_PADDING;

    /** 是否处于可以缩放，平移，旋转状态 */
    private boolean isTransformable = true; // 是否可变形

    private DisplayMetrics metrics;

    /**
     * 图片在旋转时x方向的偏移量
     */
    private int offsetX;
    /**
     * 图片在旋转时y方向的偏移量
     */
    private int offsetY;

    private PointF mFirstOriginalPoint = new PointF();
    private PointF mSecondOriginalPoint = new PointF();


    private Rect mLTBounds = new Rect();
    private Rect mRTBounds = new Rect();
    private Rect mLBBounds = new Rect();
    private Rect mRBBounds = new Rect();

    private ArrayList<PointMargin> mPointsMargin = new ArrayList<>(); // 四角点间距

    private Point curMovingControllerPoint = new Point(); // 当前移动的控制点
    private Point preMovingControllerPoint = new Point();

    public MatrixImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixImageView(Context context) {
        this(context, null);
    }

    public MatrixImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        obtainStyledAttributes(attrs);
        init();
    }

    /**
     * 获取自定义属性
     * @param attrs
     */
    private void obtainStyledAttributes(AttributeSet attrs){
        metrics = getContext().getResources().getDisplayMetrics();
        framePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FRAME_PADDING, metrics);

        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.MatrixImageView);

        Drawable srcDrawble = mTypedArray.getDrawable(R.styleable.MatrixImageView_src);
        mBitmap = BitmapUtils.drawable2Bitmap(srcDrawble);

        framePadding = mTypedArray.getDimensionPixelSize(R.styleable.MatrixImageView_framePadding, framePadding);
        mScale = mTypedArray.getFloat(R.styleable.MatrixImageView_scale, DEFAULT_SCALE);
        mOriginalDegree = mTypedArray.getFloat(R.styleable.MatrixImageView_degree, DEFAULT_DEGREE);
        mCurDegree = mOriginalDegree;
        controlDrawable = mTypedArray.getDrawable(R.styleable.MatrixImageView_controlDrawable);
        isTransformable = mTypedArray.getBoolean(R.styleable.MatrixImageView_editable, DEFAULT_EDITABLE);

        mTypedArray.recycle();
    }

    // 初始化控件
    private void init(){

        mMatrix.reset();
        mCurDegree = 0;
        mScale = 1f;

        mPointsMargin.clear();
        mPointsMargin.add(new PointMargin());
        mPointsMargin.add(new PointMargin());
        mPointsMargin.add(new PointMargin());
        mPointsMargin.add(new PointMargin());

        if(controlDrawable == null){
            controlDrawable = getContext().getResources().getDrawable(R.drawable.shape_white_circle);
        }

        mControllerDrawableWidth = controlDrawable.getIntrinsicWidth();
        mControllerDrawableHeight = controlDrawable.getIntrinsicHeight();

        transformDraw(mCurDegree);

        if(mBitmap == null)
            return;

        // 左上角
        mLTBounds.set(0,
                0,
                mControllerDrawableWidth,
                mControllerDrawableHeight);

        // 左下角
        mLBBounds.set(0,
                this.getHeight() - mControllerDrawableHeight,
                mControllerDrawableWidth,
                this.getHeight());

        // 右上角
        mRTBounds.set(this.getWidth() - mControllerDrawableWidth,
                0,
                this.getWidth(),
                mControllerDrawableHeight);

        // 右下角
        mRBBounds.set(this.getWidth() - mControllerDrawableWidth,
                this.getHeight() - mControllerDrawableHeight,
                this.getWidth(),
                this.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 将屏幕中点坐标设置为控件中点
        mCenterPoint.set(
                ScreenUtils.getScreenWidth(getContext()) / 2,
                ScreenUtils.getScreenHeight(getContext()) / 2
        );
    }

    /**
     * 调整View的大小，位置
     */
    private void adjustLayout(){
        int actualWidth = mViewWidth + mControllerDrawableWidth;
        int actualHeight = mViewHeight + mControllerDrawableHeight;

        int newPaddingLeft = (int) (mCenterPoint.x - actualWidth /2);
        int newPaddingTop = (int) (mCenterPoint.y - actualHeight/2);

        if(mViewPaddingLeft != newPaddingLeft || mViewPaddingTop != newPaddingTop){
            mViewPaddingLeft = newPaddingLeft;
            mViewPaddingTop = newPaddingTop;
        }

        this.layout(newPaddingLeft, newPaddingTop, newPaddingLeft + actualWidth, newPaddingTop + actualHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //每次draw之前调整View的位置和大小
        super.onDraw(canvas);

        if(mBitmap == null) return;

        canvas.drawBitmap(OpenCVHelper.warpPerspective(mBitmap, mPointsMargin), mMatrix, null);
        preMovingControllerPoint.set(curMovingControllerPoint.x, curMovingControllerPoint.y);

        //处于可编辑状态才画边框和控制图标
        if(isTransformable){
            //画旋转, 缩放图标
            int halfControllerDrawableWidth = mControllerDrawableWidth / 2;
            int halfControllerDrawableHeight = mControllerDrawableHeight / 2;

            // 左上角
            // setBounds：drawable将被绘制在canvas的哪个矩形区域内
            controlDrawable.setBounds(mLTPoint.x - halfControllerDrawableWidth,
                    mLTPoint.y - halfControllerDrawableHeight,
                    mLTPoint.x + halfControllerDrawableWidth,
                    mLTPoint.y + halfControllerDrawableHeight);
            controlDrawable.draw(canvas);

            // 左下角
            controlDrawable.setBounds(mLBPoint.x - halfControllerDrawableWidth,
                    mLBPoint.y - halfControllerDrawableHeight,
                    mLBPoint.x + halfControllerDrawableWidth,
                    mLBPoint.y + halfControllerDrawableHeight);
            controlDrawable.draw(canvas);

            // 右上角
            controlDrawable.setBounds(mRTPoint.x - halfControllerDrawableWidth,
                    mRTPoint.y - halfControllerDrawableHeight,
                    mRTPoint.x + halfControllerDrawableWidth,
                    mRTPoint.y + halfControllerDrawableHeight);
            controlDrawable.draw(canvas);

            // 右下角
            controlDrawable.setBounds(mRBPoint.x - halfControllerDrawableWidth,
                    mRBPoint.y - halfControllerDrawableHeight,
                    mRBPoint.x + halfControllerDrawableWidth,
                    mRBPoint.y + halfControllerDrawableHeight);
            controlDrawable.draw(canvas);
        }

        adjustLayout();
    }

    /**
     * 设置Matrix, 强制刷新
     */
    public void transformDraw(float degree){

        if(mBitmap == null) return;
        int bitmapWidth = (int)(mBitmap.getWidth() * mScale);
        int bitmapHeight = (int)(mBitmap.getHeight()* mScale);
        computeRect(-framePadding, -framePadding, bitmapWidth + framePadding, bitmapHeight + framePadding, degree);

        //设置缩放比例
        mMatrix.setScale(mScale, mScale);
        //绕着图片中心进行旋转
        mMatrix.postRotate(degree % 360, bitmapWidth/2, bitmapHeight/2);
        //设置画该图片的起始点
        mMatrix.postTranslate(offsetX + mControllerDrawableWidth/2, offsetY + mControllerDrawableHeight/2);

        adjustLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 主点
            case MotionEvent.ACTION_DOWN:
                // 第一个触点原始位置
                mFirstOriginalPoint.set(getTheFirstTouchPoint(event));
                mStatus = JudgeStatus(event); // 判断当前的控制状态
                break;

            // 副点
            case MotionEvent.ACTION_POINTER_DOWN:
                // 第二个触点原始位置
                mSecondOriginalPoint.set(getTheSecondTouchPoint(event));
                mStatus = JudgeStatus(event); // 判断当前的控制状态
                break;

            case MotionEvent.ACTION_UP:
                mStatus = STATUS_INIT;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mStatus == STATUS_TRANSFORM) {
                    curMovingControllerPoint.set((int) event.getX(), (int) event.getY());

                    float dx = curMovingControllerPoint.x - preMovingControllerPoint.x;
                    float dy = curMovingControllerPoint.y - preMovingControllerPoint.y;
                    int pointLocation = OpenCVHelper.LT_LOCATION;

                    if (curMovingControllerPoint == mLTPoint) {
                        pointLocation = OpenCVHelper.LT_LOCATION;

                    } else if (curMovingControllerPoint == mLBPoint) {
                        pointLocation = OpenCVHelper.LB_LOCATION;

                    } else if (curMovingControllerPoint == mRBPoint) {
                        pointLocation = OpenCVHelper.RB_LOCATION;

                    } else if (curMovingControllerPoint == mRTPoint) {
                        pointLocation = OpenCVHelper.RT_LOCATION;
                    }

                    LogUtils.i("MotionEvent.ACTION_MOVE . . mCurDegree . . . " + mCurDegree);
                    LogUtils.i("MotionEvent.ACTION_MOVE . . pointLocation . . . " + pointLocation);

                    PointMargin pointMargin = mPointsMargin.get(pointLocation);
                    pointMargin.setxMargin(pointMargin.getxMargin() + (int)dx);
                    pointMargin.setyMargin(pointMargin.getyMargin() + (int)dy);

                    if (pointMargin.getxMargin() >= this.getWidth() / 2) {
                        pointMargin.setxMargin(pointMargin.getxMargin() - (int)dx);
                    }

                    if (pointMargin.getyMargin() >= this.getHeight() / 2) {
                        pointMargin.setyMargin(pointMargin.getyMargin() - (int)dy);
                    }

                    invalidate();

                } else if (mStatus == STATUS_ROTATE_ZOOM) {
                    // 第二个触点的坐标
                    PointF touchPoint2 = getTheSecondTouchPoint(event);
                    if (touchPoint2 == null) {
                        return super.onTouchEvent(event);
                    }

                    float twoPointsOriginalDistance = distance4PointF(mSecondOriginalPoint, touchPoint2);
                    if (twoPointsOriginalDistance <= 10) {
                        return super.onTouchEvent(event);
                    }

                    // 缩放
                    int halfBitmapWidth = mBitmap.getWidth() / 2;
                    int halfBitmapHeight = mBitmap.getHeight() /2 ;

                    //图片某个点到图片中心的距离
                    float bitmapToCenterDistance = (float)Math.sqrt(halfBitmapWidth * halfBitmapWidth + halfBitmapHeight * halfBitmapHeight);
                    // 第一个触点和第二个触点移动后的距离
                    float twoPointsMovedDistance = getTwoPointsDistance(event);

//                    float s = Math.abs(twoPointsOriginalDistance - twoPointsMovedDistance);
//                    LogUtils.i("移动后的差距 。 。 " + s);

                    //计算缩放比例
//                    scale = s / bitmapToCenterDistance;
                    mScale = twoPointsMovedDistance / bitmapToCenterDistance;
//                    mScale = twoPointsMovedDistance / mTwoPointsOriginalDistance;


                    //缩放比例的界限判断
                    if (mScale <= MIN_SCALE) {
                        mScale = MIN_SCALE;
                    } else if (mScale >= MAX_SCALE) {
                        mScale = MAX_SCALE;
                    }

                    // 角度
                    double a = distance4PointF(mCenterPoint, mSecondOriginalPoint);
                    double b = distance4PointF(mSecondOriginalPoint, touchPoint2);
                    double c = distance4PointF(mCenterPoint, touchPoint2);

                    double cosb = (a * a + c * c - b * b) / (2 * a * c);

                    if (cosb >= 1) {
                        cosb = 1f;
                    }

                    double radian = Math.acos(cosb);
                    float newDegree = (float) GraphicsUtils.radianToDegree(radian);

                    //center -> proMove的向量， 我们使用PointF来实现
                    PointF centerToProMove = new PointF((mSecondOriginalPoint.x - mCenterPoint.x), (mSecondOriginalPoint.y - mCenterPoint.y));
                    //center -> curMove 的向量
                    PointF centerToCurMove = new PointF((touchPoint2.x - mCenterPoint.x), (touchPoint2.y - mCenterPoint.y));
                    //向量叉乘结果, 如果结果为负数， 表示为逆时针， 结果为正数表示顺时针
                    float result = centerToProMove.x * centerToCurMove.y - centerToProMove.y * centerToCurMove.x;

                    if (result < 0) {
                        newDegree = -newDegree;
                    }

                    mCurDegree += newDegree;
                    mSecondOriginalPoint.set(touchPoint2);

                    transformDraw(mCurDegree);

                } else if (mStatus == STATUS_DRAG) {
                    // 第一个触点的坐标
                    PointF firstTouchPoint = getTheFirstTouchPoint(event);
                    // 修改中心点
                    mCenterPoint.x += firstTouchPoint.x - mFirstOriginalPoint.x;
                    mCenterPoint.y += firstTouchPoint.y - mFirstOriginalPoint.y;
                    // 调整view大小和位置
                    adjustLayout();

                    // 将原始触点设置为当前触点
                    mFirstOriginalPoint.set(firstTouchPoint);
                }
                break;

            default:
                return super.onTouchEvent(event);
        }
        return true;
    }

    /**
     * 获取四个点和View的大小
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param degree
     */
    private void computeRect(int left, int top, int right, int bottom, float degree){

        Point lt = new Point(left, top);
        Point rt = new Point(right, top);
        Point rb = new Point(right, bottom);
        Point lb = new Point(left, bottom);
        Point cp = new Point((left + right) / 2, (top + bottom) / 2);

        mLTPoint = obtainRotationPoint(cp, lt, degree);
        mRTPoint = obtainRotationPoint(cp, rt, degree);
        mRBPoint = obtainRotationPoint(cp, rb, degree);
        mLBPoint = obtainRotationPoint(cp, lb, degree);

        mControllerPoints.clear();
        mControllerPoints.add(mLTPoint);
        mControllerPoints.add(mRTPoint);
        mControllerPoints.add(mRBPoint);
        mControllerPoints.add(mLBPoint);

        //计算X坐标最大的值和最小的值
        int maxCoordinateX = getMaxValue(mLTPoint.x, mRTPoint.x, mRBPoint.x, mLBPoint.x);
        int minCoordinateX = getMinValue(mLTPoint.x, mRTPoint.x, mRBPoint.x, mLBPoint.x);;

        mViewWidth = maxCoordinateX - minCoordinateX ;

        //计算Y坐标最大的值和最小的值
        int maxCoordinateY = getMaxValue(mLTPoint.y, mRTPoint.y, mRBPoint.y, mLBPoint.y);
        int minCoordinateY = getMinValue(mLTPoint.y, mRTPoint.y, mRBPoint.y, mLBPoint.y);

        mViewHeight = maxCoordinateY - minCoordinateY ;

        //View中心点的坐标
        Point viewCenterPoint = new Point((maxCoordinateX + minCoordinateX) / 2, (maxCoordinateY + minCoordinateY) / 2);

        offsetX = mViewWidth / 2 - viewCenterPoint.x;
        offsetY = mViewHeight / 2 - viewCenterPoint.y;

        int halfDrawableWidth = mControllerDrawableWidth / 2;
        int halfDrawableHeight = mControllerDrawableHeight /2;

        //将Bitmap的四个点的X的坐标移动offsetX + halfDrawableWidth
        mLTPoint.x += (offsetX + halfDrawableWidth);
        mRTPoint.x += (offsetX + halfDrawableWidth);
        mRBPoint.x += (offsetX + halfDrawableWidth);
        mLBPoint.x += (offsetX + halfDrawableWidth);

        //将Bitmap的四个点的Y坐标移动offsetY + halfDrawableHeight
        mLTPoint.y += (offsetY + halfDrawableHeight);
        mRTPoint.y += (offsetY + halfDrawableHeight);
        mRBPoint.y += (offsetY + halfDrawableHeight);
        mLBPoint.y += (offsetY + halfDrawableHeight);
    }

    /**
     * 获取变长参数最大的值
     * @param array
     * @return
     */
    public int getMaxValue(Integer...array){
        List<Integer> list = Arrays.asList(array);
        Collections.sort(list);
        return list.get(list.size() -1);
    }

    /**
     * 获取变长参数最大的值
     * @param array
     * @return
     */
    public int getMinValue(Integer...array){
        List<Integer> list = Arrays.asList(array);
        Collections.sort(list);
        return list.get(0);
    }

    /**
     * 获取旋转某个角度之后的点
     * @param center 控件中心点
     * @param source 需要进行计算的点
     * @param degree 旋转的角度
     * @return
     */
    public static Point obtainRotationPoint(Point center, Point source, float degree) {
        //两者之间的距离
        Point disPoint = new Point();
        disPoint.x = source.x - center.x;
        disPoint.y = source.y - center.y;

        //没旋转之前的弧度
        double originRadian = 0;

        //没旋转之前的角度
        double originDegree = 0;

        //旋转之后的角度
        double resultDegree = 0;

        //旋转之后的弧度
        double resultRadian = 0;

        //经过旋转之后点的坐标
        Point resultPoint = new Point();

        double distance = Math.sqrt(disPoint.x * disPoint.x + disPoint.y * disPoint.y);
        if (disPoint.x == 0 && disPoint.y == 0) {
            resultPoint.set(center.x, center.y);
            return resultPoint;
            // 第一象限
        } else if (disPoint.x >= 0 && disPoint.y >= 0) {
            // 计算与x正方向的夹角
            originRadian = Math.asin(disPoint.y / distance);

            // 第二象限
        } else if (disPoint.x < 0 && disPoint.y >= 0) {
            // 计算与x正方向的夹角
            originRadian = Math.asin(Math.abs(disPoint.x) / distance);
            originRadian = originRadian + Math.PI / 2;

            // 第三象限
        } else if (disPoint.x < 0) {
            // 计算与x正方向的夹角
            originRadian = Math.asin(Math.abs(disPoint.y) / distance);
            originRadian = originRadian + Math.PI;

        } else {
            // 计算与x正方向的夹角
            originRadian = Math.asin(disPoint.x / distance);
            originRadian = originRadian + Math.PI * 3 / 2;
        }

        // 弧度换算成角度
        originDegree = GraphicsUtils.radianToDegree(originRadian);
        resultDegree = originDegree + degree;

        // 角度转弧度
        resultRadian = GraphicsUtils.degreeToRadian(resultDegree);

        resultPoint.x = (int) Math.round(distance * Math.cos(resultRadian));
        resultPoint.y = (int) Math.round(distance * Math.sin(resultRadian));
        resultPoint.x += center.x;
        resultPoint.y += center.y;

        return resultPoint;
    }

    /**
     * 根据点击的位置判断是否点中控制旋转，缩放的图片， 初略的计算
     * @return
     */
    private int JudgeStatus(MotionEvent event){

        // 第一个触点的坐标
        PointF touchPoint = new PointF(event.getX(), event.getY());
        // 如果第一个触点点击了变形控制点
        if (isTransformable && isTouchControllerPoint(touchPoint)) {
            return STATUS_TRANSFORM; // 状态改为可变形，扭曲
        }

        // 第二个触点的坐标
        PointF touchPoint2 = getTheSecondTouchPoint(event);
        if (touchPoint2 != null) {
            // 如果两个连续触点距离大于10，则认为是缩放状态
            if (distance4PointF(getTheFirstTouchPoint(event), touchPoint2) >= 10) {
                return STATUS_ROTATE_ZOOM; // 状态改为可变形，扭曲
            }
        }

        return STATUS_DRAG;
    }

    /**
     * 设置旋转图
     * @param bitmap
     */
    public void setImageBitmap(Bitmap bitmap){
        this.mBitmap = bitmap;
        transformDraw(mCurDegree);
    }

    /**
     * 设置旋转图
     * @param drawable
     */
    public void setImageDrawable(Drawable drawable){
        this.mBitmap = BitmapUtils.drawable2Bitmap(drawable);
        transformDraw(mCurDegree);
    }

    /**
     * 根据id设置旋转图
     * @param resId
     */
    public void setImageResource(int resId){
        Drawable drawable = getContext().getResources().getDrawable(resId);
        setImageDrawable(drawable);
    }

    public float getImageDegree() {
        return this.mCurDegree;
    }

    /**
     * 设置图片旋转角度
     */
    public void setImageDegree(float degree) {
        if (this.mCurDegree != degree) {
            this.mCurDegree = degree;
            transformDraw(degree);
        }
    }

    public float getImageScale() {
        return mScale;
    }

    /**
     * 设置图片缩放比例
     * @param scale
     */
    public void setImageScale(float scale) {
        if(this.mScale != scale){
            this.mScale = scale;
            transformDraw(mCurDegree);
        };
    }

    public Drawable getControlDrawable() {
        return controlDrawable;
    }

    /**
     * 设置控制图标
     * @param drawable
     */
    public void setControlDrawable(Drawable drawable) {
        this.controlDrawable = drawable;
        mControllerDrawableWidth = drawable.getIntrinsicWidth();
        mControllerDrawableHeight = drawable.getIntrinsicHeight();
        transformDraw(mCurDegree);
    }

    public int getFramePadding() {
        return framePadding;
    }

    public void setFramePadding(int framePadding) {
        if(this.framePadding == framePadding)
            return;
        this.framePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, framePadding, metrics);
        transformDraw(mCurDegree);
    }

    public PointF getCenterPoint() {
        return mCenterPoint;
    }

    /**
     * 设置图片中心点位置，相对于父布局而言
     * @param mCenterPoint 控件中心点
     */
    public void setCenterPoint(PointF mCenterPoint) {
        this.mCenterPoint = mCenterPoint;
        adjustLayout();
    }

    public boolean isTransformable() {
        return this.isTransformable;
    }

    /**
     * 设置是否处于可缩放，平移，旋转状态
     * @param isTransformable 是否显示控制点
     */
    public void setTransformable(boolean isTransformable) {
        this.isTransformable = isTransformable;
        invalidate();
    }

    /**
     * 两个点之间的距离
     * @return
     */
    private float distance4PointF(PointF pf1, PointF pf2) {
        float disX = pf2.x - pf1.x;
        float disY = pf2.y - pf1.y;
        return (float)Math.sqrt(disX * disX + disY * disY);
    }

    /**
     * 是否点触到控制点
     * @param touchPoint 当前点击的位置点
     * @return
     * <p>
     *     true：点击到其中一个控制点
     * </p>
     * <p>
     *     false：没点到控制点
     * </p>
     */
    private boolean isTouchControllerPoint(final PointF touchPoint) {
        for (Point point : mControllerPoints) {
            if (point == null) {
                continue;
            }
            PointF controlPointF = new PointF(point);
            //点击的点到控制旋转，缩放点的距离
            float distanceToControl = distance4PointF(touchPoint, controlPointF);
            //如果两者之间的距离小于 控制图标的宽度，高度的最小值，则认为点中了控制图标
            if(distanceToControl < Math.min(mControllerDrawableWidth/2, mControllerDrawableHeight/2)){
                curMovingControllerPoint = point;
                preMovingControllerPoint.set(point.x, point.y);

                return true;
            }
        }
        return false;
    }

    /**
     * 获取前两个触点的距离
     * @param event 触碰事件
     * @return
     */
    private float getTwoPointsDistance(MotionEvent event) {
        // 第二个触点的坐标
        PointF touchPoint2 = getTheSecondTouchPoint(event);
        if (touchPoint2 == null) {
            return 0;
        }

        // 第一个触点的坐标
        PointF touchPoint = getTheFirstTouchPoint(event);

        return distance4PointF(touchPoint, touchPoint2);
    }

    /**
     * 获取第一个触点的坐标
     * @param event touch事件
     */
    private PointF getTheFirstTouchPoint(MotionEvent event) {
        float x2 = event.getX() + mViewPaddingLeft;
        float y2 = event.getY() + mViewPaddingTop;
        return new PointF(x2, y2);
    }

    /**
     * 获取第二个触点的坐标
     * @param event touch事件
     */
    private PointF getTheSecondTouchPoint(MotionEvent event) {
        if (event.getPointerCount() < 2) {
            return null;
        }

        // 触点的坐标
        float x2 = event.getX(1) + mViewPaddingLeft;
        float y2 = event.getY(1) + mViewPaddingTop;
        return new PointF(x2, y2);
    }

    /**
     * 重置图形
     */
    public void resetAll() {
        init();
        invalidate();
    }
}
