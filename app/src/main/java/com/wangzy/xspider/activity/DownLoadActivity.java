package com.wangzy.xspider.activity;

import android.os.Bundle;

import com.common.BaseActivity;
import com.wangzy.xspider.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownLoadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.imageButtonBack)
    public void onImageBackClick() {
        finish();
    }
}
