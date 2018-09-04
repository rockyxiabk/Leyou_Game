package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.ipresenter.mine.IWinResultFragmentPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Description : 上期赢大奖结果获奖名单逻辑处理层
 *
 * @author : rocky
 * @Create Time : 2017/4/24 下午8:29
 * @Modified Time : 2017/4/24 下午8:29
 */
public class WinResultFragmentPresenter {
    private Context context;
    private IWinResultFragmentPresenter iWinResultFragmentPresenter;
    private String markId;
    private Subscription subscribe;
    Observer<ResultArray<GameWinPriseBean>> observer = new Observer<ResultArray<GameWinPriseBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iWinResultFragmentPresenter.showError();
        }

        @Override
        public void onNext(ResultArray<GameWinPriseBean> gamePrizeResultPersonInfoResultArray) {
            if (null != gamePrizeResultPersonInfoResultArray) {
                int result = gamePrizeResultPersonInfoResultArray.result;
                if (0 != result) {
                    List<GameWinPriseBean> data = gamePrizeResultPersonInfoResultArray.data;
                    iWinResultFragmentPresenter.showData(data);
                } else {
                    iWinResultFragmentPresenter.showError();
                }
            } else {
                iWinResultFragmentPresenter.showError();
            }
        }
    };

    public WinResultFragmentPresenter(Context context, IWinResultFragmentPresenter iWinResultFragmentPresenter, String markId) {
        this.context = context;
        this.iWinResultFragmentPresenter = iWinResultFragmentPresenter;
        this.markId = markId;
        getWinResultPersonList(markId);
    }

    public void getWinResultPersonList(String markId) {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getLatestWinnerList(markId), observer);
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
