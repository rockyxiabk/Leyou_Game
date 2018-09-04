package com.leyou.game.widget.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.leyou.game.R;

/**
 * Description : 消费记录筛选下拉菜单
 *
 * @author : rocky
 * @Create Time : 2017/12/1 下午2:31
 * @Modified Time : 2017/12/1 下午2:31
 */
public class ConsumeTabPopUp extends PopupWindow {

    private final Context context;
    private LayoutInflater inflater;
    private View inflate;

    public ConsumeTabPopUp(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        inflate = LayoutInflater.from(context).inflate(R.layout.layout_consume_popup_view, null);
    }

}
