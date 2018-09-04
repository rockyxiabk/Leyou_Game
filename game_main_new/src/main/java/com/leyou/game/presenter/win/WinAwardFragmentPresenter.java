package com.leyou.game.presenter.win;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.ipresenter.win.IWinAwardFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/4/13 上午10:43
 * @Modified Time : 2017/4/13 上午10:43
 */
public class WinAwardFragmentPresenter {

    private Context context;
    private IWinAwardFragment iWinAwardFragment;
    private Subscription subscription;
    Observer<ResultArray<GameBean>> observable = new Observer<ResultArray<GameBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iWinAwardFragment.showError("网络错误");
            iWinAwardFragment.showView(false);
        }

        @Override
        public void onNext(ResultArray<GameBean> resultArray) {
            if (null != resultArray) {
                int result = resultArray.result;
                if (0 != result) {
                    iWinAwardFragment.showView(true);
                    iWinAwardFragment.showGameList(resultArray.data);
                } else {
                    //加载缓存数据
                }
            } else {
                iWinAwardFragment.showView(false);

            }
        }
    };

    public WinAwardFragmentPresenter(Context context, IWinAwardFragment iWinAwardFragment) {
        this.context = context;
        this.iWinAwardFragment = iWinAwardFragment;
        request();
    }

    /**
     * 订阅数据，开始加载网络数据
     */
    public void request() {
        subscription = HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameList(GameBean.TYPE_WIN_PRIZE, 1, 5), observable);
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void unSubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
