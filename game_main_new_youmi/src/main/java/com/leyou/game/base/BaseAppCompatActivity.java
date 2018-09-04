package com.leyou.game.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * Description : com.leyou.game.base
 *
 * @author : rocky
 * @Create Time : 2017/10/22 下午5:11
 * @Modified Time : 2017/10/22 下午5:11
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    protected Handler handler = new Handler();

    /**
     * 初始化窗口设置
     */
    public abstract void initWindows();

    /**
     * 加载布局文件
     *
     * @return
     */
    public abstract int getLayout();

    /**
     * 初始化控件
     *
     * @param savedInstanceState
     */
    public abstract void initWeight(Bundle savedInstanceState);

    /**
     * 初始化表示层
     */
    public abstract void initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        setContentView(getLayout());
        initWeight(savedInstanceState);
        initPresenter();
    }

    @Override
    public Resources getResources() {
        //获取到resources对象
        Resources res = super.getResources();
        //修改configuration的fontScale属性
        res.getConfiguration().fontScale = 1;
        //将修改后的值更新到metrics.scaledDensity属性上
        res.updateConfiguration(null, null);
        return res;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 进入动画退出动画,没有进入动画
     */
    public void finishCurrentActivity() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishCurrentActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean netWorkConnection() {
        ConnectivityManager mgrConn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    @Override
    public void openWifiSetting() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("网络连接错误,打开WIFI设置页面")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openWifiSetting();
                            }
                        }, 1000);
                    }
                })
                .setNegativeButton("取消", null).create().show();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.e(this.getClass().getName(), "LeakActivity has been recycled!");
    }
}
