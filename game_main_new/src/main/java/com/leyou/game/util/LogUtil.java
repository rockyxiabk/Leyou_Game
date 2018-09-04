package com.leyou.game.util;

import android.util.Log;

import com.leyou.game.BuildConfig;

/**
 * Description : log日志打印管理
 *
 * @author : rocky
 * @Create Time : 2017/4/5 下午3:06
 * @Modified By: rocky
 * @Modified Time : 2017/4/5 下午3:06
 */
public class LogUtil {

    public static void d(String tag, Object object) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, "-------" + object);
        }
    }

    public static void e(String tag, Object object, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "-------" + object, throwable);
        }
    }

    public static void e(String tag, Object object) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "-------" + object);
        }
    }

    public static void i(String tag, Object object) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, "-------" + object);
        }
    }
}
