package com.leyou.game.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leyou.game.GameApplication;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.IRegisterActivity;
import com.leyou.game.presenter.RegisterPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.PhoneUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 用户注册页面
 *
 * @author : rocky
 * @Create Time : 2017/3/30 上午10:17
 * @Modified By: rocky
 * @Modified Time : 2017/3/30 上午10:17
 */
public class RegisterActivity extends BaseActivity implements IRegisterActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.ed_phoneNum)
    EditText edPhoneNum;
    @BindView(R.id.ed_verifyNum)
    EditText edVerifyNum;
    @BindView(R.id.btn_logoIn)
    Button btnLogoIn;
    @BindView(R.id.tv_request_verify)
    TextView tvRequestVerify;
    @BindView(R.id.tv_immediately_in)
    TextView tvImmediatelyIn;
    private String phoneNumber;
    private String verifyVode;
    private RegisterPresenter presenter;
    private boolean isRequestSMS;

    private CountDownTimer timer = new CountDownTimer(90000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tvRequestVerify.setText("重新获取(" + (millisUntilFinished / 1000) + "s)");
        }

        @Override
        public void onFinish() {
            tvRequestVerify.setEnabled(true);
            tvRequestVerify.setText("重新获取验证码");
        }
    };
    private LoadingProgressDialog dialog;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        tvImmediatelyIn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加文字下划线
        //手机号码输入监听
        edPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumber = s.toString();
                LogUtil.d(TAG, phoneNumber);
                if (phoneNumber.length() == 11) {
                    if (!PhoneUtil.isMobileNumber(phoneNumber)) {
                        ToastUtils.showToastShort("手机号码格式错误");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edVerifyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyVode = s.toString();
                if (!TextUtils.isEmpty(verifyVode)) {
                    btnLogoIn.setEnabled(true);
                } else {
                    btnLogoIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edVerifyNum.setFocusable(false);
        edVerifyNum.setFocusableInTouchMode(false);
    }

    @Override
    public void initPresenter() {
        presenter = new RegisterPresenter(this, this);
    }

    @OnClick({R.id.tv_request_verify, R.id.btn_logoIn, R.id.tv_immediately_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_request_verify:
                if (HttpUtil.isNetworkConnected(this)) {
                    if (PhoneUtil.isMobileNumber(phoneNumber)) {
                        presenter.getSmsCode(phoneNumber);
                    } else {
                        ToastUtils.showToastShort("请输入正确的手机号");
                    }
                } else {
                    ToastUtils.showToastShort("检查网络连接是否正常！");
                }
                break;
            case R.id.btn_logoIn:
                if (!TextUtils.isEmpty(verifyVode)) {
                    presenter.verifyPhoneNum(phoneNumber, verifyVode);
                } else {
                    ToastUtils.showToastShort("请输入验证码");
                }
                break;
            case R.id.tv_immediately_in:
                UserData.getInstance().clearUserInfo();
                toMainActivity();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void sendSmsStatus(boolean isSuccess) {
        dismissedLoading();
        if (isSuccess) {
            edPhoneNum.setFocusable(false);
            edPhoneNum.setFocusableInTouchMode(false);

            edVerifyNum.setFocusable(true);
            edVerifyNum.setFocusableInTouchMode(true);

            tvRequestVerify.setEnabled(false);

            edPhoneNum.setFocusable(true);
            edPhoneNum.setFocusableInTouchMode(true);

            isRequestSMS = true;
            timer.start();
            showMessageToast("验证码发送成功，请注意查收！");
        }
    }

    @Override
    public void showLoading() {
        dialog = new LoadingProgressDialog(this, false);
        dialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != dialog) {
            dialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @Override
    public void showMessageToast(String msg) {
        ToastUtils.showToastShort(msg);
    }

    @Override
    public void toMainActivity() {
        timer.cancel();
        EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, 1));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishCurrentActivity();
    }
}
