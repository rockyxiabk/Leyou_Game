package com.leyou.game.widget.dialog.friend;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.leyou.game.R;

/**
 * Description : com.leyou.game.widget.dialog.friend
 *
 * @author : rocky
 * @Create Time : 2017/12/5 下午2:30
 * @Modified Time : 2017/12/5 下午2:30
 */
public class AddPopup extends PopupWindow {
    private View mainView;
    private LinearLayout layoutAddFriend, layoutCreateCrowd, layoutAddCrowd;
    View.OnClickListener listener;

    public AddPopup(Context context, View.OnClickListener listener) {
        super(context);
        this.listener = listener;
        //窗口布局
        mainView = LayoutInflater.from(context).inflate(R.layout.layout_popup_friend_add, null);
        //查找好友
        layoutAddFriend = ((LinearLayout) mainView.findViewById(R.id.layout_add_friend));
        //创建群聊
        layoutCreateCrowd = (LinearLayout) mainView.findViewById(R.id.layout_create_crowd);
        //查找群聊
        layoutAddCrowd = ((LinearLayout) mainView.findViewById(R.id.layout_add_crowd));
        //设置每个子布局的事件监听器
        if (listener != null) {
            layoutAddFriend.setOnClickListener(listener);
            layoutAddCrowd.setOnClickListener(listener);
            layoutCreateCrowd.setOnClickListener(listener);
        }
        setContentView(mainView);
        //设置宽度
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置高度
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置显示隐藏动画
        setAnimationStyle(R.style.popWindow_anim_style2);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }
}
