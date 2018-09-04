package com.leyou.game.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.leyou.game.Constants;
import com.leyou.game.bean.PayResultCode;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Description : 微信支付
 *
 * @author : rocky
 * @Create Time : 2017/7/13 下午3:32
 * @Modified By: rocky
 * @Modified Time : 2017/7/13 下午3:32
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        boolean handleIntent = api.handleIntent(getIntent(), this);
        LogUtil.d(TAG, "oncreate handleIntent:" + handleIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        boolean handleIntent = api.handleIntent(intent, this);
        LogUtil.d(TAG, "onNewIntent handleIntent:" + handleIntent);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(final BaseResp resp) {
        String errStr = resp.errStr;
        LogUtil.d(TAG, "onPayFinish, errCode = " + resp.errCode + "------errorStr:" + errStr);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                EventBus.getDefault().post(new PayResultCode(1));
                break;
            case BaseResp.ErrCode.ERR_COMM:
                EventBus.getDefault().post(new PayResultCode(2));
                ToastUtils.showToastShort("支付失败");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                EventBus.getDefault().post(new PayResultCode(2));
                ToastUtils.showToastShort("操作取消");
                break;
            default:
                EventBus.getDefault().post(new PayResultCode(2));
                ToastUtils.showToastShort("未知错误");
                break;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
