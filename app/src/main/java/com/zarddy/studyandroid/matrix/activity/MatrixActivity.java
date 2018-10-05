package com.zarddy.studyandroid.matrix.activity;

import android.view.View;

import com.zarddy.library.base.BaseActivity;
import com.zarddy.library.util.LogUtils;
import com.zarddy.studyandroid.R;
import com.zarddy.studyandroid.matrix.view.MatrixImageView;

import org.opencv.android.OpenCVLoader;

import butterknife.BindView;

/**
 * TODO 显示可变形ImageView的界面
 * <p>
 *     <h3>参考文章</h3>
 *     <dl>
 *         <dt>Android 图片随着手势缩放，平移，并且支持多点触控</dt>
 *         <dt>https://blog.csdn.net/lijinweii/article/details/77717540</dt>
 *     </dl>
 * </p>
 */
public class MatrixActivity extends BaseActivity {

    @BindView(R.id.image)
    MatrixImageView imageView;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_matrix;
    }

    @Override
    protected void initView() {
        OpenCVLoader.initDebug();
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void setEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startChange: // 开始变形
                startChange();
                break;

            case R.id.endChange: // 取消变形
                endChange();
                break;

            case R.id.resetAll: // 还原
                resetAll();
                break;
        }
    }

    /**
     * TODO 开始变形
     */
    private void startChange() {
        LogUtils.i("开始变形 . . . . ");

        imageView.setTransformable(true);
    }

    /**
     * TODO 取消变形
     */
    private void endChange() {
        LogUtils.i("取消变形 . . . . ");

        imageView.setTransformable(false);
    }

    /**
     * TODO 重置
     */
    private void resetAll() {
        imageView.resetAll();
    }
}
