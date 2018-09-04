package com.leyou.game.widget.dialog;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.event.PayEvent;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.umeng.socialize.utils.Log;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 充值弹窗
 *
 * @author : rocky
 * @Create Time : 2017/4/20 上午9:40
 * @Modified Time : 2017/4/20 上午9:40
 */
public class BuyDiamondDialog extends BaseDialog {

    private static final String TAG = "BuyDiamondDialog";
    private final int type_source;
    private Context context;
    @BindView(R.id.iv_buy_diamond_close)
    ImageView ivBuyDiamondClose;
    @BindView(R.id.cb_1)
    CheckBox cb1;
    @BindView(R.id.ll_buy_diamond_1)
    LinearLayout llBuyDiamond1;
    @BindView(R.id.cb_2)
    CheckBox cb2;
    @BindView(R.id.ll_buy_diamond_2)
    LinearLayout llBuyDiamond2;
    @BindView(R.id.cb_3)
    CheckBox cb3;
    @BindView(R.id.ll_buy_diamond_3)
    LinearLayout llBuyDiamond3;
    @BindView(R.id.cb_input)
    CheckBox cbInput;
    @BindView(R.id.et_buy_diamond_input)
    EditText etBuyDiamondInput;
    @BindView(R.id.ll_buy_diamond_input)
    LinearLayout llBuyDiamondInput;
    @BindView(R.id.btn_buy_diamond_pay)
    Button btnBuyDiamondPay;
    private CheckBox[] checkBoxes = new CheckBox[4];
    private int currentIndex;//0普通 1游戏中
    private int money;
    private Handler handler = new Handler();

    public BuyDiamondDialog(Context context, int type_source) {
        super(context);
        this.context = context;
        this.type_source = type_source;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_buy_diamond;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        checkBoxes[0] = cb1;
        checkBoxes[1] = cb2;
        checkBoxes[2] = cb3;
        checkBoxes[3] = cbInput;
        money = 100;
        btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ " + money / 10.00 + " ");
        etBuyDiamondInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    selectorCheckBox(3);
                }
            }
        });
        etBuyDiamondInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.d("et", "before");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    Integer result = Integer.valueOf(str);
                    if (result >= 10) {
                        int res = result % 10;
                        if (res == 0) {
                            money = result;
                            btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ " + money / 10.00 + " ");
                        } else {
                            money = 0;
                            btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ 0.00 ");
                        }
                    } else {
                        money = 0;
                        btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ 0.00 ");
                    }
                }
                LogUtil.d(TAG, "--money:" + money);
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.d("et", "after");
            }
        });
    }

    @OnClick({R.id.iv_buy_diamond_close, R.id.ll_buy_diamond_1, R.id.ll_buy_diamond_2, R.id.ll_buy_diamond_3, R.id.et_buy_diamond_input, R.id.ll_buy_diamond_input, R.id.btn_buy_diamond_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_buy_diamond_close:
                if (type_source == 1) {
                    EventBus.getDefault().post(new PayEvent(false));
                }
                dismiss();
                break;
            case R.id.ll_buy_diamond_1:
                etBuyDiamondInput.clearFocus();
                KeyBoardUtils.closeKeyboard(etBuyDiamondInput, context);
                selectorCheckBox(0);
                money = 100;
                btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ " + money / 10.00 + " ");
                break;
            case R.id.ll_buy_diamond_2:
                etBuyDiamondInput.clearFocus();
                KeyBoardUtils.closeKeyboard(etBuyDiamondInput, context);
                selectorCheckBox(1);
                money = 300;
                btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ " + money / 10.00 + " ");
                break;
            case R.id.ll_buy_diamond_3:
                etBuyDiamondInput.clearFocus();
                KeyBoardUtils.closeKeyboard(etBuyDiamondInput, context);
                selectorCheckBox(2);
                money = 500;
                btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ " + money / 10.00 + " ");
                break;
            case R.id.et_buy_diamond_input:
            case R.id.ll_buy_diamond_input:
                selectorCheckBox(3);
                money = 0;
                btnBuyDiamondPay.setText(context.getString(R.string.sure_buy_diamond) + " ¥ 0.00 ");
                break;
            case R.id.btn_buy_diamond_pay:
                if (money > 0) {
                    pay();
                } else {
                    ToastUtils.showToastShort("请输入10的倍数");
                }
                break;
        }
    }

    private void pay() {
        switch (currentIndex) {
            case 0:
                money = 100;
                break;
            case 1:
                money = 300;
                break;
            case 2:
                money = 500;
                break;
        }
        PayUtil.pay(context, PayUtil.SOURCE_TOP_UP, money / 10.0, money, null);
        dismiss();
    }

    private void selectorCheckBox(final int index) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkBoxes[i].setChecked(false);
                }
                etBuyDiamondInput.setText("");
                currentIndex = index;
                checkBoxes[index].setChecked(true);
            }
        });
    }
}
