package com.leyou.game.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.pay.PayBean;
import com.leyou.game.ipresenter.IOrderPayActivity;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.newapi.PayApi;
import com.leyou.game.util.newapi.UserApi;

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
    private Subscription subscribeCheckOrderState;
    private int checkTimes = 0;
    private String orderNo = "";


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
                iOrderPayActivity.startOrderResultPage(orderNo);
            } else {
                iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
                checkOrderState();
            }
            iOrderPayActivity.dismissedLoading();
        }
    };

    public OrderPayPresenter(Context context, IOrderPayActivity iOrderPayActivity) {
        this.context = context;
        this.iOrderPayActivity = iOrderPayActivity;
    }

    public void payOrder(final String pay_type, double money, String extraId, String threeNo, int type, String goodsName, int diamondNumber) {
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
        HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.PAY_URL).createOrder(extraId, goodsName, Constants.getChannelId(),
                money, diamondNumber, pay_type, type, threeNo, UserData.getInstance().getId()), new Observer<Result<PayBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                iOrderPayActivity.dismissedLoading();
                iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_generator_failed));
            }

            @Override
            public void onNext(Result<PayBean> payBeanResult) {
                if (payBeanResult.result == 1) {
                    switch (pay_type) {
                        case PayUtil.PAY_TYPE_WX:
                            PayBean data = payBeanResult.data;
                            orderNo = data.orderNo;
                            PayUtil.wxPay(((Activity) context), data);
                            break;
                        case PayUtil.PAY_TYPE_ZFB:
                            PayBean data1 = payBeanResult.data;
                            orderNo = data1.orderNo;
                            PayUtil.aliPay(((Activity) context), data1);
                            break;
                        case PayUtil.PAY_TYPE_YUE:
                            iOrderPayActivity.updateWealth();
                            iOrderPayActivity.dismissedLoading();
                            iOrderPayActivity.startOrderResultPage(System.currentTimeMillis() + "");
                            break;
                    }
                } else {
                    iOrderPayActivity.dismissedLoading();
                    iOrderPayActivity.showMessageToast(payBeanResult.msg);
                }
            }
        });
    }

    public void checkOrderState() {
        iOrderPayActivity.changeLoadingDes(context.getString(R.string.order_pay_check_pay_result));
        if (checkTimes <= 3) {
            checkTimes++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    subscribeCheckOrderState = HttpUtil.subscribe(HttpUtil.createApi(PayApi.class, Constants.PAY_URL).checkOrder(orderNo, pay_type), observerCheckOrderState);
                }
            }, 1000);
        } else {
            iOrderPayActivity.showMessageToast(context.getString(R.string.order_pay_failed));
            iOrderPayActivity.dismissedLoading();
        }
    }

    public void updateWealth() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getUserWealth(), new Observer<Result<UserData.UserInfo>>() {
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
                    UserData.getInstance().setDiamonds(data.diamondsNum);
                    UserData.getInstance().setMoney(data.money);
                }
            }
        });
    }

    public void unSubscribe() {
        if (null != subscribeCheckOrderState && subscribeCheckOrderState.isUnsubscribed()) {
            subscribeCheckOrderState.unsubscribe();
        }
    }
}
