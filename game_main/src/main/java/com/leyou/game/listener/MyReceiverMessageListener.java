package com.leyou.game.listener;

import com.leyou.game.util.LogUtil;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Description : 接收消息监听器的实现，所有接收到的消息、通知、状态都经由此处设置的监听器处理。
 * 包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
 *
 * @author : rocky
 * @Create Time : 2017/7/28 上午11:35
 * @Modified Time : 2017/7/28 上午11:35
 */
public class MyReceiverMessageListener implements RongIMClient.OnReceiveMessageListener {
    private static final String TAG = "MyReceiverMessageListener";

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param count   剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int count) {
        LogUtil.d(TAG, "----" + message.getTargetId() + "---" +message.getConversationType()+"---un read count:"+count);
        return false;
    }
}
