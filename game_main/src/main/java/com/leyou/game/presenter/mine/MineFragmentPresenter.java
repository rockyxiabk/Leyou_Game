package com.leyou.game.presenter.mine;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.bean.BankCardInfoBean;
import com.leyou.game.bean.DeveloperStateBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.mine.IMineFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.UserApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/4/27 下午3:48
 * @Modified Time : 2017/4/27 下午3:48
 */
public class MineFragmentPresenter {

    private Context context;
    private IMineFragment iMineFragment;
    private Observer<Result<UserData.UserInfo>> userInfoObserver = new Observer<Result<UserData.UserInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iMineFragment.showUserData(UserData.getInstance().getNickname(), UserData.getInstance().getPictureUrl(), null);
            iMineFragment.isLogIn(UserData.getInstance().isLogIn());
        }

        @Override
        public void onNext(Result<UserData.UserInfo> userInfoResult) {
            if (1 == userInfoResult.result) {
                UserData.UserInfo data = userInfoResult.data;
                UserData.getInstance().saveUserNickNameAndPicture(data.getNickname(), data.getPictureUrl());
                iMineFragment.showUserData(data.nickname, data.pictureUrl, null);
                iMineFragment.isLogIn(UserData.getInstance().isLogIn());
            } else {
                iMineFragment.showUserData(UserData.getInstance().getNickname(), UserData.getInstance().getPictureUrl(), null);
                iMineFragment.isLogIn(UserData.getInstance().isLogIn());
            }
        }
    };
    private Observer<Result<UserData.UserInfo>> userWealthObserver = new Observer<Result<UserData.UserInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iMineFragment.showWealth(0, 0.0);
        }

        @Override
        public void onNext(Result<UserData.UserInfo> userInfoResult) {
            if (1 == userInfoResult.result) {
                UserData.UserInfo data = userInfoResult.data;
                UserData.getInstance().setDiamonds(data.virtualCoin);
                UserData.getInstance().setMoney(data.money);
                iMineFragment.showWealth(data.virtualCoin, data.money);
            } else {
                iMineFragment.showWealth(0, 0.0);
            }
        }
    };
    private Observer<Result<DeveloperStateBean>> observerState = new Observer<Result<DeveloperStateBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iMineFragment.isDeveloper(false, 0);
        }

        @Override
        public void onNext(Result<DeveloperStateBean> developerStateBeanResult) {
            if (1 == developerStateBeanResult.result) {
                int status = developerStateBeanResult.data.status;
                if (status == 0) {
                    iMineFragment.isDeveloper(false, 0);
                } else {
                    iMineFragment.isDeveloper(true, status);
                }
            } else {
                iMineFragment.isDeveloper(false, 0);
            }
        }
    };
    private Subscription subscribeUserInfo;
    private Subscription subscribeUserWealth;
    private Subscription subscribeState;

    public MineFragmentPresenter(Context context, IMineFragment iMineFragment) {
        this.context = context;
        this.iMineFragment = iMineFragment;
        getUserDate();
        getUserWealth();
        getDeveloperState();
        getBankInfo();
    }

    public void getUserDate() {
        if (!TextUtils.isEmpty(UserData.getInstance().getId())) {
            subscribeUserInfo = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getUserInfo(), userInfoObserver);
            getPrizeDy();
        } else {
            iMineFragment.showUserData("点击登录", "null", "");
            iMineFragment.isLogIn(UserData.getInstance().isLogIn());
        }
    }

    public void getUserWealth() {
        if (!TextUtils.isEmpty(UserData.getInstance().getId())) {
            subscribeUserWealth = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getTreasure(), userWealthObserver);
        } else {
            iMineFragment.showWealth(0, 0.0);
        }
    }

    public void getDeveloperState() {
        if (!TextUtils.isEmpty(UserData.getInstance().getId())) {
            subscribeState = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getDeveloperState(), observerState);
        } else {
            iMineFragment.isDeveloper(false, 0);
        }
    }

    public void getBankInfo() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getBindCardInfo(), new Observer<Result<BankCardInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMineFragment.isBindCard(false);
            }

            @Override
            public void onNext(Result<BankCardInfoBean> bankCardInfoBeanResult) {
                if (bankCardInfoBeanResult.result == 1) {
                    iMineFragment.isBindCard(true);
                } else {
                    iMineFragment.isBindCard(false);
                }
            }
        });
    }

    private void getPrizeDy() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getPrizeDynamic(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMineFragment.setPrizeDynamic(false);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iMineFragment.setPrizeDynamic(true);
                } else {
                    iMineFragment.setPrizeDynamic(false);
                }
            }
        });
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void unSubscribe() {
        if (subscribeUserWealth != null && !subscribeUserWealth.isUnsubscribed()) {
            subscribeUserWealth.unsubscribe();
        }
        if (subscribeUserInfo != null && !subscribeUserInfo.isUnsubscribed()) {
            subscribeUserInfo.unsubscribe();
        }
        if (subscribeState != null && !subscribeState.isUnsubscribed()) {
            subscribeState.unsubscribe();
        }
    }

}
