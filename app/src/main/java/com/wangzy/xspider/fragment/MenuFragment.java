package com.wangzy.xspider.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.common.util.Tool;
import com.wangzy.xspider.R;
import com.wangzy.xspider.activity.DownLoadActivity;
import com.wangzy.xspider.activity.SettingActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuFragment extends Fragment implements OnClickListener {


    private View rootView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = View.inflate(getActivity(), R.layout.menu_slide_left, null);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @OnClick(R.id.viewSetting)
    public void onSettingClick() {
        Tool.startActivity(getActivity(), SettingActivity.class);
    }

    @OnClick(R.id.viewDownLoad)
    public void onDownload(){
        Tool.startActivity(getActivity(), DownLoadActivity.class);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            default:
                break;
        }
    }


    public static interface OnSlideMenuClickListener {

        public void onLogOutClick();
    }
}
