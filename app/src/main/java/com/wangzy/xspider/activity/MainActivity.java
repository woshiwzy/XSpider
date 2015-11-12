package com.wangzy.xspider.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;

import com.common.util.ListUtiles;
import com.common.util.LogUtil;
import com.common.util.StringUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wangzy.xspider.App;
import com.wangzy.xspider.BaseSlideActivity;
import com.wangzy.xspider.DownLoadTask;
import com.wangzy.xspider.R;
import com.wangzy.xspider.db.DbConstant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseSlideActivity {


    @InjectView(R.id.editText)
    EditText editText;

    @InjectView(R.id.buttonGo)
    ImageButton buttonGo;

    @InjectView(R.id.webView)
    WebView webView;

    @InjectView(R.id.imageButtonPre)
    ImageButton imageButtonPre;

    @InjectView(R.id.imageButtonNext)
    ImageButton imageButtonNext;

    private String stringUrlHome;

    private ArrayList<DownLoadTask> downLoadTasks;

    private HashSet<String> setSuffix = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        WebSettings setting = webView.getSettings();
        downLoadTasks = new ArrayList<DownLoadTask>();

        setting.setJavaScriptEnabled(true);//支持js
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtil.i(App.tag, "load:" + title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
                showProgressDialog(false);
                editText.setText(url);
                downLoadTasks.clear();
                if (setSuffix.isEmpty()) {
                    setUpReSourceType();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressDialog();

                LogUtil.e(App.tag, "task size:" + downLoadTasks.size());
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                LogUtil.i(App.tag, url);
                addUrl2DownloadTask(url);

            }
        });

        setUpReSourceType();
    }


    private void setUpReSourceType() {

        findResourceType(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (null == e && !ListUtiles.isEmpty(list)) {
                    setSuffix.clear();
                    for (ParseObject parseObject : list) {
                        setSuffix.add(parseObject.getString(DbConstant.RESOURCE_TYPE_COL));
                    }
                }
            }
        });
    }


    public void addUrl2DownloadTask(String url) {
        String suffix = getSuffix(url);
        if (!StringUtils.isEmpty(suffix)) {
            if (setSuffix.contains(suffix) || setSuffix.contains(suffix.toUpperCase()) || setSuffix.contains(suffix.toLowerCase())) {

                downLoadTasks.add(new DownLoadTask(url));

                LogUtil.e(App.tag, downLoadTasks.size() + "  tasks :" + url);
            }
        }
    }

    public String getSuffix(String url) {

        try {
            String suffix = url.substring(url.lastIndexOf(".") + 1);
            return suffix;
        } catch (Exception e) {
        }
        return "";
    }

    private void findResourceType(FindCallback<ParseObject> findCallback) {
        ParseQuery<ParseObject> types = ParseQuery.getQuery(DbConstant.RESOURCE_TYPE);
        types.fromLocalDatastore();
        types.findInBackground(findCallback);
    }

    @OnClick(R.id.imageButtonPre)
    public void onPreClick() {
        if (webView.canGoBack()) {
            webView.goBack();
        }

    }

    @OnClick(R.id.imageButtonRefresh)
    public void onRefreshClick() {
        if (!StringUtils.isEmpty((webView.getUrl()))) {
            webView.loadUrl(webView.getUrl());
        }
    }

    @OnClick(R.id.imageButtonNext)
    public void onNextClick() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }


    @OnClick(R.id.imageButtonHome)
    public void onHomeClick() {

        if (!StringUtils.isEmpty(stringUrlHome)) {
            webView.loadUrl(stringUrlHome);
        }
    }

    @OnClick(R.id.imageButtonMenu)
    public void onClickImageMenu() {
        getSlidingMenu().toggle();
    }

    @OnClick(R.id.imageButtonDown)
    public void onDownloadClick() {

    }

    @OnClick(R.id.buttonGo)
    public void onClickGo() {
        String url = getInput(editText);
        if (!StringUtils.isEmpty(url)) {
            this.stringUrlHome = url;
            if (url.startsWith("http")) {
                webView.loadUrl(editText.getText().toString());
            } else {
                webView.loadUrl("http://" + editText.getText().toString());
            }

        }
    }


}
