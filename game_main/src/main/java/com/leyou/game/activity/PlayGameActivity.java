package com.leyou.game.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseWebViewActivity;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.PayEvent;
import com.leyou.game.event.RefreshWinAwardEvent;
import com.leyou.game.ipresenter.win.IPlayGameActivity;
import com.leyou.game.presenter.win.PlayGameActivityPresenter;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.BuyDiamondDialog;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.view.NumberProgressBar;
import com.tamic.jswebview.view.ProgressBarWebView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 游戏开始页面
 *
 * @author : rocky
 * @Create Time : 2017/4/26 下午12:17
 * @Modified By: rocky
 * @Modified Time : 2017/4/26 下午12:17
 */

public class PlayGameActivity extends BaseWebViewActivity implements IPlayGameActivity {

    private static final String TAG = "PlayGameActivity";
    @BindView(R.id.progressbar_webView)
    ProgressBarWebView progressbarWebView;
    @BindView(R.id.re_loading)
    RelativeLayout reLoading;
    private long exitTime;
    private JsInterfaceFun jsInterfaceFun;
    private Handler handler = new Handler();
    private LoadingProgressDialog loadingDialog;
    private PlayGameActivityPresenter presenter;
    private GameBean gameBean;
    private String gameCountId;
    private long currentPayMoney;
    private boolean isFinished;
    private boolean isChongzhi;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("plat", "platform" + platform);
            Toast.makeText(PlayGameActivity.this, " 分享成功 ", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PlayGameActivity.this, " 分享失败 ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PlayGameActivity.this, " 分享取消 ", Toast.LENGTH_SHORT).show();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payEvent(PayEvent event) {
        LogUtil.d(TAG, "-----pay Event:" + event.isFlag());
        if (isFinished) {
            isFinished = false;
            if (event.isFlag()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.getUserWealth();
                    }
                }, 100);
                LogUtil.d(TAG, "----- restart pay");
            } else {
                showMessageToast(getString(R.string.chongzhi_failed));
                dismissedLoading();
            }
        } else {
            LogUtil.d(TAG, "----- restart failed");
            dismissedLoading();
        }
    }

    @Override
    public void initWindows() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    @Override
    public int getLayout() {
        return R.layout.activity_play_game;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        gameBean = getIntent().getParcelableExtra("game");
        setScreenOrientation(gameBean.directivity);
        NumberProgressBar progressBar = progressbarWebView.getProgressBar();
        progressBar.setVisibility(View.GONE);

        WebSettings webSetting = progressbarWebView.getWebView().getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        jsInterfaceFun = new JsInterfaceFun(this);
        progressbarWebView.getWebView().addJavascriptInterface(jsInterfaceFun, "gamePage");
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
                isFinished = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isFinished = true;
            }
        });
    }

    private void setScreenOrientation(int direct) {
        if (0 == direct) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void initPresenter() {
        presenter = new PlayGameActivityPresenter(this, this);
        presenter.getGameTime(gameBean.mark);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                ToastUtils.showToastShort("再按一次退出游戏");
                return false;
            } else {
                EventBus.getDefault().post(new RefreshWinAwardEvent(RefreshWinAwardEvent.REFRESH));
                finishCurrentActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressbarWebView.getWebView().onResume();
        }
        if (isFinished) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissedLoading();
                    progressbarWebView.loadUrl("javascript:getUserVisibleState(" + true + ")");
                }
            }, 10);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressbarWebView.getWebView().onPause();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissedLoading();
                progressbarWebView.loadUrl("javascript:getUserVisibleState(" + false + ")");
            }
        }, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressbarWebView.getWebView().destroy();
        EventBus.getDefault().unregister(this);
        presenter.destroy();
    }

    @Override
    public void loadUrl(String gameCountId) {
        this.gameCountId = gameCountId;
        progressbarWebView.loadUrl(gameBean.url);
    }

    @Override
    public void showGameView() {
        reLoading.setVisibility(View.GONE);
    }

    @Override
    public void sendOrderState(final boolean flag) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbarWebView.loadUrl("javascript:getOrderState(" + flag + ")");
            }
        }, 200);
        if (flag) {
            UserData.getInstance().setDiamonds(UserData.getInstance().getDiamonds() - (int) currentPayMoney);
        } else {
            showMessageToast(getString(R.string.diamonds_number_no_more));
        }
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        } else {
            LogUtil.d(TAG, "----loading is not showing or null");
        }
    }

    @Override
    public void showChongZhiDialog() {
        isFinished = true;
        dismissedLoading();
        BuyDiamondDialog buyDiamondDialog = new BuyDiamondDialog(this, 1);
        buyDiamondDialog.show();
    }

    @Override
    public void reStartPay() {
        if (UserData.getInstance().getDiamonds() >= currentPayMoney) {
            changeLoadingDes(this.getString(R.string.order_pay) + currentPayMoney + "个钻石...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.payVirtualCoin(gameCountId);
                    dismissedLoading();
                }
            }, 500);

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissedLoading();
                    progressbarWebView.loadUrl("javascript:getOrderState(" + false + ")");
                }
            }, 200);
            dismissedLoading();
            showMessageToast(getString(R.string.diamonds_number_no_more));
            showChongZhiDialog();
        }
    }

    private void startShare(final String url) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //友盟微信朋友圈分享
                UMImage umImage = new UMImage(PlayGameActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_launcher));
                UMWeb umWeb = new UMWeb(url);
                umWeb.setThumb(umImage);
                umWeb.setDescription("游戏着，快乐着");
                umWeb.setTitle(getString(R.string.app_name));
                new ShareAction(PlayGameActivity.this)
                        .withText(getString(R.string.app_name))
                        .withMedia(umWeb)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener)
                        .open();
            }
        });
    }

    @Override
    public void showLoading() {
        loadingDialog = new LoadingProgressDialog(this, false);
        loadingDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != loadingDialog) {
            loadingDialog.setLoadingText(des);
        } else {
            LogUtil.d(TAG, "----change loading");
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        } else {
            LogUtil.d(TAG, "------dismiss null");
        }
    }

    @Override
    public void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
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
            JSONObject object = new JSONObject();
            try {
                object.put("userId", UserData.getInstance().getId());
                object.put("gameCountId", gameCountId);
                object.put("nickName", UserData.getInstance().getNickname());
                object.put("headImg", UserData.getInstance().getPictureUrl());
                object.put("currentScore", gameBean.score);
                return object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object.toString();
        }

        @JavascriptInterface
        public void payOrder(long money, final String gameCountId2) {
            showLoading();
            currentPayMoney = money;
            gameCountId = gameCountId2;
            LogUtil.d(TAG, "diamonds:" + UserData.getInstance().getDiamonds() + "----money:" + money);
            if (UserData.getInstance().getDiamonds() >= money) {
                changeLoadingDes(context.getString(R.string.order_pay) + money + "个钻石...");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.payVirtualCoin(gameCountId2);
                    }
                }, 500);

            } else {
                dismissedLoading();
                if (UserData.getInstance().isLogIn()) {
                    showMessageToast(context.getString(R.string.diamonds_number_no_more));
                    showChongZhiDialog();
                } else {
                    showMessageToast("登录后复活～");
                }
            }
        }

        @JavascriptInterface
        public void share(String url) {
            startShare(url);
        }
    }
}
