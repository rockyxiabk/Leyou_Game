package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 收购钻石页面
 *
 * @author : rocky
 * @Create Time : 2017/11/2 上午11:37
 * @Modified By: rocky
 * @Modified Time : 2017/11/2 上午11:37
 */
public class PurchaseDiamondActivity extends BaseActivity {


    private static final String TAG = "PurchaseDiamondActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_yue)
    TextView tvYue;
    @BindView(R.id.et_purchase_diamond_input)
    EditText etPurchaseDiamondInput;
    @BindView(R.id.et_purchase_unit_price)
    EditText etPurchaseUnitPrice;
    @BindView(R.id.tv_purchase_market_price)
    TextView tvPurchaseMarketPrice;
    @BindView(R.id.tv_purchase_total_money)
    TextView tvPurchaseTotalMoney;
    @BindView(R.id.btn_purchase_diamond)
    Button btnPurchaseDiamond;
    @BindView(R.id.activity_sale_diamond)
    LinearLayout activitySaleDiamond;
    private double minPrice;
    private double maxPrice;
    private int purchaseDiamondNumber = 0;
    private double purchaseUnitPrice = 0.0;
    private DiamondBean diamondBean;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_purchase_diamond;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        diamondBean = getIntent().getParcelableExtra("diamondBean");
        minPrice = diamondBean.minPrice;
        maxPrice = diamondBean.maxPrice;

        tvPurchaseMarketPrice.setText("*当前交易价格浮动范围为(" + minPrice + "," + maxPrice + "]");
        tvYue.setText("余额:" + UserData.getInstance().getMoney() + "元");
        etPurchaseDiamondInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    int number;
                    try {
                        number = Integer.valueOf(string);
                        if (0 < number) {
                            purchaseDiamondNumber = number * 100;
                        } else {
                            purchaseDiamondNumber = 100;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        purchaseDiamondNumber = 100;
                    }
                }
                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPurchaseUnitPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    double number;
                    try {
                        number = Double.valueOf(string);
                        if (0 <= number) {
                            purchaseUnitPrice = number;
                        } else {
                            purchaseUnitPrice = 0.0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToastShort("请输入正确的钻石单价！");
                        purchaseUnitPrice = 0.0;
                    }
                }
                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        calculate();
    }

    private void calculate() {
        LogUtil.d(TAG, "----unit:" + purchaseUnitPrice);
        tvPurchaseTotalMoney.setText("预付金额:" + NumberUtil.getTwoPointNumber(purchaseDiamondNumber * purchaseUnitPrice) + "元");
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

    @OnClick({R.id.iv_back, R.id.et_purchase_diamond_input, R.id.et_purchase_unit_price, R.id.btn_purchase_diamond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                KeyBoardUtils.closeKeyboard(etPurchaseUnitPrice, this);
                finishCurrentActivity();
                break;
            case R.id.et_purchase_diamond_input:
                KeyBoardUtils.openKeyboard(etPurchaseDiamondInput, this);
                break;
            case R.id.et_purchase_unit_price:
                KeyBoardUtils.openKeyboard(etPurchaseUnitPrice, this);
                break;
            case R.id.btn_purchase_diamond:
                commitApply();
                break;
        }
    }

    private void commitApply() {
        KeyBoardUtils.closeKeyboard(etPurchaseUnitPrice, this);
        if (0 < purchaseDiamondNumber && minPrice < purchaseUnitPrice && purchaseUnitPrice <= maxPrice) {
            double money = purchaseUnitPrice * purchaseDiamondNumber;
            PayUtil.purchase(this, PayUtil.SOURCE_EXCHANGE_PURCHASE, money, purchaseDiamondNumber, getResources().getString(R.string.purchase_diamond));
            finishCurrentActivity();
        } else {
            ToastUtils.showToastLong("请检查输入的钻石数量和单价是否合理！");
        }
    }


}
