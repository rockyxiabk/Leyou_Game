package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.SystemApi;
import com.leyou.game.util.newapi.UserApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 验证当前手机号 更换手机号
 *
 * @author : rocky
 * @Create Time : 2017/10/27 上午12:09
 * @Modified Time : 2017/10/27 上午12:09
 */
public class ChangePhoneDialog extends BaseDialog {
    private static final String TAG = "ChangePhoneDialog";

    public static int TYPE_VERIFY = 1;//验证当前手机号
    public static int TYPE_NEW_VERIFY = 2;//绑定新手机号
    private int type;
    private Context context;

    @BindView(R.id.tv_item_title)
    TextView tvItemTitle;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ed_phoneNum)
    EditText edPhoneNum;
    @BindView(R.id.tv_verify_code)
    TextView tvVerifyCode;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.tv_send_sms_code)
    TextView tvSendSmsCode;
    @BindView(R.id.tv_item_done)
    TextView tvItemDone;
    @BindView(R.id.ll_item_done)
    LinearLayout llItemDone;
    private CountDownTimer timer = new CountDownTimer(90000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tvSendSmsCode.setText("重新获取(" + (millisUntilFinished / 1000) + "s)");
        }

        @Override
        public void onFinish() {
            tvSendSmsCode.setEnabled(true);
            tvSendSmsCode.setText("获取验证码");
            isRequestSMS = false;
        }
    };
    private boolean isRequestSMS;

    public ChangePhoneDialog(Context context, int type) {
        super(context);
        this.context = context;
        this.type = type;
        setCanceledOnTouchOutside(false);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_change_phone;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (type == TYPE_VERIFY) {
            tvItemTitle.setText("验证当前手机");
            tvItemDone.setText("下一步");
            edPhoneNum.setEnabled(false);
            edPhoneNum.setText(UserData.getInstance().getPhoneNum());
        } else if (type == TYPE_NEW_VERIFY) {
            tvItemTitle.setText("绑定新手机");
            tvItemDone.setText("确认更换");
            edPhoneNum.setEnabled(true);
            edPhoneNum.setText("");
            edCode.setText("");
            edPhoneNum.setFocusable(true);
            edPhoneNum.setFocusableInTouchMode(true);
            edCode.setFocusable(false);
            edCode.setFocusableInTouchMode(false);
        }
        tvSendSmsCode.setEnabled(true);
        tvSendSmsCode.setText("获取验证码");
        isRequestSMS = false;
    }

    @OnClick({R.id.ed_phoneNum, R.id.ed_code, R.id.tv_send_sms_code, R.id.ll_item_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ed_phoneNum:
                break;
            case R.id.ed_code:
                break;
            case R.id.tv_send_sms_code:
                if (!isRequestSMS) {
                    sendSmsCode(edPhoneNum.getText().toString());
                }
                break;
            case R.id.ll_item_done:
                if (type == TYPE_VERIFY) {
                    checkPhone(UserData.getInstance().getPhoneNum(), edCode.getText().toString());
                } else if (type == TYPE_NEW_VERIFY) {
                    updatePhone(edPhoneNum.getText().toString(), edCode.getText().toString());
                }
                break;
        }
    }

    public void sendSmsStatus(boolean isSuccess) {
        if (isSuccess) {
            edPhoneNum.setFocusable(false);
            edPhoneNum.setFocusableInTouchMode(false);

            edCode.setFocusable(true);
            edCode.setFocusableInTouchMode(true);

            tvSendSmsCode.setEnabled(false);

            edPhoneNum.setFocusable(true);
            edPhoneNum.setFocusableInTouchMode(true);

            isRequestSMS = true;
            timer.start();
        }
    }

    private void sendSmsCode(String phoneNumber) {
        HttpUtil.subscribe(HttpUtil.createApi(SystemApi.class, Constants.URL).sendSMSCode(phoneNumber), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("短信发送失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    sendSmsStatus(true);
                    ToastUtils.showToastShort("短信发送成功");
                } else {
                    sendSmsStatus(false);
                    ToastUtils.showToastShort(result.msg);
                }
            }
        });
    }

    private void updatePhone(String phoneNumber, String code) {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).upgradeUserMobile(phoneNumber, code), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("手机号绑定失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    ToastUtils.showToastShort("手机号绑定成功");
                    context.startActivity(new Intent(context, RegisterActivity.class));
                    dismiss();
                } else {
                    ToastUtils.showToastShort(result.msg);
                }
            }
        });
    }

    private void checkPhone(String phoneNumber, String code) {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).checkUserMobile(phoneNumber, code), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("手机号验证失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    type = TYPE_NEW_VERIFY;
                    timer.cancel();
                    init();
                    ToastUtils.showToastShort(((String) result.data));
                } else {
                    ToastUtils.showToastShort(result.msg);
                }
            }
        });
    }
}
