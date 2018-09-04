package com.leyou.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.leyou.game.activity.MainActivity;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.activity.WebViewActivity;
import com.leyou.game.adapter.InducePagerAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.AdBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.ISplashActivity;
import com.leyou.game.presenter.SplashPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ddd.eee.fff.AdManager;
import ddd.eee.fff.nm.vdo.VideoAdManager;
import pl.droidsonroids.gif.GifImageView;
import rx.Observable;
import rx.Subscriber;

/**
 * Description : 应用启动页
 *
 * @author : rocky
 * @Create Time : 2017/3/29 上午10:59
 * @Modified By: rocky
 * @Modified Time : 2017/3/29 上午10:59
 */

public class SplashActivity extends BaseActivity implements ISplashActivity, ViewPager.OnPageChangeListener {
    private static final String TAG = "SplashActivity";
    @BindView(R.id.vp_induce)
    ViewPager vpInduce;
    @BindView(R.id.btn_start_app)
    Button btnStartApp;
    @BindView(R.id.re_ad)
    LinearLayout reAd;
    @BindView(R.id.iv_gif_ad)
    GifImageView ivGifAd;
    @BindView(R.id.tv_count_timer)
    TextView tvTimer;
    private SplashPresenter presenter;
    private InducePagerAdapter adapter;
    private int[] images = {R.mipmap.induce_one, R.mipmap.induce_two, R.mipmap.induce_three};
    private List<ImageView> imageViews = new ArrayList<>();
    private boolean isClickedAd = false;
    private boolean isGoApp = false;//是否直接打开应用
    private CountDownTimer timer = new CountDownTimer(5000, 100) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvTimer.setText(millisUntilFinished / 1000 + " 跳过");
        }

        @Override
        public void onFinish() {
            if (!isClickedAd) {
                if (UserData.getInstance().isLogIn()) {
                    startApp();
                } else {
                    startLogoIn();
                }
            }
        }
    };
    private AdBean adBean;

    @Override
    public void initWindows() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initFiles();
        initAD();
    }

    private void initAD() {
        AdManager.getInstance(this).init("e6c367ea1f35e193", "7cc50df92b3a14b1", true);
        VideoAdManager.getInstance(this).setUserId(UserData.getInstance().getId());
        VideoAdManager.getInstance(this).requestVideoAd(this);
    }

    @Override
    public void initPresenter() {
        presenter = new SplashPresenter(this, this);
        initSplashImage();
    }

    /**
     * 初始化文件夹
     */
    private void initFiles() {
        File dir = new File(Constants.UPDATE_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        dir = new File(Constants.IMAGE_CACHE_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        dir = new File(Constants.WEB_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        dir = new File(Constants.WEB_APP_CACHE_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        dir = new File(Constants.WEB_DATABASES_CACHE_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        dir = new File(Constants.WEB_GEOLOCATION_CACHE_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    /**
     * 加载开始页
     */
    private void initSplashImage() {
        boolean isShow = SPUtil.getBoolean(this, SPUtil.SPLASH, "isShow");
        if (!isShow) {
            for (int i = 0; i < images.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(images[i]);
                imageViews.add(imageView);
            }
            vpInduce.setVisibility(View.VISIBLE);
            reAd.setVisibility(View.GONE);
            adapter = new InducePagerAdapter(this, imageViews);
            vpInduce.setAdapter(adapter);
            vpInduce.setOnPageChangeListener(this);
        } else {
            vpInduce.setVisibility(View.GONE);
            // TODO: 2017/9/21 开启广告
            reAd.setVisibility(View.VISIBLE);
            tvTimer.setClickable(true);
            if (HttpUtil.isNetworkConnected(this)) {
                presenter.getAd();
            } else {
                startLogoIn();
            }
            // TODO: 2017/9/21 关闭广告
//            presenter.verifyLogoIn();
//            tvTimer.setVisibility(View.GONE);
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

    @OnClick({R.id.btn_start_app, R.id.tv_count_timer, R.id.iv_gif_ad})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_app:
                SPUtil.setBoolean(this, SPUtil.SPLASH, "isShow", true);
                startLogoIn();
                break;
            case R.id.iv_gif_ad:
                timer.cancel();
                if (null != adBean) {
                    if (!TextUtils.isEmpty(adBean.url)) {
                        Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra("title", "广告推荐");
                        intent.putExtra("url", adBean.url);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                        finishCurrentActivity();
                    }
                }
                break;
            case R.id.tv_count_timer:
                timer.cancel();
                if (UserData.getInstance().isLogIn()) {
                    startApp();
                } else {
                    startLogoIn();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void showMessageToast(String msg) {
        ToastUtils.showToastShort(msg);
    }

    @Override
    public void showAdView(AdBean adBean) {
        if (null != adBean) {
            isGoApp = false;
            this.adBean = adBean;
            reAd.setVisibility(View.VISIBLE);
            tvTimer.setVisibility(View.VISIBLE);
            Glide.with(this).load(adBean.pictureUrl).error(R.mipmap.induce_one).into(ivGifAd);
            timer.start();
            presenter.verifyLogoIn();
        } else {
            tvTimer.setVisibility(View.GONE);
            isGoApp = true;
        }
    }

    @Override
    public void setLogInState(boolean isLogIn) {
        tvTimer.setClickable(true);
        if (isGoApp) {
            if (UserData.getInstance().isLogIn()) {
                startApp();
            } else {
                startLogoIn();
            }
        }
    }

    @Override
    public void startLogoIn() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finishCurrentActivity();
    }

    @Override
    public void startApp() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finishCurrentActivity();
            }
        }, 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == images.length - 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnStartApp.animate().translationY(ScreenUtil.dp2px(SplashActivity.this,-60))
                            .alphaBy(0.9f).setStartDelay(100).setDuration(300).start();
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnStartApp.animate().translationY(ScreenUtil.dp2px(SplashActivity.this,60))
                            .alphaBy(0.0f).setStartDelay(100).setDuration(300).start();
                }
            });

        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
