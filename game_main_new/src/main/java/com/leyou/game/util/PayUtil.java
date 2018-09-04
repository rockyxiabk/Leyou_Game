package com.leyou.game.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.leyou.game.Constants;
import com.leyou.game.activity.OrderPayActivity;
import com.leyou.game.bean.pay.PayResult;
import com.leyou.game.bean.pay.PayBean;
import com.leyou.game.event.PayResultCode;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Description : 支付配置
 *
 * @author : rocky
 * @Create Time : 2017/5/15 下午4:52
 * @Modified Time : 2017/5/15 下午4:52
 */
public class PayUtil {
    private static final String TAG = "PayUtil";
    public static final int SOURCE_TO_UP = 1;//充值
    public static final int SOURCE_EXCHANGE_PURCHASE = 2;//交易所收购钻石
    public static final int SOURCE_EXCHANGE_SALE_PURCHASE = 3;//交易所收购别人出售的钻石
    public static final int SOURCE_H5 = 4;//h5页面购买道具支付

    public static final String PAY_TYPE_YUE = "yue";
    public static final String PAY_TYPE_ZFB = "zfb";
    public static final String PAY_TYPE_WX = "wx";
    public static final String PAY_TYPE_ZS = "zs";

    public static void pay(Context context, int source, double money, int diamondNumber, String extraId, String goodsName) {
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("type", source);
        intent.putExtra("money", money);
        intent.putExtra("diamondNumber", diamondNumber);
        intent.putExtra("extraId", extraId);
        intent.putExtra("goodsName", goodsName);
        context.startActivity(intent);
    }

    public static void payH5(Context context, int source, double goodsPrice, int diamondNumber, String threeNo, String gameId, String goodsName) {
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("type", source);
        intent.putExtra("money", goodsPrice);
        intent.putExtra("diamondNumber", diamondNumber);
        intent.putExtra("extraId", gameId);
        intent.putExtra("thirdOrderNo", threeNo);
        intent.putExtra("goodsName", goodsName);
        context.startActivity(intent);
    }

    /**
     * 发布收购信息
     *
     * @param context
     * @param source
     * @param money
     * @param diamondNumber
     * @param goodsName
     */
    public static void purchase(Context context, int source, double money, int diamondNumber, String goodsName) {
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("type", source);
        intent.putExtra("money", money);
        intent.putExtra("diamondNumber", diamondNumber);
        intent.putExtra("goodsName", goodsName);
        context.startActivity(intent);
    }

    public static void aliPay(final Activity activity, final PayBean orderInfo) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        ToastUtils.showToastShort("支付失败");
                        EventBus.getDefault().post(new PayResultCode(2));
                        break;
                    case 1:
                        EventBus.getDefault().post(new PayResultCode(1));
                        break;
                    case 2:
                        ToastUtils.showToastShort("等待支付");
                        EventBus.getDefault().post(new PayResultCode(2));
                        break;
                    case 3:
                        ToastUtils.showToastShort("用户取消");
                        EventBus.getDefault().post(new PayResultCode(2));
                        break;
                    default:
                        ToastUtils.showToastShort("支付失败");
                        EventBus.getDefault().post(new PayResultCode(2));
                        break;
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask payTask = new PayTask(activity);
                String result = payTask.pay(orderInfo.getAliPayInfo(), true);
                if (null != result && result.length() > 0 && !result.equals("")) {
                    LogUtil.d(TAG, "result:" + result);
                    PayResult payResult = new PayResult(result);
                    if (payResult.getResultStatus().equals("9000")) {//支付成功
                        handler.sendEmptyMessage(1);
                    } else if (payResult.getResultStatus().equals("8000")) {//支付等待中，待支付
                        handler.sendEmptyMessage(2);
                    } else if (payResult.getResultStatus().equals("6001")) {//操作取消
                        handler.sendEmptyMessage(3);
                    } else {//其他情况
                        handler.sendEmptyMessage(0);
                    }
                } else {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    public static void wxPay(Activity activity, PayBean payInfo) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, null);
        boolean isInstall = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (isInstall) {
            api.registerApp(Constants.WX_APP_ID);
            PayReq request = new PayReq();
            request.appId = payInfo.wxAppId;
            request.partnerId = payInfo.wxPartnerId;
            request.prepayId = payInfo.wxPrepayId;
            request.nonceStr = payInfo.nonceStr;
            request.timeStamp = payInfo.wxTimeStamp;
            request.packageValue = payInfo.wxPackage;
            request.sign = payInfo.wxSign;

            //吊起微信支付
            boolean sendReq = api.sendReq(request);
            LogUtil.d(TAG, "sendReq():" + sendReq);
        } else {
            ToastUtils.showToastShort("您未安装微信客户端");
        }
    }


    public static void destroy(Context context) {
        WXAPIFactory.createWXAPI(context, null).unregisterApp();
    }
}
