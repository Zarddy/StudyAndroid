package com.zarddy.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewId());

        ButterKnife.bind(this);

        initView();
        setData();
        setEvent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setData();
    }

    protected abstract int setContentViewId();

    protected abstract void initView();

    protected abstract void setData();

    protected abstract void setEvent();

    public abstract void onClick(View view);
}
