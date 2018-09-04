package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.user.BankCardBean;
import com.leyou.game.util.BankCardUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.UserApi;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 绑定银行卡
 *
 * @author : rocky
 * @Create Time : 2017/8/13 下午5:02
 * @Modified By: rocky
 * @Modified Time : 2017/8/13 下午5:02
 */
public class WithCashBindCardActivity extends BaseActivity {

    @BindView(R.id.iv_cash_back)
    ImageView ivCashBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.re_bind_card)
    RelativeLayout reBindCardView;
    @BindView(R.id.tv_current_card)
    TextView tvCurrentCard;
    @BindView(R.id.view_bind_line)
    View viewLine;
    @BindView(R.id.et_with_cash_apply_name)
    EditText etWithCashApplyName;
    @BindView(R.id.et_with_cash_card_number)
    EditText etWithCashCardNumber;
    @BindView(R.id.et_with_cash_card_bank)
    EditText etWithCashCardBank;
    @BindView(R.id.btn_with_cash_commit)
    Button btnWithCashCommit;
    private String cashName = "";
    private String cashCard = "";
    private String cashBank = "";
    private LoadingProgressDialog loadingProgressDialog;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_with_cash_bind_card;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (UserData.getInstance().isBindCard()) {
            tvTitle.setText(getString(R.string.with_cash_band_card_change));
            reBindCardView.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
            tvCurrentCard.setText(UserData.getInstance().getCardNum());
        } else {
            tvTitle.setText(getString(R.string.with_cash_band_card));
            reBindCardView.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }

        etWithCashApplyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    cashName = string;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etWithCashCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cashCard = etWithCashCardNumber.getText().toString().trim();
                if (cashCard.length() == 0) {
                    etWithCashCardBank.setText("");
                } else if (cashCard.length() == 6) {
                    String nameOfBank = BankCardUtil.getNameOfBank(cashCard);
                    etWithCashCardBank.setText(nameOfBank);
                } else if (cashCard.length() == 8) {
                    String nameOfBank = BankCardUtil.getNameOfBank(cashCard);
                    if (!TextUtils.isEmpty(nameOfBank)) {
                        etWithCashCardBank.setText(nameOfBank);
                    }
                } else if (cashCard.length() > 15) {
                    String nameOfBank = BankCardUtil.getNameOfBank(cashCard);
                    if (!TextUtils.isEmpty(nameOfBank)) {
                        etWithCashCardBank.setText(nameOfBank);
                    }
                }
            }
        });
        etWithCashCardBank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    cashBank = string;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initPresenter() {
    }

    @OnClick({R.id.iv_cash_back, R.id.et_with_cash_apply_name, R.id.et_with_cash_card_number, R.id.et_with_cash_card_bank, R.id.btn_with_cash_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cash_back:
                finishCurrentActivity();
                break;
            case R.id.et_with_cash_apply_name:
                break;
            case R.id.et_with_cash_card_number:
                break;
            case R.id.et_with_cash_card_bank:
                break;
            case R.id.btn_with_cash_commit:
                bindCard();
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

    private void bindCard() {
        loadingProgressDialog = new LoadingProgressDialog(this, false);
        loadingProgressDialog.show();
        loadingProgressDialog.setLoadingText("正在提交中...");
        if (!TextUtils.isEmpty(cashCard) && BankCardUtil.checkBankCard(cashCard)) {
            if (!TextUtils.isEmpty(cashName)) {
                HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).bindCard(cashName, cashCard, cashBank), new Observer<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showMessageToast("提交失败");
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.result == 1) {
                            showMessageToast("提交成功");
                            BankCardBean bankCardBean = new BankCardBean();
                            bankCardBean.bankName = cashBank;
                            bankCardBean.cardNum = cashCard.substring(0, 4) + " **** **** " + cashCard.substring(cashCard.length() - 4, cashCard.length());
                            UserData.getInstance().setBankInfo(bankCardBean);
                            UserData.getInstance().setIsBindCard(true);
                            finishCurrentActivity();
                        } else {
                            showMessageToast("提交失败");
                        }
                    }
                });
            } else {
                showMessageToast("持卡人不可为空");
            }
        } else {
            showMessageToast("请输入正确的银行卡号！");
        }
    }

    private void showMessageToast(String msg) {
        loadingProgressDialog.dismiss();
        ToastUtils.showToastShort(msg);
    }
}
