package com.leyou.game.util;

import android.widget.Toast;

import com.leyou.game.GameApplication;

/**
 * Description : Toast工具类
 *
 * @author : rocky
 * @Create Time : 2017/4/5 下午3:01
 * @Modified By: rocky
 * @Modified Time : 2017/4/5 下午3:01
 */
public class ToastUtils {

    private static Toast toast;

    /**
     * 强大的吐司，可以连续弹，不用等上一个显示
     *
     * @param text
     */
    public static void showToastShort(String text) {
        if (toast == null) {
            toast = Toast.makeText(GameApplication.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);//将文本设置为toast
        toast.show();
    }

    public static void showToastLong(String text) {
        if (toast == null) {
            toast = Toast.makeText(GameApplication.getContext(), text, Toast.LENGTH_LONG);
        }
        toast.setText(text);//将文本设置为toast
        toast.show();
    }
}
