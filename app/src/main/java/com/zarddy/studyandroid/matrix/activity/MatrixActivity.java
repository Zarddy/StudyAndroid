package com.zarddy.studyandroid.matrix.activity;

import android.view.View;

import com.zarddy.library.base.BaseActivity;
import com.zarddy.library.util.LogUtils;
import com.zarddy.studyandroid.R;
import com.zarddy.studyandroid.matrix.view.MatrixImageView;
import com.zarddy.studyandroid.matrix.view.SingleTouchView;

import butterknife.BindView;

/**
 * TODO 显示可变形ImageView的界面
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
        }
    }

    /**
     * TODO 开始变形
     */
    private void startChange() {
        LogUtils.i("开始变形 . . . . ");

        imageView.setEditable(true);
    }

    /**
     * TODO 取消变形
     */
    private void endChange() {
        LogUtils.i("取消变形 . . . . ");

        imageView.setEditable(false);
    }
}
