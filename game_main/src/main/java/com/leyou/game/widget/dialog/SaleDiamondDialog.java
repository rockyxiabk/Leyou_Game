package com.leyou.game.widget.dialog;

import android.content.Context;
import android.icu.lang.UScript;
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
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.SaleEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.UserApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 出售钻石弹窗
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午5:15
 * @Modified Time : 2017/4/20 下午5:15
 */
public class SaleDiamondDialog extends BaseDialog {
    private final double minPrice;
    private final double maxPrice;
    private Context context;
    @BindView(R.id.iv_sale_diamond_close)
    ImageView ivSaleDiamondClose;
    @BindView(R.id.et_sale_diamond_input)
    EditText etSaleDiamondInput;
    @BindView(R.id.tv_sale_total_diamond)
    TextView tvSaleTotalDiamond;
    @BindView(R.id.tv_sale_market_price)
    TextView tvSaleMarketPrice;
    @BindView(R.id.et_sale_money_input)
    EditText etSaleTotalMoney;
    @BindView(R.id.btn_sale_diamond)
    Button btnSaleDiamond;
    private int saleDiamondNumber = 0;
    private double saleMoney = 0.0;

    public SaleDiamondDialog(Context context, double minPrice, double maxPrice) {
        super(context);
        this.context = context;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_sale_diamond;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvSaleTotalDiamond.setText("当前可用" + UserData.getInstance().getDiamonds() + "个钻石");
        tvSaleMarketPrice.setText("交易价格允许当前是市场价" + NumberUtil.getTwoPointNumber((minPrice + maxPrice) / 2) + "元上下浮动10%范围内");
        etSaleDiamondInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    Integer number;
                    try {
                        number = Integer.valueOf(string);
                        if (0 == number % 500) {
                            saleDiamondNumber = number;
                        } else {
                            saleDiamondNumber = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        saleDiamondNumber = 0;
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
                    Double number;
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
        btnSaleDiamond.setText("确认出售" + " ¥ " + NumberUtil.getTwoPointNumber(saleDiamondNumber * saleMoney));
    }

    @OnClick({R.id.iv_sale_diamond_close, R.id.et_sale_diamond_input, R.id.btn_sale_diamond, R.id.et_sale_money_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sale_diamond_close:
                KeyBoardUtils.closeKeyboard(etSaleDiamondInput, context);
                dismiss();
                break;
            case R.id.et_sale_diamond_input:
                KeyBoardUtils.openKeyboard(etSaleDiamondInput, context);
                break;
            case R.id.et_sale_money_input:
                KeyBoardUtils.openKeyboard(etSaleTotalMoney, context);
                break;
            case R.id.btn_sale_diamond:
                commitApply();
                break;
        }
    }

    private void commitApply() {
        KeyBoardUtils.closeKeyboard(etSaleDiamondInput, context);
        if (0 < saleDiamondNumber && saleDiamondNumber <= UserData.getInstance().getDiamonds() && saleMoney <= maxPrice && saleMoney >= minPrice) {
            HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).sale(saleMoney, saleDiamondNumber), new Observer<Result>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtils.showToastLong("提交失败");
                    dismiss();
                }

                @Override
                public void onNext(Result result) {
                    if (result.result == 1) {
                        ToastUtils.showToastLong("提交成功");
                        EventBus.getDefault().post(new SaleEvent(1));
                        dismiss();
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
