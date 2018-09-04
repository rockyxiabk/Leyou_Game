package com.leyou.game.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.widget.CustomWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 文字性介绍 弹窗提示加载html
 *
 * @author : rocky
 * @Create Time : 2017/6/26 上午10:53
 * @Modified Time : 2017/6/26 上午10:53
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewDialog extends BaseDialog {

    @BindView(R.id.tv_text_webView_title)
    TextView tvTextWebViewTitle;
    @BindView(R.id.iv_webView_close)
    ImageView ivWebViewClose;
    @BindView(R.id.webView)
    CustomWebView progressbarWebView;
    private Context context;
    private String title;
    private String url;

    public WebViewDialog(Context context, String title, String url) {
        super(context);
        this.context = context;
        this.title = title;
        this.url = url;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_webview;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        tvTextWebViewTitle.setText(title);

        progressbarWebView.setMaxHeight(ScreenUtil.getInstance(context).getScreenHeight() * 46 / 100);
        WebSettings webSetting = progressbarWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setSupportZoom(false);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(context.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        progressbarWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }
        });
        progressbarWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressbarWebView.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressbarWebView.loadUrl("file:///android_asset/error.html");
            }
        });

        progressbarWebView.loadUrl(url);

    }

    @OnClick(R.id.iv_webView_close)
    public void onViewClicked() {
        dismiss();
    }
}
