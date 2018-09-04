package com.leyou.game.listener;

import com.leyou.game.bean.UserData;

import io.rong.imlib.RongIMClient;

/**
 * Description : com.leyou.game.listener
 * 设置连接状态监听，必须在 init 后进行调用。
 * 注意：建议设置在 Application 里面，这样才能在整个应用的生命周期，都能监听到状态变化。
 *
 * @author : rocky
 * @Create Time : 2017/7/28 上午11:34
 * @Modified Time : 2017/7/28 上午11:34
 */
public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

        switch (connectionStatus) {
            case NETWORK_UNAVAILABLE://网络不可用 -1
                UserData.getInstance().setRcIsConnected(false);
                break;
            case CONNECTED://连接成功 0
                UserData.getInstance().setRcIsConnected(true);
                break;
            case CONNECTING://连接中 1
                UserData.getInstance().setRcIsConnected(false);
                break;
            case DISCONNECTED://断开连接 2
                UserData.getInstance().setRcIsConnected(false);
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线 3
                UserData.getInstance().setRcIsConnected(false);
                break;
            case TOKEN_INCORRECT://token过期或者失效  4
                UserData.getInstance().setRcIsConnected(false);
                break;
            case SERVER_INVALID://服务器 失效 5
                UserData.getInstance().setRcIsConnected(false);
                break;
        }
    }
}