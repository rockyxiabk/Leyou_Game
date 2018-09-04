package com.leyou.game.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.http.SslCertificate;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.mine.UptoActivity;
import com.leyou.game.base.BaseWebViewActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.event.PayEvent;
import com.leyou.game.event.PayResultCode;
import com.leyou.game.event.RefreshWinAwardEvent;
import com.leyou.game.ipresenter.win.IPlayGameActivity;
import com.leyou.game.presenter.win.PlayGameActivityPresenter;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.SystemApi;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.LogInDialog;
import com.leyou.game.widget.dialog.game.GameManagerDialog;
import com.leyou.game.widget.dialog.game.ShareGameToContactDialog;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 游戏开始页面-赢大奖-赢钻石
 *
 * @author : rocky
 * @Create Time : 2017/4/26 下午12:17
 * @Modified By: rocky
 * @Modified Time : 2017/4/26 下午12:17
 */

public class PlayGameActivity extends BaseWebViewActivity implements View.OnClickListener, IPlayGameActivity {

    private static final String TAG = "PlayGameActivity";
    @BindView(R.id.web_view)
    ViewGroup contentView;
    @BindView(R.id.re_loading)
    RelativeLayout reLoading;
    @BindView(R.id.root_ReLay)
    RelativeLayout rootReLay;//显示蒙层效果
    @BindView(R.id.re_floatView)
    RelativeLayout reFloatView;
    @BindView(R.id.iv_header)
    SimpleDraweeView ivHeader;

    //share view
    private View inflate;
    private RelativeLayout rootPopup;
    private View llBack;
    private View llItem1;
    private View llItem2;
    private View llItem3;
    private View llItem4;
    private View tvCancel;
    private PopupWindow popupWindow;

    private long exitTime;
    private JsInterfaceFun jsInterfaceFun;
    private Handler handler = new Handler();
    private LoadingProgressDialog loadingDialog;
    private PlayGameActivityPresenter presenter;
    private GameBean gameBean = new GameBean();
    private String gameCountId = "";
    private List<GameBean> recommendList = new ArrayList<>();
    private String gameUrl = "";
    private WebView webView;

