package com.leyou.game.base;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Description :
 *
 * @author : rocky
 * @Create Time : 2017/3/24 上午11:00
 * @Modified By: rocky
 * @Modified Time : 2017/3/24 上午11:00
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements IBaseActivity, View.OnClickListener {

    private Handler handler = new Handler();

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initPresenter();
    }

//    @Override
//    public Resources getResources() {
//        //获取到resources对象
//        Resources res = super.getResources();
//        //修改configuration的fontScale属性
//        res.getConfiguration().fontScale = 1;
//        //将修改后的值更新到metrics.scaledDensity属性上
//        res.updateConfiguration(null, null);
//        return res;
//    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void finishCurrentActivity() {
        finish();
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
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

        Log.d(this.getClass().getName(), "---------------------------LeakActivity has been recycled!");
    }
}
