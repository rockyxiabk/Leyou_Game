package com.leyou.game.base;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Description : WebView抽象类
 *
 * @author : rocky
 * @Create Time : 2017/3/24 上午11:00
 * @Modified By: ***
 * @Modified Time : 2017/3/24 上午11:00
 */
public abstract class BaseWebViewActivity extends AppCompatActivity {


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

    /**
     * 进入动画，退出动画
     */
    public void finishCurrentActivityIO() {
        finish();
    }

    /**
     * 进入动画退出动画,没有进入动画
     */
    public void finishCurrentActivity() {
        finish();
    }

    /**
     * 只有进入动画,没有退出动画
     */
    public void finishCurrentActivityI() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_no);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        Log.d(this.getClass().getName(), "---------------------------LeakActivity has been recycled!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
