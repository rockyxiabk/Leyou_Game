package com.leyou.game.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.PayEvent;
import com.leyou.game.event.PayResultCode;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.IOrderPayActivity;
import com.leyou.game.presenter.OrderPayPresenter;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 下单支付页面
 *
 * @author : rocky
 * @Create Time : 2017/10/26 下午3:27
 * @Modified By: rocky
 * @Modified Time : 2017/10/26 下午3:27
 */

public class OrderPayActivity extends BaseActivity implements IOrderPayActivity {

    @BindView(R.id.iv_order_pay_back)
    ImageView ivOrderPayBack;
    @BindView(R.id.tv_order_pay_money)
    TextView tvOrderPayMoney;
    @BindView(R.id.tv_order_pay_sale_des)
    TextView tvOrderPaySaleDes;
    @BindView(R.id.re_pay_yue)
    RelativeLayout rePayYue;
    @BindView(R.id.tv_yue_money)
    TextView tvPayYueMoney;
    @BindView(R.id.cb_yue)
    CheckBox cbYue;
    @BindView(R.id.cb_alipay)
    CheckBox cbAlipay;
    @BindView(R.id.re_pay_alipay)
    RelativeLayout rePayAlipay;
    @BindView(R.id.cb_wxpay)
    CheckBox cbWxpay;
    @BindView(R.id.re_pay_wx)
    RelativeLayout rePayWx;
    @BindView(R.id.btn_pay)
    Button btnPay;
    private int type;//支付来源
    private double money;//需要支付的金额
    private int diamondNumber;//购买到的钻石
    private String extraId;//交易所正在出售的商品id
    private String pay_type = PayUtil.PAY_TYPE_YUE;//支付类型
    private OrderPayPresenter presenter;
    private LoadingProgressDialog loadingProgressDialog;
    private double yueMoney;//余额
    private double unitPrice;//钻石单价
    private String goodsName;//商品名称
    private String threeNo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResultEventBus(PayResultCode event) {
        int resultCode = event.getResultCode();
        if (resultCode == 1) {//支付成功
            checkOrderState();
        } else if (resultCode == 2) {//支付失败
            EventBus.getDefault().post(new PayEvent(false));
            dismissedLoading();
        } else {
            EventBus.getDefault().post(new PayEvent(false));
            finish();
        }
    }

    @Override
    public void initWindows() {
    }

    @Override
    public int getLayout() {
        return R.layout.activity_order_pay;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        yueMoney = UserData.getInstance().getMoney();
        tvPayYueMoney.setText("账户余额 " + yueMoney + "元");

        Intent intent = getIntent();
        type = intent.getIntExtra("type", PayUtil.SOURCE_TO_UP);
        threeNo = intent.getStringExtra("thirdOrderNo");
        money = intent.getDoubleExtra("money", 0.00);
        diamondNumber = intent.getIntExtra("diamondNumber", 0);
        goodsName = intent.getStringExtra("goodsName");

        tvOrderPayMoney.setText("" + NumberUtil.getTwoPointNumber(money) + "元");
        if (money > yueMoney) {
            cbYue.setChecked(false);
            cbYue.setEnabled(false);
            cbAlipay.setChecked(true);
            pay_type = PayUtil.PAY_TYPE_ZFB;
        }

        extraId = intent.getStringExtra("extraId");
        tvOrderPaySaleDes.setText("" + diamondNumber + "钻石");
        btnPay.setText("确认支付 ¥ " + NumberUtil.getTwoPointNumber(money) + "");
    }

    @Override
    public void initPresenter() {
        presenter = new OrderPayPresenter(this, this);
    }

    @OnClick({R.id.iv_order_pay_back, R.id.cb_yue, R.id.cb_alipay, R.id.re_pay_yue, R.id.re_pay_alipay, R.id.cb_wxpay, R.id.re_pay_wx, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                EventBus.getDefault().post(new PayResultCode(2));
                finishCurrentActivity();
                break;
            case R.id.cb_yue:
            case R.id.re_pay_yue:
                if (money <= yueMoney) {
                    cbWxpay.setChecked(false);
                    cbAlipay.setChecked(false);
                    cbYue.setChecked(true);
                    pay_type = PayUtil.PAY_TYPE_YUE;
                } else {
                    showMessageToast("账户余额不足");
                }
                break;
            case R.id.re_pay_alipay:
            case R.id.cb_alipay:
                cbYue.setChecked(false);
                cbWxpay.setChecked(false);
                cbAlipay.setChecked(true);
                pay_type = PayUtil.PAY_TYPE_ZFB;
                break;
            case R.id.cb_wxpay:
            case R.id.re_pay_wx:
                cbYue.setChecked(false);
                cbAlipay.setChecked(false);
                cbWxpay.setChecked(true);
                pay_type = PayUtil.PAY_TYPE_WX;
                break;
            case R.id.btn_pay:
                presenter.payOrder(pay_type, money, extraId, threeNo, type, goodsName, diamondNumber);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EventBus.getDefault().post(new PayResultCode(2));
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void checkOrderState() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.checkOrderState();
            }
        }, 1000);
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(this, false);
        loadingProgressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != loadingProgressDialog) {
            loadingProgressDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != loadingProgressDialog) {
            loadingProgressDialog.dismiss();
        }
    }

    @Override
    public void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void updateWealth() {
        presenter.updateWealth();
    }

    @Override
    public void startOrderResultPage(String tradeNo) {
        EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, RefreshEvent.MINE));
        EventBus.getDefault().post(new PayEvent(true));

        Intent intent = new Intent(this, OrderResultActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("tradeNo", tradeNo);
        intent.putExtra("pay_type", pay_type);
        intent.putExtra("money", money);
        intent.putExtra("diamondNumber", diamondNumber);
        startActivity(intent);
        finishCurrentActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dismissedLoading();
        presenter.unSubscribe();
    }
}
