package com.leyou.game.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.PayUtil;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 充值页面
 *
 * @author : rocky
 * @Create Time : 2017/10/28 下午6:18
 * @Modified By: rocky
 * @Modified Time : 2017/10/28 下午6:18
 */
public class UptoActivity extends BaseActivity {

    private static final String TAG = "UptoActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_diamond_number)
    EditText etDiamondNumber;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.cd_input)
    CardView cdInput;
    @BindView(R.id.cd_100)
    CardView cd100;
    @BindView(R.id.cd_300)
    CardView cd300;
    @BindView(R.id.cd_500)
    CardView cd500;
    @BindView(R.id.cd_700)
    CardView cd700;
    @BindView(R.id.btn_upto)
    Button btnUpto;
    private int money = 0;
    private int source = PayUtil.SOURCE_TO_UP;
    private String gameId = "0";
    private String gameName = "充值";
    private int diamondNumber;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_upto;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (null != intent) {
            diamondNumber = intent.getIntExtra("diamondNumber", 0);
            gameName = intent.getStringExtra("gameName");
            gameId = intent.getStringExtra("gameId");
        }
        if (TextUtils.isEmpty(gameName)) {
            gameName = "钻石充值";
        }
        etDiamondNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.d("et", "before");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    try {
                        Integer result = Integer.valueOf(str);
                        if (result >= 10) {
                            int res = result % 10;
                            if (res == 0) {
                                money = result;
                                tvMoney.setText("" + (money / 10f) + "元");
                            } else {
                                money = 0;
                                tvMoney.setText("" + (money / 10f) + "元");
                            }
                        } else {
                            money = 0;
                            tvMoney.setText("" + (money / 10f) + "元");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LogUtil.d(TAG, "--money:" + money);
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.d("et", "after");
            }
        });
        if (diamondNumber > 0) {
            source = PayUtil.SOURCE_H5;
            etDiamondNumber.setText("" + diamondNumber + "");
            money = diamondNumber;
            tvMoney.setText("" + (money / 10f) + "元");
        } else {
            KeyBoardUtils.openKeyboard(etDiamondNumber, this);
        }
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

    @OnClick({R.id.iv_back, R.id.et_diamond_number, R.id.cd_input, R.id.cd_100, R.id.cd_300, R.id.cd_500, R.id.cd_700, R.id.btn_upto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                KeyBoardUtils.closeKeyboard(etDiamondNumber, this);
                finishCurrentActivity();
                break;
            case R.id.et_diamond_number:
                etDiamondNumber.setFocusable(true);
                etDiamondNumber.setFocusableInTouchMode(true);
                etDiamondNumber.setSelection(etDiamondNumber.getText().length());
                break;
            case R.id.cd_100:
                money = 100;
                etDiamondNumber.setText("100");
                tvMoney.setText("10元");
                etDiamondNumber.setFocusable(false);
                etDiamondNumber.setFocusableInTouchMode(false);
                KeyBoardUtils.closeKeyboard(etDiamondNumber, this);
                break;
            case R.id.cd_300:
                money = 300;
                etDiamondNumber.setText("300");
                tvMoney.setText("30元");
                etDiamondNumber.setFocusable(false);
                etDiamondNumber.setFocusableInTouchMode(false);
                KeyBoardUtils.closeKeyboard(etDiamondNumber, this);
                break;
            case R.id.cd_500:
                money = 500;
                etDiamondNumber.setText("500");
                tvMoney.setText("50元");
                etDiamondNumber.setFocusable(false);
                etDiamondNumber.setFocusableInTouchMode(false);
                KeyBoardUtils.closeKeyboard(etDiamondNumber, this);
                break;
            case R.id.cd_700:
                money = 700;
                etDiamondNumber.setText("700");
                tvMoney.setText("70元");
                etDiamondNumber.setFocusable(false);
                etDiamondNumber.setFocusableInTouchMode(false);
                KeyBoardUtils.closeKeyboard(etDiamondNumber, this);
                break;
            case R.id.btn_upto:
                if ((money / 10.0) > 0) {
                    if (5000 >= (money / 10.0)) {
                        PayUtil.pay(this, source, money / 10.0, money, gameId, gameName);
                        finishCurrentActivity();
                    } else {
                        ToastUtils.showToastShort("钻石充值单次限额5000元");
                    }
                } else {
                    ToastUtils.showToastShort("请输入10的倍数");
                }
                KeyBoardUtils.closeKeyboard(etDiamondNumber, this);
                break;
        }
    }
}
