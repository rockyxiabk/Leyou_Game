package com.leyou.game.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.leyou.game.Constants;
import com.leyou.game.activity.MainActivity;
import com.leyou.game.bean.PushBean;
import com.leyou.game.util.SystemUtil;

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";
    private Handler handler = new Handler();
    private long delayedTimes = 500;

    public PushReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final PushBean pushInfo = intent.getParcelableExtra("pushBean");
        if (SystemUtil.isAppAlive(context)) {//应用已启动
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        } else {//应用没有启动
            delayedTimes = 3000;
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent salePush = null;
                switch (pushInfo.type) {
                    case 1:
                        salePush = new Intent(Constants.PRIZE_ACTION);
                        break;
                    case 100:
                        salePush = new Intent(Constants.OFF_LINE_ACTION);
                        break;
                }
                salePush.putExtra("pushBean", pushInfo);
                context.sendBroadcast(salePush);

            }
        }, delayedTimes);
    }
}
