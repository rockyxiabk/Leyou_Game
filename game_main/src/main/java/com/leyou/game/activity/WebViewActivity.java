package com.leyou.game.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseWebViewActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.util.ToastUtils;
import com.tamic.jswebview.browse.JsWeb.CustomWebChromeClient;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.view.NumberProgressBar;
import com.tamic.jswebview.view.ProgressBarWebView;
import com.umeng.analytics.MobclickAgent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 应用内置浏览器
 *
 * @author : rocky
 * @Create Time : 2017/4/27 上午11:31
 * @Modified By: rocky
 * @Modified Time : 2017/4/27 上午11:31
 */
public class WebViewActivity extends BaseWebViewActivity {

    @BindView(R.id.iv_webView_back)
    ImageView ivWebViewBack;
    @BindView(R.id.tv_webView_title)
    TextView tvWebViewTitle;
    @BindView(R.id.progressbar_webView)
    ProgressBarWebView progressbarWebView;
    private String title;
    private String url;
    private int type;
    private URL intentUrl;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        type = intent.getIntExtra("type", 0);
        url = intent.getStringExtra("url");
        if (null != intent) {
            url = intent.getStringExtra("url");
            if (null == url) {
                try {
                    intentUrl = new URL(intent.getData().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        NumberProgressBar progressBar = progressbarWebView.getProgressBar();
        progressBar.setVisibility(View.GONE);

        WebSettings webSetting = progressbarWebView.getWebView().getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        progressbarWebView.setWebViewClient(new CustomWebViewClient(progressbarWebView.getWebView()) {


            @Override
            public String onPageError(String url) {
                //指定网络加载失败时的错误页面
                return "file:///android_asset/error.html";
            }

            @Override
            public Map<String, String> onPageHeaders(String url) {
                // 可以加入header
                Map<String, String> map = new HashMap<>();
                map.put("userId", UserData.getInstance().getId());
                map.put("pkg", Constants.getPackageName());
                if (type == 1) {
                    return map;
                } else {
                    return null;
                }
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            }
        });
        progressbarWebView.getWebView().setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvWebViewTitle.setText(title);
            }
        });

        tvWebViewTitle.setText(title);
        if (null == url) {
            if (null != intentUrl) {
                progressbarWebView.loadUrl(intentUrl.toString());
            }
        } else {
            progressbarWebView.loadUrl(url);
        }
    }

    @Override
    public void initPresenter() {

    }

    @OnClick(R.id.iv_webView_back)
    public void onViewClicked() {
        if (type == 1) {
            startActivity(new Intent(this, MainActivity.class));
            finishCurrentActivity();
        } else {
            finishCurrentActivity();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (progressbarWebView.getWebView().canGoBack()) {
            progressbarWebView.getWebView().goBack();
        } else {
            if (type == 1) {
                startActivity(new Intent(this, MainActivity.class));
                finishCurrentActivity();
            } else {
                finishCurrentActivity();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }
}
