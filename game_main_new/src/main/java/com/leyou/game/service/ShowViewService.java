package com.leyou.game.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Description : 在应用顶层View 显示view
 *
 * @author : rocky
 * @Create Time : 2017/6/28 上午10:28
 * @Modified Time : 2017/6/28 上午10:28
 */
public class ShowViewService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
}
