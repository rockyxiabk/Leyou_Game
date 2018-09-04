package com.leyou.game.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.BankCardInfoBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.mine.IWithCashApply;
import com.leyou.game.presenter.mine.WithCashApplyPresenter;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.model.UserInfo;

/**
 * Description : 已绑定银行卡 提现申请
 *
 * @author : rocky
 * @Create Time : 2017/8/13 下午5:02
 * @Modified By: rocky
 * @Modified Time : 2017/8/13 下午5:02
 */
public class WithCashApplyActivity extends BaseActivity implements IWithCashApply {

    @BindView(R.id.iv_cash_back)
    ImageView ivCashBack;
    @BindView(R.id.iv_card_icon)
    ImageView ivCardIcon;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.tv_card_number)
    TextView tvCardNumber;
    @BindView(R.id.tv_un_bind_card)
    TextView tvUnBindCard;
    @BindView(R.id.et_with_cash_card_number)
    EditText etWithCashCardNumber;
    @BindView(R.id.tv_current_money)
    TextView tvCurrentMoney;
    @BindView(R.id.btn_with_cash_commit)
    Button btnWithCashCommit;
    private LoadingProgressDialog loadingProgressDialog;
    private WithCashApplyPresenter presenter;
    private Double money = 0.0;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_with_cash_apply;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        etWithCashCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s_ = etWithCashCardNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(s_)) {
                    Double money_D = Double.valueOf(s_);
                    money = money_D;
                }
            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new WithCashApplyPresenter(this, this);
    }

    @OnClick({R.id.iv_cash_back, R.id.tv_un_bind_card, R.id.et_with_cash_card_number, R.id.btn_with_cash_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cash_back:
                KeyBoardUtils.closeKeyboard(etWithCashCardNumber, this);
                finishCurrentActivity();
                break;
            case R.id.tv_un_bind_card:
                startActivity(new Intent(this, WithCashBindCardActivity.class));
                finishCurrentActivity();
                break;
            case R.id.et_with_cash_card_number:
                break;
            case R.id.btn_with_cash_commit:
                commit();
                break;
        }
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

    private void commit() {
        if (0 < money && money <= UserData.getInstance().getMoney()) {
            loadingProgressDialog = new LoadingProgressDialog(this, false);
            loadingProgressDialog.show();
            loadingProgressDialog.setLoadingText("提交申请中...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.commitApply(money);
                }
            }, 500);
        } else {
            showMessageToast("请输入正确的金额");
        }
    }

    @Override
    public void showBankInfo(BankCardInfoBean bankCardInfoBean) {
        Glide.with(this).load("").error(R.mipmap.icon_bank_card).into(ivCardIcon);
        tvCardName.setText(bankCardInfoBean.bankName);
        tvCardNumber.setText(bankCardInfoBean.bankIntro);
        tvCurrentMoney.setText("当前余额 " + UserData.getInstance().getMoney() + " 元");
    }

    @Override
    public void showApplyCashResult(boolean result, String msg) {
        ToastUtils.showToastShort(msg);
        if (result) {
            finishCurrentActivity();
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
}
