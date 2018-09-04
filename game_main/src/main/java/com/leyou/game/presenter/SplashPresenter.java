package com.leyou.game.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.R;
import com.leyou.game.bean.AdBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.ISplashActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.AppApi;
import com.leyou.game.util.api.TreasureApi;
import com.leyou.game.util.api.UserApi;

import org.w3c.dom.Text;

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
    private Observer<Result<String>> observer = new Observer<Result<String>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iSplashActivity.setLogInState(false);
            iSplashActivity.startLogoIn();
        }

        @Override
        public void onNext(Result<String> stringResult) {
            if (null != stringResult) {
                int result = stringResult.result;
                if (result == 1) {
                    UserData.getInstance().setLogIn(true);
                    iSplashActivity.showMessageToast(context.getString(R.string.login_verify_success));
                    GameApplication.bindAlias(UserData.getInstance().getId());
                    GameApplication.connect(UserData.getInstance().getToken());
                    getActivateState();
                } else {
                    UserData.getInstance().setLogIn(false);
                    iSplashActivity.showMessageToast(context.getString(R.string.login_verify_failed));
                    iSplashActivity.setLogInState(false);
                }
            } else {
                UserData.getInstance().setLogIn(false);
                iSplashActivity.showMessageToast(context.getString(R.string.login_verify_failed));
                iSplashActivity.setLogInState(false);
            }
        }
    };

    public SplashPresenter(Context context, ISplashActivity iSplashActivity) {
        this.context = context;
        this.iSplashActivity = iSplashActivity;
    }

    public void verifyLogoIn() {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).verifyLogIn(UserData.getInstance().getPhoneNum(), UserData.getInstance().getDeviceId()), observer);
    }

    public void getAd() {
        HttpUtil.subscribe(HttpUtil.createApi(AppApi.class, Constants.URL).getAd(), new Observer<Result<AdBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                verifyLogoIn();
            }

            @Override
            public void onNext(Result<AdBean> adBeanResult) {
                if (adBeanResult.result == 1) {
                    AdBean data = adBeanResult.data;
                    if (null != data) {
                        iSplashActivity.showAdView(data);
                    } else {
                        verifyLogoIn();
                    }
                } else {
                    verifyLogoIn();
                }
            }
        });
    }

    private void getActivateState() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getTreasureActivateState(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort(context.getString(R.string.net_error));
                iSplashActivity.setLogInState(false);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    UserData.getInstance().setTreasureActivated(true);
                    iSplashActivity.setLogInState(true);
                } else {
                    UserData.getInstance().setTreasureActivated(false);
                    ToastUtils.showToastShort(context.getString(R.string.activate_failed));
                    iSplashActivity.setLogInState(false);
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
