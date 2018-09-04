package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.event.SaleEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.DiamondApi;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 出售钻石
 *
 * @author : rocky
 * @Create Time : 2017/11/2 上午11:30
 * @Modified By: rocky
 * @Modified Time : 2017/11/2 上午11:30
 */

public class SaleDiamondActivity extends BaseActivity {
    private static final String TAG = "SaleDiamondActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_sale_total_diamond)
    TextView tvSaleTotalDiamond;
    @BindView(R.id.et_sale_diamond_input)
    EditText etSaleDiamondInput;
    @BindView(R.id.et_sale_total_money)
    EditText etSaleTotalMoney;
    @BindView(R.id.tv_sale_market_price)
    TextView tvSaleMarketPrice;
    @BindView(R.id.tv_sale_money)
    TextView tvSaleMoney;
    @BindView(R.id.btn_sale_diamond)
    Button btnSaleDiamond;
    @BindView(R.id.tv_sell_tips)
    TextView tvSellTips;
    private double minPrice;
    private double maxPrice;
    private int saleDiamondNumber = 0;
    private double saleMoney = 0.0;
    private DiamondBean diamondBean;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_sale_diamond;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        diamondBean = getIntent().getParcelableExtra("diamondBean");
        minPrice = diamondBean.minPrice;
        maxPrice = diamondBean.maxPrice;
        tvSaleTotalDiamond.setText("" + UserData.getInstance().getDiamonds() + "钻石");
        tvSaleMarketPrice.setText("*当前交易价格浮动范围为[" + minPrice + "," + maxPrice + "]");
        etSaleDiamondInput.addTextChangedListener(new TextWatcher() {
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
                            saleDiamondNumber = number * 100;
                        } else {
                            saleDiamondNumber = 100;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        saleDiamondNumber = 100;
                    }
                }
                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSaleTotalMoney.addTextChangedListener(new TextWatcher() {
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
                            saleMoney = number;
                        } else {
                            saleMoney = 0.0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToastShort("请输入正确的钻石单价！");
                        saleMoney = 0.0;
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
        LogUtil.d(TAG, "----unit:" + saleMoney);
        tvSaleMoney.setText("预计收入:" + NumberUtil.getTwoPointNumber(saleDiamondNumber * saleMoney) + "元");
    }

    @Override
    public void initPresenter() {
        HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getExChangeFee(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    String data = result.data.toString();
                    if (!TextUtils.isEmpty(data)) {
                        tvSellTips.setText(data);
                    }
                }
            }
        });
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

    @OnClick({R.id.iv_back, R.id.et_sale_diamond_input, R.id.et_sale_total_money, R.id.btn_sale_diamond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                KeyBoardUtils.closeKeyboard(etSaleDiamondInput, this);
                finishCurrentActivity();
                break;
            case R.id.et_sale_diamond_input:
                KeyBoardUtils.openKeyboard(etSaleDiamondInput, this);
                break;
            case R.id.et_sale_total_money:
                KeyBoardUtils.openKeyboard(etSaleTotalMoney, this);
                break;
            case R.id.btn_sale_diamond:
                commitApply();
                break;
        }
    }

    private void commitApply() {
        KeyBoardUtils.closeKeyboard(etSaleDiamondInput, this);
        if (0 < saleDiamondNumber && saleDiamondNumber <= UserData.getInstance().getDiamonds() && saleMoney <= maxPrice && saleMoney >= minPrice) {
            HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).sale(saleDiamondNumber, saleMoney), new Observer<Result>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.showToastLong("提交失败");
                    finishCurrentActivity();
                }

                @Override
                public void onNext(Result result) {
                    if (result.result == 1) {
                        ToastUtils.showToastLong("提交成功");
                        EventBus.getDefault().post(new SaleEvent(1));
                        finishCurrentActivity();
                    } else {
                        ToastUtils.showToastLong("提交失败");
                    }
                }
            });
        } else {
            ToastUtils.showToastLong("请检查输入的钻石数量和单价是否合理！");
        }
    }
}
