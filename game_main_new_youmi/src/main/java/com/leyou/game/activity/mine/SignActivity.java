package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.user.SignBean;
import com.leyou.game.ipresenter.mine.ISignDiamond;
import com.leyou.game.presenter.treasure.SignDiamondPresenter;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.CircleProgressView;
import com.leyou.game.widget.dialog.mine.SignSuccessDialog;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 签到页面
 *
 * @author : rocky
 * @Create Time : 2017/10/28 下午4:40
 * @Modified By: rocky
 * @Modified Time : 2017/10/28 下午4:40
 */

public class SignActivity extends BaseActivity implements ISignDiamond {

    @BindView(R.id.iv_sign_back)
    ImageView ivSignBack;
    @BindView(R.id.re_sign_circle_view)
    RelativeLayout reSignBg;
    @BindView(R.id.circle_sign_progress_view)
    CircleProgressView circleSignProgressView;
    @BindView(R.id.tv_sign_total_diamond)
    TextView tvSignTotalDiamond;
    @BindView(R.id.webView)
    WebView webView;
    private SignDiamondPresenter presenter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_sign;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

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
        webView.loadUrl(Constants.MINE_SIGNED);
    }

    @Override
    public void initPresenter() {
        presenter = new SignDiamondPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reSignBg.setSelected(true);
                circleSignProgressView.dodo(50, 50, "签到", false);
            }
        },2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.iv_sign_back, R.id.circle_sign_progress_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sign_back:
                finishCurrentActivity();
                break;
            case R.id.circle_sign_progress_view:
                presenter.toSign();
                break;
        }
    }

    @Override
    public void showSignInfo(SignBean signBean) {
        if (null==signBean){
            return;
        }
        tvSignTotalDiamond.setText("我的钻石 " + signBean.signDiamondsNum + "");
        float progress = (signBean.continuityDay / ((float) signBean.maxDay)) * 100;
        int distanceDay = DataUtil.distanceDay(signBean.lastSignDate, System.currentTimeMillis());
        if ((distanceDay == 0)) {
            reSignBg.setSelected(false);
            circleSignProgressView.dodo(progress, progress, "已签到" + NumberFormatUtil.formatInteger(signBean.continuityDay) + "天", true);
        } else {
            reSignBg.setSelected(true);
            circleSignProgressView.dodo(progress, progress, "签到", false);
        }
    }

    @Override
    public void showSignSuccess(String virtualCoin) {
        presenter.getSignInfo();
        new SignSuccessDialog(this, virtualCoin).show();
    }

    @Override
    public void showSignFailed() {
        showMessage("签到失败");
    }

    @Override
    public void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }
}
