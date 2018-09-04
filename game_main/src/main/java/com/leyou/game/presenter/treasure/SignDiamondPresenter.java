package com.leyou.game.presenter.treasure;

import android.content.Context;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.SignBean;
import com.leyou.game.ipresenter.mine.ISignDiamond;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.api.UserApi;

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
    private Context context;
    private ISignDiamond iSignDiamond;
    private Observer<Result<SignBean>> observer = new Observer<Result<SignBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iSignDiamond.showDayAndDiamondNum(0, 0);
        }

        @Override
        public void onNext(Result<SignBean> signBeanResult) {
            int result = signBeanResult.result;
            if (1 == result) {
                SignBean data = signBeanResult.data;
                iSignDiamond.showDayAndDiamondNum(data.continuousSign, data.signVirtualCoinCount);
            } else {
                iSignDiamond.showDayAndDiamondNum(0, 0);
            }
        }
    };
    private Observer<Result<SignBean>> observerSign = new Observer<Result<SignBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iSignDiamond.showSignFailed();
        }

        @Override
        public void onNext(Result<SignBean> signBeanResult) {
            int result = signBeanResult.result;
            if (1 == result) {
                SignBean data = signBeanResult.data;
                int virtualCoin = data.virtualCoin;
                iSignDiamond.showSignSuccess(virtualCoin);
                SPUtil.setString(context, SPUtil.SETTING, "lastStampTime", System.currentTimeMillis() + "");
            } else {
                SPUtil.setString(context, SPUtil.SETTING, "lastStampTime", System.currentTimeMillis() + "");
                iSignDiamond.showMessage(context.getString(R.string.signed));
            }
        }
    };

    private Subscription subscribe;
    private Subscription subscribeSign;

    public SignDiamondPresenter(Context context, ISignDiamond iSignDiamond) {
        this.context = context;
        this.iSignDiamond = iSignDiamond;
        getSignDiamond();
    }

    public void getSignDiamond() {
        subscribe = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getSignDiamond(), observer);
    }

    public void toSign() {
        subscribeSign = HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).toSign(), observerSign);
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
