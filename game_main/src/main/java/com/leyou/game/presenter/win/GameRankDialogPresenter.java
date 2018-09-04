package com.leyou.game.presenter.win;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.GameRankBean;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.win.IGameRankDialog;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.WinAwardApi;

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
    private long markId;
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
            iGameRankDialog.showError(context.getString(R.string.data_load_failed));
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
//                        iGameRankDialog.showError(context.getString(R.string.data_load_failed));
                    }
                } else {
                    iGameRankDialog.showErrorView();
//                    iGameRankDialog.showError(context.getString(R.string.data_load_failed));
                }
            } else {
                iGameRankDialog.showErrorView();
//                iGameRankDialog.showError(context.getString(R.string.data_load_failed));
            }
        }
    };

    public GameRankDialogPresenter(Context context, IGameRankDialog iGameRankDialog, long markId) {
        this.context = context;
        this.iGameRankDialog = iGameRankDialog;
        this.markId = markId;
        iGameRankDialog.showLoadingView();
        getData();
    }

    public void getData() {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(WinAwardApi.class, Constants.URL).getGameRankList(markId, 1, Constants.TEN), observer);
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
