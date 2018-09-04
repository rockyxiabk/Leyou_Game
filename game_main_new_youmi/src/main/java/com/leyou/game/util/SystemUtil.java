package com.leyou.game.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Description : 判断当前应用是否已经打开
 *
 * @author : rocky
 * @Create Time : 2017/4/5 下午3:07
 * @Modified By: rocky
 * @Modified Time : 2017/4/5 下午3:07
 */
public class SystemUtil {
    /**
     * 判断应用是否已经启动
     *
     * @param context 一个context
     * @return boolean
     */
    public static boolean isAppAlive(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
