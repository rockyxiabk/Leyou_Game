package com.leyou.game.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/6/12 下午3:43
 * @Modified Time : 2017/6/12 下午3:43
 */
public class ScreenUtil {
    private static ScreenUtil instance;
    private static WindowManager windowManager;

    public static ScreenUtil getInstance(Context context) {
        if (null == instance) {
            instance = new ScreenUtil();
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return instance;
    }

    public int getScreenHeight() {
        return windowManager.getDefaultDisplay().getHeight();
    }

    public int getScreenWidth() {
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
