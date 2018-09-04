package com.leyou.game.widget.dialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.UpdateAppBean;
import com.leyou.game.service.DownloadService;
import com.leyou.game.util.LogUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 提示更新版本
 *
 * @author : rocky
 * @Create Time : 2017/6/28 上午11:39
 * @Modified Time : 2017/6/28 上午11:39
 */
public class NewVersionDialog extends BaseDialog {

    private static final String TAG = "NewVersionDialog";

    @BindView(R.id.tv_version_des)
    TextView tvVersionDes;
    @BindView(R.id.progress_download)
    RoundCornerProgressBar progressBar;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_install)
    Button btnInstall;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    private Context context;
    private UpdateAppBean updateAppBean = new UpdateAppBean();
    private boolean isBindService;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
            DownloadService downloadService = binder.getService();

            //接口回调，下载进度
            downloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                @Override
                public void onProgress(float fraction) {
                    LogUtil.i(TAG, "下载进度：" + fraction);
                    if (fraction * 100f < 10) {
                        progressBar.setProgress(8f);
                    } else {
                        progressBar.setProgress(fraction * 100f);
                    }
                    tvProgress.setText((int) (fraction * 100f) + "%");
                    //判断是否真的下载完成进行安装了，以及是否注册绑定过服务
                    if (fraction == DownloadService.UNBIND_SERVICE && isBindService) {
                        context.unbindService(conn);
                        isBindService = false;
                        changeInstallView(2);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public NewVersionDialog(Context context, UpdateAppBean updateAppBean) {
        super(context);
        this.context = context;
        this.updateAppBean = updateAppBean;
        setCancelable(false);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_new_version;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        tvVersionDes.setText(updateAppBean.description);
        checkFile();
    }

    private void checkFile() {
        File file = new File(Constants.UPDATE_DIR + updateAppBean.name + "-" + updateAppBean.versionCode + ".apk");
        if (file.exists()) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(Constants.UPDATE_DIR + updateAppBean.name + "-" + updateAppBean.versionCode + ".apk", 0);
            String packageName = packageInfo.packageName;
            double versionCode = packageInfo.versionCode;
            if (context.getPackageName().equalsIgnoreCase(packageName) && Constants.getVerCode() < versionCode) {
                changeInstallView(2);
            } else {
                changeInstallView(0);
            }
        } else {
            changeInstallView(0);
        }
    }

    private void changeInstallView(int type) {
        btnConfirm.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        btnInstall.setVisibility(View.GONE);
        tvProgress.setVisibility(View.GONE);
        switch (type) {
            case 0:
                btnConfirm.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvProgress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnInstall.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void install() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(Constants.UPDATE_DIR + updateAppBean.name + "-" + updateAppBean.versionCode + ".apk")), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    @OnClick({R.id.btn_confirm, R.id.btn_install})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                btnConfirm.setEnabled(false);
                changeInstallView(1);
                Intent intent = new Intent(context, DownloadService.class);
                intent.putExtra("upgradeAppBean", updateAppBean);
                isBindService = context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_install:
                install();
                break;
        }
    }
}
