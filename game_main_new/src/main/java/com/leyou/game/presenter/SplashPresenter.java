package com.leyou.game.presenter;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.R;
import com.leyou.game.bean.AdBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.ISplashActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.SystemApi;
import com.leyou.game.util.newapi.UserApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/4/19 下午7:27
 * @Modified Time : 2017/4/19 下午7:27
 */
public class SplashPresenter {
    private Context context;
    private ISplashActivity iSplashActivity;
    private Subscription subscribe;
    private Observer<Result<UserData.UserInfo>> observer = new Observer<Result<UserData.UserInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iSplashActivity.setLogInState(false);
        }

        @Override
        public void onNext(Result<UserData.UserInfo> userResult) {
            int result = userResult.result;
            if (result == 1) {
                String phone = UserData.getInstance().getPhoneNum();
                UserData.UserInfo userInfo = userResult.data;
                userInfo.phone = phone;
                UserData.getInstance().saveUserInfo(userInfo);
                UserData.getInstance().setLogIn(true);
                GameApplication.connect(UserData.getInstance().getRongToken());
                iSplashActivity.setLogInState(true);
            } else {
                UserData.getInstance().setLogIn(false);
                iSplashActivity.setLogInState(false);
            }
        }
    };

    public SplashPresenter(Context context, ISplashActivity iSplashActivity) {
        this.context = context;
        this.iSplashActivity = iSplashActivity;
    }

    public void verifyLogoIn() {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).autoLogIn(), observer);
    }

    public void getAd() {
        HttpUtil.subscribe(HttpUtil.createApi(SystemApi.class, Constants.URL).getAd(), new Observer<Result<AdBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iSplashActivity.showAdView(null);
                verifyLogoIn();
            }

            @Override
            public void onNext(Result<AdBean> adBeanResult) {
                if (adBeanResult.result == 1) {
                    AdBean data = adBeanResult.data;
                    if (null != data) {
                        iSplashActivity.showAdView(data);
                    } else {
                        iSplashActivity.showAdView(null);
                        verifyLogoIn();
                    }
                } else {
                    iSplashActivity.showAdView(null);
                    verifyLogoIn();
                }
            }
        });
    }

    public void destroy() {
        if (null != subscribe && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
