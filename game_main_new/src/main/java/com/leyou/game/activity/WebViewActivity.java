package com.leyou.game.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseWebViewActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshWinAwardEvent;
import com.leyou.game.util.LogUtil;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

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

    private static final String TAG = "WebViewActivity";
    @BindView(R.id.re_toolbar)
    RelativeLayout reToolbar;
    @BindView(R.id.iv_webView_back)
    ImageView ivWebViewBack;
    @BindView(R.id.tv_webView_title)
    TextView tvWebViewTitle;
    @BindView(R.id.progressbar_webView)
    WebView webView;
    private String title;
    private String url;
    private int type;
    private URL intentUrl;
    private JsInterfaceFun jsInterfaceFun;

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
        if (null == url) {
            try {
                intentUrl = new URL(intent.getData().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setBuiltInZoomControls(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setDomStorageEnabled(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setGeolocationEnabled(true);
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setUserAgentString(webSetting.getUserAgentString() + Constants.getPackageName());
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setSupportZoom(true);
        jsInterfaceFun = new JsInterfaceFun(this);
        webView.addJavascriptInterface(jsInterfaceFun, "gamePage");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                webView.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                LogUtil.d(TAG, s);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                LogUtil.d(TAG, "---finish:" + s);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                int progress = webView.getProgress();
                LogUtil.d(TAG, "-----progress:" + progress);

            }
        });

        tvWebViewTitle.setText(title);
        if (null == url) {
            if (null != intentUrl) {
                webView.loadUrl(intentUrl.toString());
            }
        } else {
            webView.loadUrl(url);
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
        if (webView.canGoBack()) {
            webView.goBack();
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

    class JsInterfaceFun {
        private Context context;

        public JsInterfaceFun(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void finishGameActivity() {
            EventBus.getDefault().post(new RefreshWinAwardEvent(RefreshWinAwardEvent.REFRESH));
            finishCurrentActivity();
        }

        @JavascriptInterface
        public String getUserInfo() {
            if (UserData.getInstance().isLogIn()) {
                JSONObject object = new JSONObject();
                try {
                    object.put("userId", UserData.getInstance().getId());
                    object.put("nickName", UserData.getInstance().getNickname());
                    object.put("headImg", UserData.getInstance().getPictureUrl());
                    return object.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return object.toString();
            } else {
                return "";
            }
        }
    }

}
