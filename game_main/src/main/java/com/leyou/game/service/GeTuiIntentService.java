package com.leyou.game.service;

import android.content.Context;
import android.text.TextUtils;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.bean.UserData;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.PushMessageUtil;
import com.leyou.game.util.SPUtil;

/**
 * Description : 个推自定义消息接受类
 *
 * @author : rocky
 * @Create Time : 2017/5/17 上午10:04
 * @Modified Time : 2017/5/17 上午10:04
 */

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class GeTuiIntentService extends GTIntentService {
    public GeTuiIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        LogUtil.e(TAG, "onReceive pid -> " + "pid = " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String messageId = msg.getMessageId();
        String payLoad = new String(msg.getPayload());
        String payloadId = msg.getPayloadId();
        String taskId = msg.getTaskId();
        String clientId = msg.getClientId();
        String pkgName = msg.getPkgName();
        String appId = msg.getAppid();
        LogUtil.d(TAG, "messageId-->" + messageId + "\npayload:-->" + payLoad + "\npayloadId-->" + payloadId + "\ntaskId-->" + taskId + "\nclientId:-->" + clientId + "\npkgName-->" + pkgName + "\nappId-->" + appId);
        if (!TextUtils.isEmpty(payLoad)) {
            PushMessageUtil.startService(context, payLoad);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientId) {
        LogUtil.e(TAG, "onReceiveClientId -> " + "clientId = " + clientId);
        HttpUtil.updateClientId(clientId);
        SPUtil.setString(context, SPUtil.THREE_ID, "getui_id", clientId);
        Constants.setClientId(clientId);

    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        LogUtil.e(TAG, "onReceive -> " + "online state = " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}
