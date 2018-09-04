package com.leyou.game.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.widget.CustomWebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

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

    private static final String TAG = "WebViewDialog";
    @BindView(R.id.tv_text_webView_title)
    TextView tvTextWebViewTitle;
    @BindView(R.id.iv_webView_close)
    ImageView ivWebViewClose;
    @BindView(R.id.webView)
    CustomWebView webView;
    private Context context;
    private String title;
    private String url;
    private boolean isHalf = false;

    public WebViewDialog(Context context, String title, String url) {
        super(context);
        this.context = context;
        this.title = title;
        this.url = url;
    }

    public WebViewDialog(Context context, String title, String url, boolean isHalf) {
        super(context);
        this.context = context;
        this.title = title;
        this.url = url;
        this.isHalf = isHalf;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_webview;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        tvTextWebViewTitle.setText(title);
        ViewGroup.LayoutParams params = webView.getLayoutParams();
        if (isHalf) {
            params.height = ScreenUtil.getInstance(context).getScreenHeight() * 46 / 100;
            webView.setLayoutParams(params);
        }

//        webView.setMaxHeight(ScreenUtil.getInstance(context).getScreenHeight() * 46 / 100);
        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(context.getDir("appcache", 0).getPath());
        webSetting.setBuiltInZoomControls(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setDomStorageEnabled(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setGeolocationEnabled(true);
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setUserAgentString(webSetting.getUserAgentString() + Constants.getPackageName());
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setSupportZoom(true);
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

        webView.loadUrl(url);

    }

    @OnClick(R.id.iv_webView_close)
    public void onViewClicked() {
        dismiss();
    }
}
