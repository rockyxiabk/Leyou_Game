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
import com.leyou.game.util.api.TreasureApi;
import com.leyou.game.util.newapi.SystemApi;
import com.leyou.game.util.newapi.UserApi;

import rx.Observer;

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

    public RegisterPresenter(Context context, IRegisterActivity iRegisterActivity) {
        this.context = context;
        this.iRegisterActivity = iRegisterActivity;
    }

    /**
     * 验证成功后 获取用户信息或者注册新用户
     *
     * @param phoneNumber
     * @param code
     */
    public void verifyPhoneNum(final String phoneNumber, String code) {
        iRegisterActivity.showLoading();
        iRegisterActivity.changeLoadingDes("验证登录中...");
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).logIn(phoneNumber, code, Constants.getClientId()),
                new Observer<Result<UserData.UserInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG, e.toString());
                        e.printStackTrace();
                        iRegisterActivity.showMessageToast(context.getString(R.string.verify_login_failed));
                        iRegisterActivity.dismissedLoading();
                    }

                    @Override
                    public void onNext(Result<UserData.UserInfo> userInfoResult) {
                        iRegisterActivity.dismissedLoading();
                        if (userInfoResult.result == 1) {
                            UserData.UserInfo userInfo = userInfoResult.data;
                            userInfo.phone = phoneNumber;
                            UserData.getInstance().saveUserInfo(userInfo);
                            UserData.getInstance().setLogIn(true);
                            iRegisterActivity.showMessageToast(context.getString(R.string.verify_login_success));
                            GameApplication.connect(userInfo.rongyunToken);

                            iRegisterActivity.toMainActivity();
                        } else {
                            iRegisterActivity.showMessageToast(context.getString(R.string.verify_login_failed));
                        }
                    }
                });
    }

    /**
     * 发送验证码
     *
     * @param phoneNumber
     */
    public void getSmsCode(String phoneNumber) {
        iRegisterActivity.showLoading();
        iRegisterActivity.changeLoadingDes("发送验证码中");
        HttpUtil.subscribe(HttpUtil.createApi(SystemApi.class, Constants.URL).sendSMSCode(phoneNumber), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iRegisterActivity.sendSmsStatus(false);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    iRegisterActivity.sendSmsStatus(true);
                } else {
                    iRegisterActivity.sendSmsStatus(false);
                    iRegisterActivity.showMessageToast(result.msg);
                }
            }
        });
    }

    /**
     * 解除订阅，停止数据请求
     */
    public void destroy() {
    }
}
