package com.leyou.game.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.AliPayOrderInfoBean;
import com.leyou.game.bean.OrderInfoBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WXOrderInfoBean;
import com.leyou.game.ipresenter.IOrderPayActivity;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.PayApi;
import com.leyou.game.util.api.UserApi;

import rx.Observer;
import rx.Subscription;

/**
 * Description : com.leyou.game.presenter
 *
 * @author : rocky
 * @Create Time : 2017/5/16 下午3:38
 * @Modified Time : 2017/5/16 下午3:38
 */
public class OrderPayPresenter {
    private Context context;
    private IOrderPayActivity iOrderPayActivity;
    private String pay_type;
    private Handler handler = new Handler();
    private Subscription subscribe_wx;
    private Subscription subscribe_aliPay;
    private Subscription subscribe_yue;
    private Subscription subscribePayFailed;
    private Subscription subscribeCheckOrderState;
    private WXOrderInfoBean wxOrder;
    private AliPayOrderInfoBean aliPayOrder;
    private int checkTimes = 0;
    private Observer<Result<AliPayOrderInfoBean>> observer_yue = new Observer<Result<AliPayOrderInfoBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iOrderPayActivity.dismissedLoading();
            iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
        }

        @Override
        public void onNext(Result<AliPayOrderInfoBean> aliPayOrderInfoBeanResult) {
            int result = aliPayOrderInfoBeanResult.result;
            if (1 == result) {
                iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_success));
                iOrderPayActivity.updateWealth();
            } else {
                iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
            }
            iOrderPayActivity.dismissedLoading();
        }
    };
    private Observer<Result<AliPayOrderInfoBean>> observer_aliPay = new Observer<Result<AliPayOrderInfoBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iOrderPayActivity.dismissedLoading();
            iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_generator_failed));
        }

        @Override
        public void onNext(Result<AliPayOrderInfoBean> aliPayOrderInfoBeanResult) {
            int result = aliPayOrderInfoBeanResult.result;
            if (1 == result) {
                aliPayOrder = aliPayOrderInfoBeanResult.data;
                if (!TextUtils.isEmpty(aliPayOrder.payInfo) && !TextUtils.isEmpty(aliPayOrder.outTraderNo)) {
                    PayUtil.aliPay(((Activity) context), aliPayOrder);
                } else {
                    iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_generator_failed));
                    iOrderPayActivity.dismissedLoading();
                }
            } else {
                iOrderPayActivity.dismissedLoading();
            }
        }
    };
    private Observer<Result<WXOrderInfoBean>> observer_wx = new Observer<Result<WXOrderInfoBean>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iOrderPayActivity.dismissedLoading();
            iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_generator_failed));
        }

        @Override
        public void onNext(Result<WXOrderInfoBean> wxOrderInfoBeanResult) {
            int result = wxOrderInfoBeanResult.result;
            if (1 == result) {
                wxOrder = wxOrderInfoBeanResult.data;
                PayUtil.wxPay(((Activity) context), wxOrder);
            } else {
                iOrderPayActivity.dismissedLoading();
            }
        }
    };
    private Observer<Result> observerCheckOrderState = new Observer<Result>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iOrderPayActivity.dismissedLoading();
        }

        @Override
        public void onNext(Result result) {
            int code = result.result;
            if (1 == code) {
                iOrderPayActivity.updateWealth();
                iOrderPayActivity.closeCurrentPage();
            } else {
                iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
                checkOrderState();
            }
            iOrderPayActivity.dismissedLoading();
        }
    };
    private Observer<Result> observerPayFailed = new Observer<Result>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
            iOrderPayActivity.dismissedLoading();
        }

        @Override
        public void onNext(Result result) {
            if (result.result == 1) {
                iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
                iOrderPayActivity.dismissedLoading();
            } else {
                iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
                iOrderPayActivity.dismissedLoading();
            }

        }
    };

    public OrderPayPresenter(Context context, IOrderPayActivity iOrderPayActivity) {
        this.context = context;
        this.iOrderPayActivity = iOrderPayActivity;
    }

    public void payOrder(String pay_type, double money, String id, int source, double unitPrice, int diamondNumber) {
        this.pay_type = pay_type;
        iOrderPayActivity.showLoading();
        iOrderPayActivity.changeLoadingDes(context.getString(R.string.order_pay_check_environment));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iOrderPayActivity.changeLoadingDes(context.getString(R.string.order_pay_generator_order));
            }
        }, 1000);
        checkTimes = 0;
        OrderInfoBean orderInfoBean = new OrderInfoBean(id, money, pay_type, source, unitPrice, diamondNumber);
        switch (pay_type) {
            case PayUtil.PAY_TYPE_WX:
                subscribe_wx = HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.URL).generatorOrder_wx(orderInfoBean), observer_wx);
                break;
            case PayUtil.PAY_TYPE_ZFB:
                subscribe_aliPay = HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.URL).generatorOrder_alipay(orderInfoBean), observer_aliPay);
                break;
            case PayUtil.PAY_TYPE_YUE:
                subscribe_yue = HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.URL).generatorOrder_yue(orderInfoBean), observer_yue);
                break;
        }
    }

    public void checkOrderState() {
        iOrderPayActivity.changeLoadingDes(context.getString(R.string.order_pay_check_pay_result));
        String tradeNO = "";
        switch (pay_type) {
            case PayUtil.PAY_TYPE_WX:
                tradeNO = wxOrder.outTraderNo;
                break;
            case PayUtil.PAY_TYPE_ZFB:
                tradeNO = aliPayOrder.outTraderNo;
                break;
        }
        if (checkTimes <= 3) {
            checkTimes++;
            final String finalTradeNO = tradeNO;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    subscribeCheckOrderState = HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.URL).checkOrder(finalTradeNO), observerCheckOrderState);
                }
            }, 1000);
        } else {
            iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
            iOrderPayActivity.dismissedLoading();
        }
    }

    public void notifyFailed() {
        String tradeNO = "";
        switch (pay_type) {
            case PayUtil.PAY_TYPE_WX:
                tradeNO = wxOrder.outTraderNo;
                break;
            case PayUtil.PAY_TYPE_ZFB:
                tradeNO = aliPayOrder.outTraderNo;
                break;
        }
        subscribePayFailed = HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.URL).payFailed(pay_type, tradeNO), observerPayFailed);
    }

    public void updateWealth() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getTreasure(), new Observer<Result<UserData.UserInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result<UserData.UserInfo> userInfoResult) {
                if (1 == userInfoResult.result) {
                    UserData.UserInfo data = userInfoResult.data;
                    UserData.getInstance().setDiamonds(data.virtualCoin);
                    UserData.getInstance().setMoney(data.money);
                    iOrderPayActivity.closeCurrentPage();
                }
            }
        });
    }

    public void unSubscribe() {
        if (null != subscribe_wx && subscribe_wx.isUnsubscribed()) {
            subscribe_wx.unsubscribe();
        }
        if (null != subscribe_aliPay && subscribe_aliPay.isUnsubscribed()) {
            subscribe_aliPay.unsubscribe();
        }
        if (null != subscribe_yue && subscribe_yue.isUnsubscribed()) {
            subscribe_yue.unsubscribe();
        }
        if (null != subscribePayFailed && subscribePayFailed.isUnsubscribed()) {
            subscribePayFailed.unsubscribe();
        }
        if (null != subscribeCheckOrderState && subscribeCheckOrderState.isUnsubscribed()) {
            subscribeCheckOrderState.unsubscribe();
        }
    }
}
