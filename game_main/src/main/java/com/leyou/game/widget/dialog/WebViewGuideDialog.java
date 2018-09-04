package com.leyou.game.widget.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.UserData;
import com.leyou.game.widget.CustomWebView;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.view.NumberProgressBar;
import com.tamic.jswebview.view.ProgressBarWebView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/8/14 下午5:46
 * @Modified Time : 2017/8/14 下午5:46
 */
public class WebViewGuideDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.webView)
    ProgressBarWebView progressbarWebView;
    private JsInterfaceFun jsInterfaceFun;

    public WebViewGuideDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_web_view_guide;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        NumberProgressBar progressBar = progressbarWebView.getProgressBar();
        progressBar.setVisibility(View.GONE);

        WebSettings webSetting = progressbarWebView.getWebView().getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setSupportZoom(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        jsInterfaceFun = new JsInterfaceFun(context);
        progressbarWebView.getWebView().addJavascriptInterface(jsInterfaceFun, "gamePage");
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

        progressbarWebView.loadUrl("http://192.168.0.80:8020/intro/guide.html");
    }

    class JsInterfaceFun {
        private Context context;

        public JsInterfaceFun(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void finishActivity() {
            dismiss();
        }
    }
}
