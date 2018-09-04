package com.leyou.game.widget.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 收购钻石弹窗
 *
 * @author : rocky
 * @Create Time : 2017/6/21 下午3:15
 * @Modified Time : 2017/6/21 下午3:15
 */
public class PurchaseDialog extends BaseDialog {
    private final double minPrice;
    private final double maxPrice;
    private Context context;
    @BindView(R.id.iv_purchase_diamond_close)
    ImageView ivPurchaseDiamondClose;
    @BindView(R.id.et_purchase_diamond_num_input)
    EditText etPurchaseDiamondNumInput;
    @BindView(R.id.et_purchase_unit_price_input)
    EditText etPurchaseUnitPriceInput;
    @BindView(R.id.tv_sell_des)
    TextView tvSellDes;
    @BindView(R.id.btn_purchase_diamond)
    Button btnPurchaseDiamond;
    private int purchaseDiamondNumber = 0;
    private double purchaseUnitPrice = 0.0;

    public PurchaseDialog(Context context, double minPrice, double maxPrice) {
        super(context);
        this.context = context;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_purchase_diamond;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        tvSellDes.setText("交易价格允许当前是市场价" + NumberUtil.getTwoPointNumber((minPrice + maxPrice) / 2) + "元上下浮动10%范围内");

        etPurchaseDiamondNumInput.addTextChangedListener(new TextWatcher() {
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
                            purchaseDiamondNumber = number;
                        } else {
                            purchaseDiamondNumber = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        purchaseDiamondNumber = 0;
                    }
                }
                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPurchaseUnitPriceInput.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.iv_purchase_diamond_close, R.id.et_purchase_diamond_num_input, R.id.et_purchase_unit_price_input, R.id.btn_purchase_diamond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_purchase_diamond_close:
                KeyBoardUtils.closeKeyboard(etPurchaseDiamondNumInput, context);
                dismiss();
                break;
            case R.id.et_purchase_diamond_num_input:
                KeyBoardUtils.openKeyboard(etPurchaseDiamondNumInput, context);
                break;
            case R.id.et_purchase_unit_price_input:
                KeyBoardUtils.openKeyboard(etPurchaseUnitPriceInput, context);
                break;
            case R.id.btn_purchase_diamond:
                commitApply();
                break;
        }
    }

    private void calculate() {
        btnPurchaseDiamond.setText("确认收购 ¥ " + NumberUtil.getTwoPointNumber(purchaseDiamondNumber * purchaseUnitPrice)+" ");
    }

    private void commitApply() {
        KeyBoardUtils.closeKeyboard(etPurchaseUnitPriceInput, context);
        if (0 < purchaseDiamondNumber && minPrice <= purchaseUnitPrice&&purchaseUnitPrice<=maxPrice) {
            double money = purchaseUnitPrice * purchaseDiamondNumber;
            PayUtil.purchase(context, PayUtil.SOURCE_EXCHANGE_PURCHASE, money, purchaseDiamondNumber, purchaseUnitPrice);
            dismiss();
        } else {
            ToastUtils.showToastLong("请检查输入的钻石数量和单价是否合理！");
        }
    }
}
