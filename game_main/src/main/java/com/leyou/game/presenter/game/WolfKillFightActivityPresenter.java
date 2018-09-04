package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WolfKillRoomInfoBean;
import com.leyou.game.ipresenter.fight.IWolfKillFightActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.WinAwardApi;
import com.leyou.game.util.api.WolfKillApi;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/7/30 下午4:35
 * @Modified Time : 2017/7/30 下午4:35
 */
public class WolfKillFightActivityPresenter {

    private String roomId;
    private Context context;
    private IWolfKillFightActivity iWolfKillFightActivity;
    private Observer<Result<WolfKillRoomInfoBean>> roomInfoObserve = new Observer<Result<WolfKillRoomInfoBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Result<WolfKillRoomInfoBean> wolfKillRoomInfoBeanResult) {
            if (wolfKillRoomInfoBeanResult.result == 1) {
                WolfKillRoomInfoBean data = wolfKillRoomInfoBeanResult.data;
                if (null != data) {
                    iWolfKillFightActivity.showWolfKillRoomInfo(data);
                } else {
                    iWolfKillFightActivity.showWolfKillRoomInfo(null);
                }
            }
        }
    };

    public WolfKillFightActivityPresenter(Context context, IWolfKillFightActivity iWolfKillFightActivity, String mChannel) {
        this.context = context;
        this.iWolfKillFightActivity = iWolfKillFightActivity;
        this.roomId = mChannel;
        getRoomInfoAndPlayer();
    }

    public void getRoomInfoAndPlayer() {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).getRoomInfo(roomId), roomInfoObserve);
    }

    public void prepareGame(final boolean isReadyGame) {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).playerPrepare(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWolfKillFightActivity.prepareGameResult(false,isReadyGame);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iWolfKillFightActivity.prepareGameResult(true,!isReadyGame);
                } else {
                    iWolfKillFightActivity.prepareGameResult(false,isReadyGame);
                }
            }
        });
    }

    public void quitRoom(long roomId) {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).quitRoom(roomId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iWolfKillFightActivity.currentQuitRoom(false);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iWolfKillFightActivity.currentQuitRoom(true);
                } else {
                    iWolfKillFightActivity.currentQuitRoom(false);
                }
            }
        });
    }
}
