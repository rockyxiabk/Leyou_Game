package com.leyou.game.presenter.mine;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.AwardPersonInfo;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.ipresenter.mine.IWinResultFragmentPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.WinAwardApi;

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
    private long markId;
    Observer<ResultArray<AwardPersonInfo>> observer = new Observer<ResultArray<AwardPersonInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iWinResultFragmentPresenter.showError();
        }

        @Override
        public void onNext(ResultArray<AwardPersonInfo> gamePrizeResultPersonInfoResultArray) {
            if (null != gamePrizeResultPersonInfoResultArray) {
                int result = gamePrizeResultPersonInfoResultArray.result;
                if (0 != result) {
                    List<AwardPersonInfo> data = gamePrizeResultPersonInfoResultArray.data;
                    iWinResultFragmentPresenter.showData(data);
                } else {
                    iWinResultFragmentPresenter.showError();
                }
            } else {
                iWinResultFragmentPresenter.showError();
            }
        }
    };
    private Subscription subscribe;

    public WinResultFragmentPresenter(Context context, IWinResultFragmentPresenter iWinResultFragmentPresenter, long markId) {
        this.context = context;
        this.iWinResultFragmentPresenter = iWinResultFragmentPresenter;
        this.markId = markId;
        getWinResultPersonList(markId);
    }

    public void getWinResultPersonList(long markId) {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(WinAwardApi.class, Constants.URL).getPrizeResultPersonList(markId), observer);
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