    //    private AudioManager mAudioManager;
    private boolean isPause = false;
    private boolean isShare = false;//分析页面是否展示
    private boolean isReload = false;//是否需要重新加载
    private LogInDialog logInDialog;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResultEventBus(PayResultCode event) {
        int resultCode = event.getResultCode();
        if (resultCode == 1) {//支付成功
            if (isReload)
                webView.loadUrl(gameUrl);
        } else if (resultCode == 2) {//支付失败
            if (isReload)
                webView.loadUrl(gameUrl);
        } else {
            if (isReload)
                webView.loadUrl(gameUrl);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResult(PayEvent event) {
        if (isReload)
            webView.loadUrl(gameUrl);
    }

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

//    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            LogUtil.e(TAG, "focusChange: " + focusChange);
//            if (isPause && focusChange == AudioManager.AUDIOFOCUS_LOSS) {
//                requestAudioFocus();
//            }
//        }
//    };

    @Override
    public void initWindows() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    @Override
    public int getLayout() {
        return R.layout.activity_play_game;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

//        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        gameBean = getIntent().getParcelableExtra("game");
        if (gameBean.gameUrl.contains("?")) {
            gameUrl = gameBean.gameUrl + "&userId=" + UserData.getInstance().getId() + "&source=" + Constants.SOURCE;
        } else {
            gameUrl = gameBean.gameUrl + "?userId=" + UserData.getInstance().getId() + "&source=" + Constants.SOURCE;
        }
        setScreenOrientation(gameBean.screenDirection);
        webView = new WebView(this);
        contentView.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
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
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setUserAgentString(webSetting.getUserAgentString() + Constants.getPackageName());
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setSupportZoom(true);
        jsInterfaceFun = new JsInterfaceFun(this);
        webView.addJavascriptInterface(jsInterfaceFun, "gamePage");
        syncCookie();
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
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
                LogUtil.e(TAG, "---" + sslError.toString());
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

        inflate = LayoutInflater.from(this).inflate(R.layout.layout_popup_share, null);
        rootPopup = ButterKnife.findById(inflate, R.id.root_Popup);
        rootPopup.setOnClickListener(this);
        llBack = ButterKnife.findById(inflate, R.id.ll_back);
        llBack.setOnClickListener(this);
        llItem1 = ButterKnife.findById(inflate, R.id.ll_item_1);
        llItem1.setOnClickListener(this);
        llItem2 = ButterKnife.findById(inflate, R.id.ll_item_2);
        llItem2.setOnClickListener(this);
        llItem3 = ButterKnife.findById(inflate, R.id.ll_item_3);
        llItem3.setOnClickListener(this);
        llItem4 = ButterKnife.findById(inflate, R.id.ll_item_4);
        llItem4.setOnClickListener(this);
        tvCancel = ButterKnife.findById(inflate, R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        rootPopup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    rootReLay.setVisibility(View.GONE);
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        popupWindow = new PopupWindow(inflate, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rootPopup.setFocusable(true);
        rootPopup.setFocusableInTouchMode(true);
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        reLoading.setVisibility(View.GONE);

        LogUtil.d(TAG, "qbSdk version:" + QbSdk.getTbsVersion(this));

        webView.loadUrl(gameUrl);
        ivHeader.setImageURI(UserData.getInstance().getPictureUrl());
        showDragView();
    }

    private void setScreenOrientation(int direct) {
        if (1 == direct) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void syncCookie() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie("m.igamestorm.com", "source=android");
        cookieManager.setCookie("m.igamestorm.com", "pandanickName=" + UserData.getInstance().getNickname());
        cookieManager.setCookie("m.igamestorm.com", "pandauserId=" + UserData.getInstance().getId());
        cookieManager.setCookie("m.igamestorm.com", "pandapicUrl=" + UserData.getInstance().getPictureUrl());
        CookieSyncManager.getInstance().sync();
    }

//    private void requestAudioFocus() {
//        int result = mAudioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            LogUtil.e(TAG, "audio focus been granted");
//        }
//    }

    @Override
    public void initPresenter() {
        presenter = new PlayGameActivityPresenter(this, this);
        presenter.getRecommendData();
        presenter.startGame(gameBean.uniqueMark);
    }

    @OnClick({R.id.iv_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                if (clickOrMove) {
                    showGameManagerDialog();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                if (!isShare) {
                    exitTime = System.currentTimeMillis();
                    ToastUtils.showToastShort("再按一次退出游戏");
                }
                return false;
            } else {
                EventBus.getDefault().post(new RefreshWinAwardEvent(RefreshWinAwardEvent.REFRESH));
                finishCurrentActivity();
            }
        }
        return false;
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
        isPause = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != webView) {
            webView.destroy();
            contentView.removeAllViews();
        }
        presenter.destroy();
        System.gc();
//        mAudioManager.abandonAudioFocus(audioFocusChangeListener);
    }

    @Override
    public void loadGame(GameBean gameBean) {
        this.gameBean = gameBean;
        if (gameBean.gameUrl.contains("?")) {
            gameUrl = gameBean.gameUrl + "&userId=" + UserData.getInstance().getId() + "&source=" + Constants.SOURCE;
        } else {
            gameUrl = gameBean.gameUrl + "?userId=" + UserData.getInstance().getId() + "&source=" + Constants.SOURCE;
        }
        webView.loadUrl(gameUrl);
        presenter.startGame(gameBean.uniqueMark);
    }

    @Override
    public void setGameCountId(String gameCountId) {
        this.gameCountId = gameCountId;
    }

    @Override
    public void setRecommendData(List<GameBean> data) {
        recommendList.clear();
        recommendList.addAll(data);
    }


    @Override
    public void showGameManagerDialog() {
        final GameManagerDialog managerDialog = new GameManagerDialog(this, recommendList);
        managerDialog.setOnViewClickListener(new GameManagerDialog.OnViewClickListener() {
            @Override
            public void startGame(GameBean gameBean) {
                loadGame(gameBean);
                managerDialog.dismiss();
            }

            @Override
            public void shareGame() {
                startShare();
                managerDialog.dismiss();
            }

            @Override
            public void exit() {
                finishCurrentActivity();
                managerDialog.dismiss();
            }
        });
        managerDialog.show();
    }

    private void finishActivity() {
        EventBus.getDefault().post(new RefreshWinAwardEvent(RefreshWinAwardEvent.REFRESH));
        finishCurrentActivity();
    }

    private void startShare() {
        isShare = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rootReLay.setVisibility(View.VISIBLE);
                popupWindow.showAtLocation(webView, Gravity.BOTTOM, 0, 0);
            }
        }, 10);
    }

