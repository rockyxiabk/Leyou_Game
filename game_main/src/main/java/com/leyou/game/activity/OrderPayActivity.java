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
import com.leyou.game.bean.PayResultCode;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.PayEvent;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.IOrderPayActivity;
import com.leyou.game.util.PayUtil;
import com.leyou.game.presenter.OrderPayPresenter;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private int source;//支付来源
    private double money;//需要支付的金额
    private int diamondNumber;//购买到的钻石
    private String id;//交易所正在出售的商品id
    private String pay_type = PayUtil.PAY_TYPE_YUE;//支付类型
    private OrderPayPresenter presenter;
    private LoadingProgressDialog loadingProgressDialog;
    private double yueMoney;
    private double unitPrice;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResultEventBus(PayResultCode event) {
        int resultCode = event.getResultCode();
        if (resultCode == 1) {//支付成功
            checkOrderState();
        } else if (resultCode == 2) {//支付失败
            EventBus.getDefault().post(new PayEvent(false));
            presenter.notifyFailed();
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
        source = intent.getIntExtra("source", 0);
        money = intent.getDoubleExtra("money", 0.00);
        diamondNumber = intent.getIntExtra("diamondNumber", 0);

        tvOrderPayMoney.setText(money + "");
        if (money > yueMoney) {
            cbYue.setChecked(false);
            cbAlipay.setChecked(true);
            pay_type = PayUtil.PAY_TYPE_ZFB;
        }

        if (source == PayUtil.SOURCE_EXCHANGE) {
            id = intent.getStringExtra("id");
            tvOrderPaySaleDes.setText(diamondNumber + "钻石");
        } else if (source == PayUtil.SOURCE_EXCHANGE_PURCHASE) {
            unitPrice = intent.getDoubleExtra("unitPrice", 0.0);
            tvOrderPaySaleDes.setText(diamondNumber + "钻石");
        } else {
            tvOrderPaySaleDes.setText(diamondNumber + "钻石");
        }
        btnPay.setText("确认支付 ¥ " + money);

    }

    @Override
    public void initPresenter() {
        presenter = new OrderPayPresenter(this, this);
    }

    @OnClick({R.id.iv_order_pay_back, R.id.cb_yue, R.id.cb_alipay, R.id.re_pay_alipay, R.id.cb_wxpay, R.id.re_pay_wx, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                EventBus.getDefault().post(new PayEvent(false));
                finishCurrentActivity();
                break;
            case R.id.cb_yue:
            case R.id.re_pay_yue:
                cbWxpay.setChecked(false);
                cbAlipay.setChecked(false);
                cbYue.setChecked(true);
                pay_type = PayUtil.PAY_TYPE_YUE;
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
                presenter.payOrder(pay_type, money, id, source, unitPrice, diamondNumber);
                break;
        }
}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EventBus.getDefault().post(new PayEvent(false));
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
    public void closeCurrentPage() {
        EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, RefreshEvent.MINE));
        EventBus.getDefault().post(new PayEvent(true));
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
