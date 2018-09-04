package com.leyou.game.presenter.mine;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.user.BankCardBean;
import com.leyou.game.bean.user.UserExtBean;
import com.leyou.game.ipresenter.mine.IMineFragment;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.GameApi;
import com.leyou.game.util.newapi.UserApi;

import java.util.List;

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

    private static final String TAG = "MineFragmentPresenter";
    private Context context;
    private IMineFragment iMineFragment;
    private Subscription subscribeUserInfo;
    private Subscription subscribeUserWealth;

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
                UserData.getInstance().saveUserNickNameAndPicture(data.getNickname(), data.getHeadImgUrl(), data.getIdNo());
                iMineFragment.showUserData(data.nickname, data.headImgUrl, null);
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
                UserData.getInstance().setDiamonds(data.diamondsNum);
                UserData.getInstance().setMoney(data.money);
                iMineFragment.showWealth(data.diamondsNum, data.money);
            } else {
                iMineFragment.showWealth(0, 0.0);
            }
        }
    };

    public MineFragmentPresenter(Context context, IMineFragment iMineFragment) {
        this.context = context;
        this.iMineFragment = iMineFragment;
        getUserDate();
        getUserWealth();
        getMyPlayedGame();
        getPrizeDy();
    }

    public void getUserDate() {
        subscribeUserInfo = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getUserInfo(), userInfoObserver);
    }

    public void getUserWealth() {
        subscribeUserWealth = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getUserWealth(), userWealthObserver);
    }

    public void getBankInfo() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getBindCard(), new Observer<Result<BankCardBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<BankCardBean> bankCardBeanResult) {
                if (bankCardBeanResult.result == 1) {
                    BankCardBean data = bankCardBeanResult.data;
                    UserData.getInstance().setBankInfo(data);
                }
            }
        });
    }

    public void getPrizeDy() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getUserUnReadNum(), new Observer<Result<UserExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iMineFragment.setPrizeDynamic(0);
            }

            @Override
            public void onNext(Result<UserExtBean> userExtBeanResult) {
                if (userExtBeanResult.result == 1) {
                    iMineFragment.setPrizeDynamic(userExtBeanResult.data.unreadNum);
                } else {
                    iMineFragment.setPrizeDynamic(0);
                }
            }
        });
    }

    public void getMyPlayedGame() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getMyPlayedGames(), new Observer<ResultArray<GameBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.fillInStackTrace());
                iMineFragment.setMyPlayedList(null);
            }

            @Override
            public void onNext(ResultArray<GameBean> gameTabBeanResultArray) {
                if (gameTabBeanResultArray.result == 1) {
                    List<GameBean> data = gameTabBeanResultArray.data;
                    iMineFragment.setMyPlayedList(data);
                } else {
                    iMineFragment.setMyPlayedList(null);
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
    }
}
