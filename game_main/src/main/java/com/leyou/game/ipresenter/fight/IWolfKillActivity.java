package com.leyou.game.ipresenter.fight;

import com.leyou.game.bean.WolfKillRoomBean;

/**
 * Description : com.leyou.game.ipresenter
 *
 * @author : rocky
 * @Create Time : 2017/7/27 下午1:59
 * @Modified Time : 2017/7/27 下午1:59
 */
public interface IWolfKillActivity {

    void showLoading();

    void changeLoadingDes(String des);

    void dismissedLoading();

    void setUserRote(int gameCount, String winRate);

    void autoJoinRoom(long roomId);

    void createJoinRoom(WolfKillRoomBean roomId);
}
