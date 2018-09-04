package com.leyou.game.activity.mine;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.WebViewActivity;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UpdateAppBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.mine.IAboutActivity;
import com.leyou.game.presenter.mine.AboutActivityPresenter;
import com.leyou.game.util.DataCleanManager;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.NewVersionDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 关于页面
 *
 * @author : rocky
 * @Create Time : 2017/4/18 上午10:56
 * @Modified By: rocky
 * @Modified Time : 2017/4/18 上午10:56
 */
public class AboutActivity extends BaseActivity implements IAboutActivity {
    private static final String TAG = "AboutActivity";
    @BindView(R.id.iv_about_back)
    ImageView ivAboutBack;
    @BindView(R.id.re_about_go_judge)
    RelativeLayout reAboutGoJudge;
    @BindView(R.id.re_about_faction_des)
    RelativeLayout reAboutFactionDes;
    @BindView(R.id.tv_about_versionCode)
    TextView tvAboutVersionCode;
    @BindView(R.id.tv_version_des)
    TextView tvVersionDes;
    @BindView(R.id.re_about_version_check)
    RelativeLayout reAboutVersionCheck;
    @BindView(R.id.re_about_share)
    RelativeLayout reAboutShare;
    @BindView(R.id.tv_cache_count)
    TextView tvCacheCount;
    @BindView(R.id.re_about_clear_cache)
    RelativeLayout reAboutClearCache;
    private LoadingProgressDialog loadingProgressDialog;
    private AboutActivityPresenter presenter;
    private LoadingProgressDialog loadingDialog;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("plat", "platform" + platform);
            Toast.makeText(AboutActivity.this, " 分享成功 ", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(AboutActivity.this, " 分享失败 ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AboutActivity.this, " 分享取消 ", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        tvAboutVersionCode.setText("版本号：" + Constants.getVerName());
        setSize();
    }

    @Override
    public void initPresenter() {
        presenter = new AboutActivityPresenter(this, this);
    }

    private void setSize() {
        try {
            long cacheSize = DataCleanManager.getFolderSize(new File(this.getExternalCacheDir().toString()));
            long rootCacheSize = DataCleanManager.getFolderSize(new File(Constants.ROOT_DIR));
            LogUtil.i("tag", "----cachesize----" + rootCacheSize + "---root-" + cacheSize);
            String size = DataCleanManager.getFormatSize(rootCacheSize + cacheSize);
            tvCacheCount.setText(size);
        } catch (Exception e) {
            LogUtil.d(TAG, e.toString());
        }
    }

    @OnClick({R.id.iv_about_back, R.id.re_about_go_judge, R.id.re_about_faction_des, R.id.re_about_version_check, R.id.re_about_share, R.id.re_about_clear_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_about_back:
                finishCurrentActivity();
                break;
            case R.id.re_about_go_judge:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.re_about_faction_des:
                Intent intentAbout = new Intent(this, WebViewActivity.class);
                intentAbout.putExtra("title", getString(R.string.about_faction_des));
                intentAbout.putExtra("url", Constants.MINE_FUNCTION);
                startActivity(intentAbout);
                break;
            case R.id.re_about_version_check:
                presenter.checkUpgrade();
                break;
            case R.id.re_about_share:
                share();
                break;
            case R.id.re_about_clear_cache:
                clearAppCache();
                break;
        }
    }

    private void share() {
        LogUtil.d(TAG, "-----userId-----" + UserData.getInstance().getId());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String url = Constants.MINE_DEVELOPER;
                //友盟微信朋友圈分享
                UMImage umImage = new UMImage(AboutActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_launcher));
                UMWeb umWeb = new UMWeb(Constants.MINE_DEVELOPER);
                umWeb.setThumb(umImage);
                umWeb.setDescription("游戏着，快乐着");
                umWeb.setTitle(getString(R.string.app_name));
                new ShareAction(AboutActivity.this)
                        .withText(getString(R.string.app_name))
                        .withMedia(umWeb)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener)
                        .open();
            }
        });
    }

    private void clearAppCache() {
        showLoading();
        changeLoadingDes(getString(R.string.clear_cache));
        this.deleteDatabase("webview.db");
        this.deleteDatabase("webviewCache.db");
        DataCleanManager.deleteFile(new File(Constants.IMAGE_CACHE_DIR));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvCacheCount.setText("0.0Mb");
                dismissedLoading();
                showMessageToast("清理成功");
            }
        }, 1500);
    }

    @Override
    public void showNewVersionView(UpdateAppBean data) {
        NewVersionDialog newVersionDialog = new NewVersionDialog(this, data);
        newVersionDialog.show();
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(this, false);
        loadingProgressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        loadingProgressDialog.setLoadingText(des);
    }

    @Override
    public void dismissedLoading() {
        loadingProgressDialog.dismiss();
    }

    @Override
    public void showMessageToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