    @Override
    public void showLoading() {
        loadingDialog = new LoadingProgressDialog(this, false);
        loadingDialog.show();
        changeLoadingDes("游戏加载中进度：0%");
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != loadingDialog) {
            loadingDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                isShare = false;
                rootReLay.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.ll_item_1:
                ShareGameToContactDialog shareGameToContactDialog = new ShareGameToContactDialog(this, gameBean);
                shareGameToContactDialog.show();
                rootReLay.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.ll_item_2:
                shareTo(SHARE_MEDIA.QQ);
                break;
            case R.id.ll_item_3:
                shareTo(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_item_4:
                shareTo(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.tv_cancel:
                isShare = false;
                rootReLay.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
        }
    }

    public void userUnLogIn() {
        logInDialog = new LogInDialog(this, false);
        logInDialog.show();
    }

    private void shareTo(SHARE_MEDIA type) {
        isShare = false;
        UMImage umImage = null;
        if (null != gameBean.iconUrl && gameBean.iconUrl.length() > 0) {
            umImage = new UMImage(this, gameBean.iconUrl);
        } else {
            umImage = new UMImage(this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        }
        LogUtil.i(TAG, "---" + gameBean.iconUrl);
        String url = gameBean.url;
        url = url.replace("android", "");
        UMWeb web = new UMWeb(url);
        web.setTitle(gameBean.name);//标题
        web.setThumb(umImage);//缩略图
        web.setDescription(gameBean.propaganda);//描述
        new ShareAction(this)
                .withMedia(web)
                .setPlatform(type)
                .setCallback(umShareListener)
                .share();
        rootReLay.setVisibility(View.GONE);
        popupWindow.dismiss();
    }

    class JsInterfaceFun {
        private Context context;

        public JsInterfaceFun(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void finishGameActivity() {
            finishActivity();
        }

        @JavascriptInterface
        public String getUserInfo() {
            JSONObject object = new JSONObject();
            try {
                object.put("userId", UserData.getInstance().getId());
                object.put("nickName", UserData.getInstance().getNickname());
                object.put("headImg", UserData.getInstance().getPictureUrl());
                object.put("gameCountId", gameCountId);
                return object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object.toString();
        }

        @JavascriptInterface
        public void logIn() {
            if (!UserData.getInstance().isLogIn()) {
                userUnLogIn();
            }
        }

        @JavascriptInterface
        public void pay(String threeNo, String goodsName, double goodsPrice, int diamondNumber) {
            isReload = true;
            PayUtil.payH5(context, PayUtil.SOURCE_H5, goodsPrice, diamondNumber, threeNo, gameBean.uniqueMark, goodsName);
        }

        @JavascriptInterface
        public void share(String url) {
            startShare();
        }

        @JavascriptInterface
        public void chongzhi() {
            isReload = false;
            LogUtil.d(TAG, "---1");
            startActivity(new Intent(context, UptoActivity.class));
        }

        @JavascriptInterface
        public void chongzhi(String gameName, String gameId, int diamondNumber) {
            isReload = false;
            LogUtil.d(TAG, "---2" + gameId + "---" + gameName + "----" + diamondNumber);

            Intent intent = new Intent(context, UptoActivity.class);
            intent.putExtra("gameName", gameName);
            intent.putExtra("gameId", gameId);
            intent.putExtra("diamondNumber", diamondNumber);
            startActivity(intent);
        }
    }

    private View content;
    private int screenWidth;
    private int screenHeight;
    private boolean clickOrMove = true;//点击或拖动，点击为true，拖动为false
    private int downX, downY;//按下时的X，Y坐标
    private boolean hasMeasured = false;//ViewTree是否已被测量过，是为true，否为false

    private void showDragView() {
        content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);//获取界面的ViewTree根节点View

        DisplayMetrics dm = getResources().getDisplayMetrics();//获取显示屏属性
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            screenWidth = dm.heightPixels;
            screenHeight = dm.widthPixels;
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        }

        ViewTreeObserver vto = content.getViewTreeObserver();
        //获取ViewTree的监听器
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    screenHeight = content.getMeasuredHeight();//获取ViewTree的高度
                    hasMeasured = true;//设置为true，使其不再被测量。
                }
                return true;//如果返回false，界面将为空。
            }
        });
        ivHeader.setOnTouchListener(new View.OnTouchListener() {//设置按钮被触摸的时间
            int lastX, lastY; // 记录移动的最后的位置

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        downX = lastX;
                        downY = lastY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 移动中动态设置位置
                        int dx = (int) event.getRawX() - lastX;//位移量X
                        int dy = (int) event.getRawY() - lastY;//位移量Y
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        //限定按钮被拖动的范围
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                        //--限定按钮被拖动的范围
                        LogUtil.d(TAG, "--left:" + left + "---top:" + top + "---right:" + right + "---bottom:" + bottom);
                        v.layout(left, top, right, bottom);//按钮重画
                        // 记录当前的位置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //判断是单击事件或是拖动事件，位移量大于5则断定为拖动事件
                        if (Math.abs((int) (event.getRawX() - downX)) > 5 || Math.abs((int) (event.getRawY() - downY)) > 5) {
                            clickOrMove = false;
                        } else {
                            clickOrMove = true;
                        }
                        int dx1 = (int) event.getRawX() - lastX;//位移量X
                        int dy1 = (int) event.getRawY() - lastY;//位移量Y
                        int top1 = v.getTop() + dy1;
                        int bottom1 = v.getBottom() + dy1;
                        v.layout(screenWidth - v.getWidth(), top1, screenWidth, bottom1);//按钮重画
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                }
                return false;
            }
        });
    }
}
