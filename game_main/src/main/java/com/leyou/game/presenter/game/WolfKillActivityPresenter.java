package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WolfKillRoomBean;
import com.leyou.game.bean.WolfKillRoomInfoBean;
import com.leyou.game.bean.WolfRoleBean;
import com.leyou.game.ipresenter.fight.IWolfKillActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.WolfKillApi;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.game
 *
 * @author : rocky
 * @Create Time : 2017/9/2 下午6:59
 * @Modified Time : 2017/9/2 下午6:59
 */
public class WolfKillActivityPresenter {

    private Context context;
    private IWolfKillActivity iWolfKillActivity;

    public WolfKillActivityPresenter(Context context, IWolfKillActivity iWolfKillActivity) {
        this.context = context;
        this.iWolfKillActivity = iWolfKillActivity;
        getUserRote();
    }

    private void getUserRote() {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).getUserWinRate(), new Observer<Result<WolfRoleBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWolfKillActivity.setUserRote(0, "0%");
            }

            @Override
            public void onNext(Result<WolfRoleBean> wolfRoleBeanResult) {
                if (wolfRoleBeanResult.result == 1) {
                    WolfRoleBean data = wolfRoleBeanResult.data;
                    iWolfKillActivity.setUserRote(data.gameCount, data.winRate);
                } else {
                    iWolfKillActivity.setUserRote(0, "0%");
                }
            }
        });
    }

    public void autoJoinRoom(int type) {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).autoJoinRoom(type), new Observer<Result<WolfKillRoomInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWolfKillActivity.autoJoinRoom(0);
            }

            @Override
            public void onNext(Result<WolfKillRoomInfoBean> wolfKillRoomInfoBeanResult) {
                if (wolfKillRoomInfoBeanResult.result == 1) {
                    WolfKillRoomInfoBean data = wolfKillRoomInfoBeanResult.data;
                    if (data.roomId > 0) {
                        iWolfKillActivity.autoJoinRoom(data.roomId);
                    } else {
                        iWolfKillActivity.autoJoinRoom(0);
                    }
                } else {
                    iWolfKillActivity.autoJoinRoom(0);
                }
            }
        });
    }

    public void createRoom(int type) {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).createRoom(type), new Observer<Result<WolfKillRoomBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWolfKillActivity.createJoinRoom(null);
            }

            @Override
            public void onNext(Result<WolfKillRoomBean> wolfKillRoomBeanResult) {
                if (wolfKillRoomBeanResult.result == 1) {
                    WolfKillRoomBean data = wolfKillRoomBeanResult.data;
                    if (null != data) {
                        iWolfKillActivity.createJoinRoom(data);
                    } else {
                        iWolfKillActivity.createJoinRoom(null);
                    }
                } else {
                    iWolfKillActivity.createJoinRoom(null);
                }
            }
        });
    }
}
