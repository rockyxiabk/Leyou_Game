package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.user.SignBean;
import com.leyou.game.ipresenter.mine.ISignDiamond;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.newapi.UserApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午4:02
 * @Modified Time : 2017/5/10 下午4:02
 */
public class SignDiamondPresenter {
    private static final String TAG = "SignDiamondPresenter";
    private Context context;
    private ISignDiamond iSignDiamond;

    private Subscription subscribe;
    private Subscription subscribeSign;

    public SignDiamondPresenter(Context context, ISignDiamond iSignDiamond) {
        this.context = context;
        this.iSignDiamond = iSignDiamond;
        getSignInfo();
    }

    public void getSignInfo() {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getSignInfo(), new Observer<Result<SignBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iSignDiamond.showSignInfo(null);
            }

            @Override
            public void onNext(Result<SignBean> signBeanResult) {
                int result = signBeanResult.result;
                if (1 == result) {
                    SignBean data = signBeanResult.data;
                    iSignDiamond.showSignInfo(data);
                } else {
                    iSignDiamond.showSignInfo(null);
                }
            }
        });
    }

    public void toSign() {
        subscribeSign = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).sign(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e.toString());
                iSignDiamond.showSignFailed();
            }

            @Override
            public void onNext(Result signBeanResult) {
                int result = signBeanResult.result;
                if (1 == result) {
                    iSignDiamond.showSignSuccess(signBeanResult.data.toString());
                } else {
                    iSignDiamond.showMessage(signBeanResult.msg);
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
        if (null != subscribeSign && subscribeSign.isUnsubscribed()) {
            subscribeSign.unsubscribe();
        }
    }
}
