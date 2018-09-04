package com.leyou.game.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.activity.mine.UptoActivity;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.util.PayUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 订单支付结果页面
 *
 * @author : rocky
 * @Create Time : 2017/11/15 下午1:47
 * @Modified By: rocky
 * @Modified Time : 2017/11/15 下午1:47
 */
public class OrderResultActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_order_result)
    ImageView ivOrderResult;
    @BindView(R.id.tv_order_diamond_change)
    TextView tvOrderDiamondChange;
    @BindView(R.id.tv_order_pay_money_change)
    TextView tvOrderPayMoneyChange;
    @BindView(R.id.tv_order_pay_tradeNo)
    TextView tvOrderPayTradeNo;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_upto)
    Button btnUpto;
    private String pay_type;
    private int type;
    private String tradeNo;
    private double money;
    private int diamondNumber;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_order_result;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", PayUtil.SOURCE_TO_UP);
        pay_type = intent.getStringExtra("pay_type");
        tradeNo = intent.getStringExtra("tradeNo");
        money = intent.getDoubleExtra("money", 0.0);
        diamondNumber = intent.getIntExtra("diamondNumber", 0);

        switch (type) {
            case PayUtil.SOURCE_EXCHANGE_PURCHASE://收购钻石，显示支预支付成功，钻石待交易
                tvOrderDiamondChange.setText("预计收入:" + diamondNumber + "钻石");
                btnBack.setText("返回交易所");
                btnUpto.setVisibility(View.GONE);
                break;
            case PayUtil.SOURCE_EXCHANGE_SALE_PURCHASE: //购买钻石，钻石增加，余额减少，或者支付宝微信支付成功
                tvOrderDiamondChange.setText("+" + diamondNumber + "钻石");
                btnBack.setText("继续购买");
                btnUpto.setVisibility(View.GONE);
                break;
            case PayUtil.SOURCE_TO_UP://充值，钻石增加
                tvOrderDiamondChange.setText("+" + diamondNumber + "钻石");
                btnBack.setText("继续充值");
                btnUpto.setVisibility(View.GONE);
                break;
            case PayUtil.SOURCE_H5://支付金额，不需要展示钻石，显示微信或者支付宝支付成功
                tvOrderDiamondChange.setVisibility(View.GONE);
                btnBack.setText("返回游戏");
                btnUpto.setText("钻石充值");
                break;
        }

        switch (pay_type) {
            case PayUtil.PAY_TYPE_YUE:
            case PayUtil.PAY_TYPE_WX:
            case PayUtil.PAY_TYPE_ZFB:
                tvOrderPayMoneyChange.setText("-" + NumberUtil.getTwoPointNumber(money) + "元");
                break;
        }
        tvOrderPayTradeNo.setText("订单编号:" + tradeNo + "");
    }

    @Override
    public void initPresenter() {

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

    @OnClick({R.id.iv_back, R.id.btn_back, R.id.btn_upto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.btn_back:
                switch (type) {
                    case PayUtil.SOURCE_EXCHANGE_PURCHASE://收购钻石，显示支预支付成功，钻石待交易
                        finishCurrentActivity();
                        break;
                    case PayUtil.SOURCE_EXCHANGE_SALE_PURCHASE: //购买钻石，钻石增加，余额减少，或者支付宝微信支付成功
                        finishCurrentActivity();
                        break;
                    case PayUtil.SOURCE_TO_UP://充值，钻石增加
                        startActivity(new Intent(this, UptoActivity.class));
                        finishCurrentActivity();
                        break;
                    case PayUtil.SOURCE_H5://支付金额，不需要展示钻石，显示微信或者支付宝支付成功
                        finishCurrentActivity();
                        break;
                }
                break;
            case R.id.btn_upto:
                switch (type) {
                    case PayUtil.SOURCE_EXCHANGE_PURCHASE://收购钻石，显示支预支付成功，钻石待交易
                        break;
                    case PayUtil.SOURCE_EXCHANGE_SALE_PURCHASE: //购买钻石，钻石增加，余额减少，或者支付宝微信支付成功
                        break;
                    case PayUtil.SOURCE_TO_UP://充值，钻石增加
                        break;
                    case PayUtil.SOURCE_H5://支付金额，不需要展示钻石，显示微信或者支付宝支付成功
                        startActivity(new Intent(this, UptoActivity.class));
                        finishCurrentActivity();
                        break;
                }
                break;
        }
    }
}
