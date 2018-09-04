package com.leyou.game.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.UserData;
import com.leyou.game.util.SPUtil;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.view.NumberProgressBar;
import com.tamic.jswebview.view.ProgressBarWebView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.activity
 *
 * @author : rocky
 * @Create Time : 2017/8/14 下午6:05
 * @Modified Time : 2017/8/14 下午6:05
 */
public class WebViewGuideActivity extends Activity {
    @BindView(R.id.webView)
    ProgressBarWebView progressbarWebView;
    private JsInterfaceFun jsInterfaceFun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_web_view_guide);
        initWeight(savedInstanceState);
    }

    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        NumberProgressBar progressBar = progressbarWebView.getProgressBar();
        progressBar.setVisibility(View.GONE);

        WebSettings webSetting = progressbarWebView.getWebView().getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setSupportZoom(false);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        jsInterfaceFun = new JsInterfaceFun(this);
        progressbarWebView.getWebView().addJavascriptInterface(jsInterfaceFun, "gamePage");
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
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
                return map;
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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        progressbarWebView.loadUrl(Constants.MAIN_GUIDE_URL);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    class JsInterfaceFun {
        private Context context;

        public JsInterfaceFun(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void finishGameActivity() {
            SPUtil.setBoolean(WebViewGuideActivity.this, SPUtil.INDUCE, "isGuide", true);
            finish();
        }
    }
}
