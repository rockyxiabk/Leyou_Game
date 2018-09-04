package com.leyou.game.presenter.game;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.WolfKillRoomInfoBean;
import com.leyou.game.bean.WolfPropBean;
import com.leyou.game.ipresenter.fight.ISnatchPropDialog;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.WolfKillApi;
import com.leyou.game.widget.fightdialog.SnatchWolfKillIdentityDialog;

import rx.Observer;

/**
 * Description : com.leyou.game.presenter.game
 *
 * @author : rocky
 * @Create Time : 2017/9/5 下午6:58
 * @Modified Time : 2017/9/5 下午6:58
 */
public class SnatchPropDialogPresenter {

    private ISnatchPropDialog iSnatch;
    private Context context;

    public SnatchPropDialogPresenter(Context context, ISnatchPropDialog iSnatch) {
        this.context = context;
        this.iSnatch = iSnatch;
        getUserProp();
    }

    private void getUserProp() {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).getUserProp(), new Observer<ResultArray<WolfPropBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iSnatch.showPropData(null);
                LogUtil.e("tag", e.toString());
            }

            @Override
            public void onNext(ResultArray<WolfPropBean> resultArray) {
                if (resultArray.result == 1) {
                    iSnatch.showPropData(resultArray.data);
                } else {
                    iSnatch.showPropData(null);
                }
            }
        });
    }

    public void useProp(long roomId, long propMark) {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).snatchRole(roomId, propMark), new Observer<Result<WolfKillRoomInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<WolfKillRoomInfoBean> result) {

            }
        });
    }
}
