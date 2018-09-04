package com.leyou.game.presenter.win;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.bean.GameExtBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.win.IPlayGameActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.api.PayApi;
import com.leyou.game.util.api.UserApi;
import com.leyou.game.util.api.WinAwardApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/25 下午2:20
 * @Modified Time : 2017/5/25 下午2:20
 */
public class PlayGameActivityPresenter {
    private Context context;
    private IPlayGameActivity iPlayGameActivity;
    private Subscription subscribe;
    private Subscription subscribePayOrder;
    private Subscription subscribeUserWealth;
    private Observer<Result<GameExtBean>> observerGameTime = new Observer<Result<GameExtBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iPlayGameActivity.showGameView();
        }

        @Override
        public void onNext(Result<GameExtBean> gameExtBeanResult) {
            int result = gameExtBeanResult.result;
            if (result == 1) {
                GameExtBean data = gameExtBeanResult.data;
                int count = data.count;
                if (count < Constants.GAME_FREE_TIMES) {
                    iPlayGameActivity.loadUrl(data.gameCountId);
                    iPlayGameActivity.showGameView();
                } else {
                    iPlayGameActivity.loadUrl(data.gameCountId);
                    iPlayGameActivity.showGameView();
                }
            } else {
                iPlayGameActivity.showGameView();
            }

        }
    };
    private Observer<Result> observerPayOrder = new Observer<Result>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iPlayGameActivity.sendOrderState(false);
        }

        @Override
        public void onNext(Result result) {
            int result1 = result.result;
            LogUtil.d("pay state", "-----pay:state:" + result.result);
            if (result1 == 1) {
                iPlayGameActivity.sendOrderState(true);
            } else {
                iPlayGameActivity.sendOrderState(false);
            }
        }
    };

    private Observer<Result<UserData.UserInfo>> userWealthObserver = new Observer<Result<UserData.UserInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iPlayGameActivity.reStartPay();
        }

        @Override
        public void onNext(Result<UserData.UserInfo> userInfoResult) {
            if (1 == userInfoResult.result) {
                UserData.UserInfo data = userInfoResult.data;
                UserData.getInstance().setDiamonds(data.virtualCoin);
                UserData.getInstance().setMoney(data.money);
                iPlayGameActivity.reStartPay();
            }
        }
    };

    public PlayGameActivityPresenter(Context context, IPlayGameActivity iPlayGameActivity) {
        this.context = context;
        this.iPlayGameActivity = iPlayGameActivity;
    }

    public void getGameTime(long mark) {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(WinAwardApi.class, Constants.URL).getGameTime(mark), observerGameTime);
    }

    public void payVirtualCoin(String gameId) {
        subscribePayOrder = HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.URL).payWinAwardGame(gameId), observerPayOrder);
    }

    public void getUserWealth() {
        if (!TextUtils.isEmpty(UserData.getInstance().getId())) {
            subscribeUserWealth = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getTreasure(), userWealthObserver);
        } else {
            iPlayGameActivity.dismissedLoading();
        }
    }

    public void destroy() {
        if (null != subscribe && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        if (null != subscribePayOrder && subscribePayOrder.isUnsubscribed()) {
            subscribePayOrder.unsubscribe();
        }
        if (subscribeUserWealth != null && !subscribeUserWealth.isUnsubscribed()) {
            subscribeUserWealth.unsubscribe();
        }
    }
}
