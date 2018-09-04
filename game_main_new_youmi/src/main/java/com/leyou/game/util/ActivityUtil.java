package com.leyou.game.util;

import android.app.ActivityManager;
import android.content.Context;

import com.leyou.game.GameApplication;
import com.leyou.game.base.BaseFragment;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/12/3 下午4:03
 * @Modified Time : 2017/12/3 下午4:03
 */
public class ActivityUtil {
    /**
     * 获取当前运行的Activity名称
     *
     * @return
     */
    public static String getRunningActivityName() {
        ActivityManager manager = (ActivityManager) GameApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        String activityName = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        String name = activityName.substring(activityName.lastIndexOf(".") + 1, activityName.length());
        return name;
    }

    /**
     * 获取当前显示的Fragment名称
     *
     * @return
     */
    public static String getCurrentFragmentName(BaseFragment fragment) {
        String fragName = fragment.getClass().toString();
        fragName = fragName.substring(fragName.lastIndexOf(".") + 1, fragName.length());
        return fragName;
    }
}
