package com.leyou.game.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.widget.CustomWebView;
import com.tencent.smtt.sdk.WebSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 用户协议
 *
 * @author : rocky
 * @Create Time : 2017/8/9 上午11:38
 * @Modified Time : 2017/8/9 上午11:38
 */
public class AgreementDialog extends BaseDialog {
    private static final String TAG = "AgreementDialog";
    private final Context context;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web_view)
    CustomWebView webView;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    public AgreementDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_argeement;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

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
        webSetting.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND);
        webSetting.setUserAgentString(webSetting.getUserAgentString() + Constants.getPackageName());
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setSupportZoom(true);
        webView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                webView.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest, com.tencent.smtt.export.external.interfaces.WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
                super.onPageFinished(webView, s);
                LogUtil.d(TAG, s);
            }

            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                LogUtil.d(TAG, "---finish:" + s);
            }
        });
        webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            @Override
            public boolean onJsAlert(com.tencent.smtt.sdk.WebView webView, String s, String s1, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
                super.onProgressChanged(webView, i);
                int progress = webView.getProgress();
                LogUtil.d(TAG, "-----progress:" + progress);

            }
        });
        webView.loadUrl(Constants.MINE_AGREEMENT);

    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                ((Activity) context).finish();
                dismiss();
                break;
            case R.id.btn_confirm:
                dismiss();
                break;
        }
    }
}
