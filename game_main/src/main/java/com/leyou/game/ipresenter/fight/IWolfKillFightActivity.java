package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.WolfKillRoomInfoBean;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/30 下午4:35
 * @Modified Time : 2017/7/30 下午4:35
 */
public interface IWolfKillFightActivity {

    void showWolfKillRoomInfo(WolfKillRoomInfoBean roomInfoBean);

    void prepareGameResult(boolean flag, boolean isReady);

    void currentQuitRoom(boolean result);

    void showMessageToast(String msg);
}
