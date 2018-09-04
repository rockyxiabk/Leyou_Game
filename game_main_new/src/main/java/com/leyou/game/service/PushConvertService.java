package com.leyou.game.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.WindowManager;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.R;
import com.leyou.game.activity.friend.NewFriendActivity;
import com.leyou.game.activity.mine.ExchangeActivity;
import com.leyou.game.activity.MainActivity;
import com.leyou.game.bean.PushBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.FriendAddEvent;
import com.leyou.game.event.MainTabEvent;
import com.leyou.game.event.RefreshTreasureEvent;
import com.leyou.game.receiver.PushReceiver;
import com.leyou.game.util.GsonUtil;
import com.leyou.game.util.SystemUtil;
import com.leyou.game.widget.dialog.OffLineDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Description : push消息处理服务
 *
 * @author : rocky
 * @Create Time : 2017/6/27 上午9:40
 * @Modified By: rocky
 * @Modified Time : 2017/6/27 上午9:40
 */
public class PushConvertService extends Service {
    public PushConvertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String json = intent.getStringExtra("push");
        if (!TextUtils.isEmpty(json)) {
            startParamsJson(json);
        }
        return START_NOT_STICKY;
    }

    private void startParamsJson(String json) {
        PushBean pushBean = GsonUtil.changeGsonToBean(json, PushBean.class);
        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        switch (pushBean.type) {
            case 0://版本更新
                Intent newVersion = new Intent(Constants.NEW_VERSION_ACTION);
                sendBroadcast(newVersion);
                break;
            case 1://中奖信息
                if (!SystemUtil.isAppAlive(this)) {//应用没有启动
                    Intent prizeNotify = new Intent(this, PushReceiver.class);
                    prizeNotify.putExtra("pushBean", pushBean);
                    prizeNotify.putExtra("winId", pushBean.id);
                    PendingIntent prizeIntent = PendingIntent.getBroadcast(this, new Random().nextInt(1000), prizeNotify, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder announceBuilder = new NotificationCompat.Builder(this)
                            .setContentTitle(pushBean.title)
                            .setContentText(pushBean.des)
                            .setTicker(pushBean.des)
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                            .setContentIntent(prizeIntent);
                    Notification prizeNotifyBuilder = announceBuilder.build();
                    nm.notify((int) (Math.random() * 1000), prizeNotifyBuilder);
                } else {//应用已启动
                    Intent salePush = new Intent(Constants.PRIZE_ACTION);
                    salePush.putExtra("pushBean", pushBean);
                    salePush.putExtra("winId", pushBean.id);
                    sendBroadcast(salePush);
                }
                break;
            case 2://宝库被攻占
                Intent raceIntent = new Intent(this, MainActivity.class);
                raceIntent.putExtra("pushBean", pushBean);
                PendingIntent raceContentIntent = PendingIntent.getActivity(this, new Random().nextInt(1000), raceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder zeroBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(pushBean.title)
                        .setContentText(pushBean.des)
                        .setTicker(pushBean.des)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(raceContentIntent);
                Notification raceNotify = zeroBuilder.build();
                nm.notify((int) (Math.random() * 2000 + 1000), raceNotify);
                EventBus.getDefault().post(new MainTabEvent(0));
                EventBus.getDefault().post(new RefreshTreasureEvent(1));
                break;
            case 3://出售成功
                Intent sellIntent = new Intent(this, ExchangeActivity.class);
                PendingIntent sellContentIntent = PendingIntent.getActivity(this, new Random().nextInt(1000), sellIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder sellBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(pushBean.title)
                        .setContentText(pushBean.des)
                        .setTicker(pushBean.des)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(sellContentIntent);
                Notification sellNotify = sellBuilder.build();
                nm.notify((int) (Math.random() * 3000 + 2000), sellNotify);
                break;
            case 4://赢大奖游戏名次被超越
                Intent winIntent = new Intent(this, MainActivity.class);
                winIntent.putExtra("pushBean", pushBean);
                PendingIntent winContentIntent = PendingIntent.getActivity(this, new Random().nextInt(1000), winIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder winBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(pushBean.title)
                        .setContentText(pushBean.des)
                        .setTicker(pushBean.des)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(winContentIntent);
                Notification winNotify = winBuilder.build();
                nm.notify(4, winNotify);
                EventBus.getDefault().post(new MainTabEvent(2));
                break;
            case 5:
                break;
            case 100://下线通知
                if (UserData.getInstance().isLogIn() && pushBean.id.equalsIgnoreCase(UserData.getInstance().getId())) {
//                    if (!SystemUtil.isAppAlive(this)) {//应用没有启动
//                        Intent offLine = new Intent(this, MainActivity.class);
//                        offLine.putExtra("pushBean", pushBean);
//                        PendingIntent offLineIntent = PendingIntent.getActivity(this, new Random().nextInt(1000), offLine, PendingIntent.FLAG_UPDATE_CURRENT);
//                        NotificationCompat.Builder offLineBuilder = new NotificationCompat.Builder(this)
//                                .setContentTitle(pushBean.title)
//                                .setContentText(pushBean.des)
//                                .setTicker(pushBean.des)
//                                .setDefaults(Notification.DEFAULT_ALL)
//                                .setWhen(System.currentTimeMillis())
//                                .setAutoCancel(true)
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
//                                .setContentIntent(offLineIntent);
//                        Notification offLineNotify = offLineBuilder.build();
//                        nm.notify(100, offLineNotify);
//                    } else {
//                        Intent salePush = new Intent(Constants.OFF_LINE_ACTION);
//                        salePush.putExtra("pushBean", pushBean);
//                        sendBroadcast(salePush);
//                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent salePush = new Intent(Constants.OFF_LINE_ACTION);
                        salePush.putExtra("pushBean", pushBean);
                        sendBroadcast(salePush);
                    } else {
                        OffLineDialog offLineDialog = new OffLineDialog(GameApplication.getContext(), pushBean);
                        offLineDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                        offLineDialog.show();
                    }
                    GameApplication.unBindAlias(UserData.getInstance().getId());
                    GameApplication.logout();
                    UserData.getInstance().clearUserInfo();
                }
                break;
            case 101:
                Intent recFriend = new Intent(Constants.FRIEND_ACTION);
                sendBroadcast(recFriend);
                break;
            case 1000://好友申请添加
                Intent newFriendIntent = new Intent(this, NewFriendActivity.class);
                PendingIntent newIntent = PendingIntent.getActivity(this, new Random().nextInt(1000), newFriendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder newIntentBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(pushBean.title)
                        .setContentText(pushBean.des)
                        .setTicker(pushBean.des)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(newIntent);
                Notification newIntentNotify = newIntentBuilder.build();
                nm.notify(1000, newIntentNotify);
                EventBus.getDefault().post(new FriendAddEvent(true));
                EventBus.getDefault().post(new MainTabEvent(1));
                break;
            case 1024://钻石兑换流量
                Intent convert = new Intent(Constants.DIAMOND_CONVERT);
                sendBroadcast(convert);
                break;
        }
    }
}
