package com.leyou.game.presenter;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.IRegisterActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.TreasureApi;
import com.leyou.game.util.api.UserApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/4/19 下午2:42
 * @Modified Time : 2017/4/19 下午2:42
 */
public class RegisterPresenter {
    private static final String TAG = "RegisterPresenter";
    private Context context;
    private IRegisterActivity iRegisterActivity;
    private Subscription subscribe;
    private Observer<Result<UserData.UserInfo>> observer = new Observer<Result<UserData.UserInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            LogUtil.d(TAG, e.toString());
            iRegisterActivity.showMessageToast(context.getString(R.string.verify_login_failed));
        }

        @Override
        public void onNext(Result<UserData.UserInfo> userInfoResult) {
            if (userInfoResult.result == 1) {
                UserData.UserInfo userInfo = userInfoResult.data;
                UserData.getInstance().saveUserInfo(userInfo);
                iRegisterActivity.showMessageToast(context.getString(R.string.verify_login_success));
                GameApplication.bindAlias(userInfo.id);
                GameApplication.connect(userInfo.token);
                getActivateState();
            } else {
                iRegisterActivity.showMessageToast(context.getString(R.string.verify_login_failed));
            }
        }
    };

    public RegisterPresenter(Context context, IRegisterActivity iRegisterActivity) {
        this.context = context;
        this.iRegisterActivity = iRegisterActivity;
    }

    /**
     * 验证成功后 获取用户信息或者注册新用户
     *
     * @param phoneNum
     */
    public void verifyPhoneNum(String phoneNum) {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).logIn(phoneNum, UserData.getInstance().getDeviceId(), Constants.getClientId()), observer);
    }

    private void activate() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).activate(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    UserData.getInstance().setTreasureActivated(true);
                    SPUtil.setBoolean(context, SPUtil.TREASURE, "isActivate", true);
                    SPUtil.setInt(context, SPUtil.TREASURE, "activateTimes", 1);
                    SPUtil.setBoolean(context, SPUtil.INDUCE, "isGuide", false);
                    iRegisterActivity.toMainActivity();
                } else {
                    UserData.getInstance().setTreasureActivated(false);
                    SPUtil.setBoolean(context, SPUtil.INDUCE, "isGuide", false);
                }
            }
        });
    }

    public void getActivateState() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getTreasureActivateState(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                UserData.getInstance().setTreasureActivated(false);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    UserData.getInstance().setTreasureActivated(true);
                    SPUtil.setBoolean(context, SPUtil.TREASURE, "isActivate", true);
                    SPUtil.setInt(context, SPUtil.TREASURE, "activateTimes", 2);
                    SPUtil.setBoolean(context, SPUtil.INDUCE, "isGuide", true);
                    iRegisterActivity.toMainActivity();
                } else {
                    activate();
                }
            }
        });
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void destroy() {
        if (null != subscribe && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}
