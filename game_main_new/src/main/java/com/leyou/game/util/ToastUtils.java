package com.leyou.game.util;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leyou.game.GameApplication;
import com.leyou.game.R;

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
        View view = LayoutInflater.from(GameApplication.getContext()).inflate(R.layout.layout_custom_toast_0, null);
        toast.setView(view);

        RelativeLayout re = (RelativeLayout) view.findViewById(R.id.re_custom_toast);
        re.setBackgroundResource(R.drawable.toast_raduis_shape);

        TextView tv = (TextView) view.findViewById(R.id.tv_custom_toast_message);
        tv.setTextColor(GameApplication.getContext().getResources().getColor(R.color.white));
        tv.setText(text);
        toast.show();
    }

    public static void showToastLong(String text) {
        if (toast == null) {
            toast = Toast.makeText(GameApplication.getContext(), text, Toast.LENGTH_LONG);
        }
        View view = LayoutInflater.from(GameApplication.getContext()).inflate(R.layout.layout_custom_toast_0, null);
        toast.setView(view);

        RelativeLayout re = (RelativeLayout) view.findViewById(R.id.re_custom_toast);
        re.setBackgroundResource(R.drawable.toast_raduis_shape);

        TextView tv = (TextView) view.findViewById(R.id.tv_custom_toast_message);
        tv.setTextColor(GameApplication.getContext().getResources().getColor(R.color.white));
        tv.setText(text);
        toast.show();
    }
}
