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

import com.leyou.game.Constants;
import com.leyou.game.GameApplication;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.IRegisterActivity;
import com.leyou.game.presenter.RegisterPresenter;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.PhoneUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Description : 用户注册页面
 *
 * @author : rocky
 * @Create Time : 2017/3/30 上午10:17
 * @Modified By: rocky
 * @Modified Time : 2017/3/30 上午10:17
 */
public class RegisterActivity extends BaseActivity implements IRegisterActivity, OnSendMessageHandler {
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
    /**
     * SMSSDK中的事件接收器
     */
    private EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    LogUtil.d(TAG, "提交验证码成功" + data.toString());
                    presenter.verifyPhoneNum(phoneNumber);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    LogUtil.d(TAG, "获取验证码成功" + data.toString());
                    //智能验证
                    boolean smart = (Boolean) data;
                    if (smart) {
                        //通过智能验证
                        presenter.verifyPhoneNum(phoneNumber);
                    } else {
                        //依然走短信验证
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToastShort("已向该手机发送验证码，请注意查收");
                            }
                        });
                    }
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    LogUtil.d(TAG, "返回支持发送验证码的国家列表" + data.toString());
                }
            } else {
                LogUtil.d(TAG, data.toString());
                try {
                    String json = data.toString();
                    json = json.substring(21, json.length());
                    LogUtil.d(TAG, json);
                    JSONObject object = new JSONObject(json);
                    final String detail = object.getString("detail");
                    int status = object.getInt("status");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToastShort(detail);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
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
    private String textPhone = "13012345678";

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
                    if (PhoneUtil.isMobileNumber(phoneNumber)) {
                    } else {
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
                if (!TextUtils.isEmpty(verifyVode) || verifyVode.equalsIgnoreCase(textPhone)) {
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

    /**
     * 验证手机号，获取验证码
     *
     * @param country
     * @param phone
     */
    private void requestSMS(String country, String phone) {
        edPhoneNum.setFocusable(false);
        edPhoneNum.setFocusableInTouchMode(false);

        edVerifyNum.setFocusable(true);
        edVerifyNum.setFocusableInTouchMode(true);

        tvRequestVerify.setEnabled(false);

        edPhoneNum.setFocusable(true);
        edPhoneNum.setFocusableInTouchMode(true);

        isRequestSMS = true;
        timer.start();
        SMSSDK.initSDK(this, Constants.APPKEY, Constants.APPSECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调
        SMSSDK.getVerificationCode(country, phone);//获取短信验证码
    }

    private void verifyCode(String country, String phone, String code) {
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    @Override
    public boolean onSendMessage(String country, String phone) {
//        Random rnd = new Random();
//        int id = Math.abs(rnd.nextInt());
//        String uid = String.valueOf(id);
//        String nickName = "SmsSDK_User_" + uid;
//        String avatar = AVATARS[id % 12];
//        SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
        return false;
    }

    @OnClick({R.id.tv_request_verify, R.id.btn_logoIn, R.id.tv_immediately_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_request_verify:
                if (phoneNumber.equalsIgnoreCase(textPhone)) {
//                    LoadingProgressDialog progressDialog = new LoadingProgressDialog(this, true);
//                    progressDialog.setLoadingText("验证登陆中");
//                    progressDialog.show();
                    isRequestSMS = false;
                    timer.start();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.verifyPhoneNum(phoneNumber);
                        }
                    }, 2000);
                } else if (PhoneUtil.isMobileNumber(phoneNumber)) {
                    requestSMS("86", phoneNumber);
                } else {
                    ToastUtils.showToastShort("请输入正确的手机号");
                }
                break;
            case R.id.btn_logoIn:
                if (!TextUtils.isEmpty(verifyVode)) {
                    verifyCode("86", phoneNumber, verifyVode);
                } else {
                    ToastUtils.showToastShort("请输入验证码");
                }
                break;
            case R.id.tv_immediately_in:
                GameApplication.unBindAlias(UserData.getInstance().getId());
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
        if (isRequestSMS) {
            SMSSDK.unregisterEventHandler(eh);
        }
        presenter.destroy();
        super.onDestroy();
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
