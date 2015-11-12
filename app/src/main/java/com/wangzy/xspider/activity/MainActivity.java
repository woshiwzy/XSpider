package com.wangzy.xspider.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;

import com.wangzy.xspider.BaseSlideActivity;
import com.wangzy.xspider.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressDialog(false);
                editText.setText(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressDialog();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.i("wzy", url);
            }
        });
    }


    @OnClick(R.id.imageButtonPre)
    public void onPreClick() {
        if (webView.canGoBack()) {
            webView.goBack();
        }

    }

    @OnClick(R.id.imageButtonNext)
    public void onNextClick() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    @OnClick(R.id.imageButtonMenu)
    public void onClickImageMenu() {
        getSlidingMenu().toggle();
    }

    @OnClick(R.id.buttonGo)
    public void onClickGo() {
        webView.loadUrl(editText.getText().toString());
    }


}
