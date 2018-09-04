package com.leyou.game.rong.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.rong.message.DiamondRedPacketMessage;
import com.leyou.game.rong.message.InviteJoinWolfKillMessage;
import com.leyou.game.util.ToastUtils;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Description : 自定义扩展输入功能区
 *
 * @author : rocky
 * @Create Time : 2017/8/15 上午9:40
 * @Modified Time : 2017/8/15 上午9:40
 */
public class MyCustomPluginModule implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.rc_ext_diamond_selector);
        return drawable;
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getResources().getString(R.string.diamond_red_packet);
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        DiamondRedPacketMessage packetMessage = DiamondRedPacketMessage.obtain("", "", "", "", "", 0, "");
        Message message = Message.obtain(Constants.getCurrentConversationId(), Constants.getConversationType(), packetMessage);
        RongIM.getInstance().sendMessage(message, "[钻石红包]", "[钻石红包]", new IRongCallback.ISendMediaMessageCallback() {
            @Override
            public void onProgress(Message message, int i) {

            }

            @Override
            public void onCanceled(Message message) {

            }

            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                ToastUtils.showToastShort("success");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
