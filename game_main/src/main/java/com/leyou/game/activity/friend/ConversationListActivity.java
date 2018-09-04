package com.leyou.game.activity.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.MainTabEvent;
import com.leyou.game.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Description : 会话列表
 *
 * @author : rocky
 * @Create Time : 2017/9/6 下午6:18
 * @Modified By: rocky
 * @Modified Time : 2017/9/6 下午6:18
 */

public class ConversationListActivity extends FragmentActivity {

    private static final String TAG = "ConversationListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        Intent intent = getIntent();
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("push") != null) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push").equals("true")) {
                enterActivity();
            }

        } else {//通知过来
            //程序切到后台，收到消息后点击进入,会执行这里
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                enterActivity();
            } else {
                EventBus.getDefault().post(new MainTabEvent(1));
                finish();
            }
        }
    }

    private void enterActivity() {
        if (!UserData.getInstance().isLogIn()) {
            startActivity(new Intent(ConversationListActivity.this, RegisterActivity.class));
            finish();
        } else {
            reconnect();
        }
    }

    private void reconnect() {
        RongIM.connect(UserData.getInstance().getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtil.e(TAG, "---onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String s) {
                LogUtil.i(TAG, "---onSuccess--" + s);
                RongIM.getInstance().setCurrentUserInfo(new UserInfo(UserData.getInstance().getId(), UserData.getInstance().getNickname(), Uri.parse(UserData.getInstance().getPictureUrl())));
                RongIM.getInstance().setMessageAttachedUserInfo(true);
                EventBus.getDefault().post(new MainTabEvent(1));
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                LogUtil.e(TAG, "---onError--" + e);
            }
        });
    }
}
