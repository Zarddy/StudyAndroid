package com.zarddy.studyandroid.matrix.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zarddy.studyandroid.R;

/**
 * TODO 参考文章
 * https://blog.csdn.net/songyulong8888/article/details/75222562
 */
public class MeshView extends View {


    //定义两个常量，指定该图片横向、纵向被划分为20格
    private final int WIDTH = 2;
    private final int HEIGHT = 2;
    //计录图片上包含441个顶点
    private final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    //定义一个数组，保存Bitmap上21 * 21个点坐标
    private final float[] verts = new float[COUNT * 2];
    //定义一个数组，记录Bitmap上的20 * 20个点经过扭曲后的坐标
    private final float[] orig = new float[COUNT * 2];
    private Bitmap bitmap;
    private long baseMesh = 10000 * 100;// 扭曲基数

    public MeshView(Context context) {
        super(context);
        init(context);
    }

    public MeshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context)
    {
        setFocusable(true);
        //根据指定资源加载图片
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.light2_thumb);
        //获取图片宽度，高宽度
        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++){
            float fy  = bitmapHeight * y / HEIGHT;

            for (int x = 0; x <= WIDTH; x++) {
                float fx = bitmapWidth * x /WIDTH;
                //初始化orig, verts两个数组。初始化后orig, verts 两个数组均匀地保存了20 * 21个点的x,y坐标
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index += 1;
            }
        }
        //设置背景色
        setBackgroundColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //对Bitmap按verts数组进行扭曲
        //从从第一个点（由第5个参数0控制）开始扭曲
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
    }

    //根据触摸事件的位置计算verts数组里的各元素值
    private void warp(float cx, float cy)
    {
        for (int i=0; i < COUNT * 2; i += 2)
        {
            float dx = cx - orig[i + 0];
            float dy = cy - orig[i + 1];
            float dd = dx * dx + dy * dy;
            //计算每个坐标点与当前点（cx, cy）之间的距离
            float d = (float)Math.sqrt(dd);
            //计算扭曲度，距离当前点(cx, cy)越远，越扭曲
            float pull = baseMesh / (float)(dd * d);
            //对verts 数组，保存bitmap上21 *21 个点经过扭曲后的坐标，重新赋值
            if(pull >= 1)
            {
                verts[i + 0] = cx;
                verts[i + 1] = cy;
            }else
            {
                verts[i + 0] = orig[i + 0] + dx * pull;
                verts[i + 1] = orig[i + 1] + dy * pull;
            }

        }
        //通知组件重绘
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        warp(event.getX(), event.getY());
        return true;
    }
}
