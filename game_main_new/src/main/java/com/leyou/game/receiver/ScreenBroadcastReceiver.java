package com.leyou.game.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.leyou.game.util.LogUtil;

/**
 * Description :  屏幕开锁屏幕广播监听
 *
 * @author : rocky
 * @Create Time : 2017/9/12 下午7:12
 * @Modified By: rocky
 * @Modified Time : 2017/9/12 下午7:12
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenBroadcastReceiver";

    public ScreenBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            LogUtil.d(TAG, "------screen on");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            LogUtil.d(TAG, "------screen off");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            LogUtil.d(TAG, "------screen present");
        } else {
            LogUtil.d(TAG, "------screen unknown");
        }
    }
}
