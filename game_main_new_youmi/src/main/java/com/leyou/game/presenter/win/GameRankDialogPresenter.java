package com.leyou.game.presenter.win;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameRankBean;
import com.leyou.game.ipresenter.win.IGameRankDialog;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/4/25 下午6:13
 * @Modified Time : 2017/4/25 下午6:13
 */
public class GameRankDialogPresenter {
    private String markId;
    private Context context;
    private IGameRankDialog iGameRankDialog;
    private Subscription subscribe;
    Observer<ResultArray<GameRankBean>> observer = new Observer<ResultArray<GameRankBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iGameRankDialog.showErrorView();
        }

        @Override
        public void onNext(ResultArray<GameRankBean> gameBeanResultArray) {
            if (null != gameBeanResultArray) {
                int result = gameBeanResultArray.result;
                if (0 != result) {
                    List<GameRankBean> data = gameBeanResultArray.data;
                    if (null != data && data.size() > 0) {
                        iGameRankDialog.showData(data);
                    } else {
                        iGameRankDialog.showErrorView();
                    }
                } else {
                    iGameRankDialog.showErrorView();
                }
            } else {
                iGameRankDialog.showErrorView();
            }
        }
    };

    public GameRankDialogPresenter(Context context, IGameRankDialog iGameRankDialog, String markId) {
        this.context = context;
        this.iGameRankDialog = iGameRankDialog;
        this.markId = markId;
        iGameRankDialog.showLoadingView();
        getData();
    }

    public void getData() {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameRankList(markId), observer);
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void unSubscribe() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

}
