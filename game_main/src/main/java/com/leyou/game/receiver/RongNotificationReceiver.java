package com.leyou.game.receiver;

import android.content.Context;

import com.leyou.game.util.LogUtil;

import io.rong.imkit.RongNotificationManager;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Description :融云 push消息接受
 *
 * @author : rocky
 * @Create Time : 2017/7/22 下午2:08
 * @Modified Time : 2017/7/22 下午2:08
 */
public class RongNotificationReceiver extends PushMessageReceiver {
    private static final String TAG = "RongNotificationReceiver";

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        LogUtil.e(TAG, "----message arrived:");
        return false; // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        LogUtil.e(TAG, "----message click:");
        return false; // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    }
}
