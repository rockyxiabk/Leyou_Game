package com.leyou.game.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.leyou.game.Constants;
import com.leyou.game.activity.OrderPayActivity;
import com.leyou.game.bean.AliPayOrderInfoBean;
import com.leyou.game.bean.PayResult;
import com.leyou.game.bean.PayResultCode;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WXOrderInfoBean;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.MD5;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.PayApi;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import rx.Observer;

/**
 * Description : 支付配置
 *
 * @author : rocky
 * @Create Time : 2017/5/15 下午4:52
 * @Modified Time : 2017/5/15 下午4:52
 */
public class PayUtil {
    private static final String TAG = "PayUtil";
    public static final int SOURCE_TOP_UP = 0;//充值
    public static final int SOURCE_GAME = 1;//游戏消耗金币
    public static final int SOURCE_EXCHANGE = 3;//交易所
    public static final int SOURCE_EXCHANGE_PURCHASE = 6;//交易所

    public static final String PAY_TYPE_YUE = "yue";
    public static final String PAY_TYPE_ZFB = "zfb";
    public static final String PAY_TYPE_WX = "wx";

    public static void pay(Context context, int source, double money, int diamondNumber, String id) {
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("source", source);
        intent.putExtra("money", money);
        intent.putExtra("diamondNumber", diamondNumber);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    public static void purchase(Context context, int source, double money, int diamondNumber, double unitPrice) {
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra("source", source);
        intent.putExtra("money", money);
        intent.putExtra("diamondNumber", diamondNumber);
        intent.putExtra("unitPrice", unitPrice);
        context.startActivity(intent);
    }

    public static void aliPay(final Activity activity, final AliPayOrderInfoBean orderInfo) {

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
                String result = payTask.pay(orderInfo.payInfo, true);
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

    public static void wxPay(Activity activity, WXOrderInfoBean wxOrderInfo) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, null);
        boolean isInstall = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (isInstall) {
            api.registerApp(Constants.WX_APP_ID);
            PayReq request = new PayReq();
            WXOrderInfoBean.PayInfo payInfo = wxOrderInfo.payInfo;
            request.appId = payInfo.appid;
            request.partnerId = payInfo.partnerid;
            request.prepayId = payInfo.prepayid;
            request.nonceStr = payInfo.noncestr;
            request.timeStamp = payInfo.timestamp;
            request.packageValue = payInfo.packageValue;
            request.sign = payInfo.sign;

            //对sign 二次赋值
            List<String> signParams = new LinkedList<>();
            signParams.add("appid=" + payInfo.appid);
            signParams.add("&noncestr=" + payInfo.noncestr);
            signParams.add("&package=" + payInfo.packageValue);
            signParams.add("&partnerid=" + payInfo.partnerid);
            signParams.add("&prepayid=" + payInfo.prepayid);
            signParams.add("&timestamp=" + payInfo.timestamp);
            request.sign = genAppSign(signParams);
            //吊起微信支付
            boolean sendReq = api.sendReq(request);

            LogUtil.d(TAG, "sendReq():" + sendReq);
        } else {
            ToastUtils.showToastShort("您未安装微信客户端");
        }

    }

    private static String genAppSign(List<String> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i));
        }
        sb.append("&key=123abcdefg456abcdefg12qwertyuio1");

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign;
    }


    public static void destroy(Context context) {
        WXAPIFactory.createWXAPI(context, null).unregisterApp();
    }
}
