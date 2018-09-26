package com.zarddy.studyandroid.matrix.activity;

import android.graphics.Matrix;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zarddy.library.base.BaseActivity;
import com.zarddy.library.util.LogUtils;
import com.zarddy.studyandroid.R;

import butterknife.BindView;

public class TestGridMatrixActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.input_01)
    EditText input_01;
    @BindView(R.id.input_02)
    EditText input_02;
    @BindView(R.id.input_03)
    EditText input_03;
    @BindView(R.id.input_04)
    EditText input_04;
    @BindView(R.id.input_05)
    EditText input_05;
    @BindView(R.id.input_06)
    EditText input_06;
    @BindView(R.id.input_07)
    EditText input_07;
    @BindView(R.id.input_08)
    EditText input_08;
    @BindView(R.id.input_09)
    EditText input_09;

    private Matrix matrix;

    private float val01, val02, val03,
            val04, val05, val06,
            val07, val08, val09;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_test_grid_matrix;
    }

    @Override
    protected void initView() {
        input_01.addTextChangedListener(this);
        input_02.addTextChangedListener(this);
        input_03.addTextChangedListener(this);
        input_04.addTextChangedListener(this);
        input_05.addTextChangedListener(this);
        input_06.addTextChangedListener(this);
        input_07.addTextChangedListener(this);
        input_08.addTextChangedListener(this);
        input_09.addTextChangedListener(this);
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
            case R.id.submit: // 执行

                matrix = new Matrix();

                float[] values = {val01, val02, val03,
                        val04, val05, val06,
                        val07, val08, val09};

                matrix.setValues(values);

                imageView.setImageMatrix(matrix);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        try {
            val01 = Float.parseFloat(input_01.getText().toString());
        } catch (Exception e) {
            input_01.setText("1");
            val01 = 1;
        }

        try {
            val02 = Float.parseFloat(input_02.getText().toString());
        } catch (Exception e) {
            input_02.setText("1");
            val02 = 1;
        }

        try {
            val03 = Float.parseFloat(input_03.getText().toString());
        } catch (Exception e) {
            input_03.setText("1");
            val03 = 1;
        }

        try {
            val04 = Float.parseFloat(input_04.getText().toString());
        } catch (Exception e) {
            input_04.setText("1");
            val04 = 1;
        }

        try {
            val05 = Float.parseFloat(input_05.getText().toString());
        } catch (Exception e) {
            input_05.setText("1");
            val05 = 1;
        }

        try {
            val06 = Float.parseFloat(input_06.getText().toString());
        } catch (Exception e) {
            input_06.setText("1");
            val06 = 1;
        }

        try {
            val07 = Float.parseFloat(input_07.getText().toString());
        } catch (Exception e) {
            input_07.setText("1");
            val07 = 1;
        }

        try {
            val08 = Float.parseFloat(input_08.getText().toString());
        } catch (Exception e) {
            input_08.setText("1");
            val08 = 1;
        }

        try {
            val09 = Float.parseFloat(input_09.getText().toString());
        } catch (Exception e) {
            input_09.setText("1");
            val09 = 1;
        }
    }
}
