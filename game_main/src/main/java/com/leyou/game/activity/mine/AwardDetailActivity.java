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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.AwardAddressBean;
import com.leyou.game.bean.AwardInfoBean;
import com.leyou.game.bean.Result;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.EMailUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.util.PhoneUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.UserApi;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 奖品详情页面（包含邮寄地址）
 *
 * @author : rocky
 * @Create Time : 2017/6/16 上午9:55
 * @Modified By: rocky
 * @Modified Time : 2017/6/16 上午9:55
 */
public class AwardDetailActivity extends BaseActivity {

    @BindView(R.id.iv_detail_back)
    ImageView ivDetailBack;
    @BindView(R.id.iv_award_img)
    ImageView ivAwardImg;
    @BindView(R.id.tv_award_title)
    TextView tvAwardTitle;
    @BindView(R.id.tv_award_des)
    TextView tvAwardDes;
    @BindView(R.id.tv_award_time)
    TextView tvAwardTime;
    @BindView(R.id.et_address_name)
    EditText etAddressName;
    @BindView(R.id.et_address_phone)
    EditText etAddressPhone;
    @BindView(R.id.et_address_mail)
    EditText etAddressMail;
    @BindView(R.id.et_address_address)
    EditText etAddressAddress;
    @BindView(R.id.btn_address_commit)
    Button btnAddressCommit;
    @BindView(R.id.re_prize_address)
    RelativeLayout rePrizeAddress;
    @BindView(R.id.tv_express_name)
    TextView tvExpressName;
    @BindView(R.id.re_express_company)
    RelativeLayout reExpressCompany;
    @BindView(R.id.tv_express_no)
    TextView tvExpressNo;
    @BindView(R.id.re_express_no)
    RelativeLayout reExpressNo;
    private AwardInfoBean awardInfo;
    private String addressName;
    private String addressPhone;
    private String addressMail;
    private String address;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_award_detial;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        awardInfo = intent.getParcelableExtra("awardInfo");
        if (null != awardInfo) {
            setState();
        } else {
            awardInfo = new AwardInfoBean();
            awardInfo.id = intent.getStringExtra("prizeId");
            getAwardInfo();
        }

        etAddressName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    addressName = string;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAddressPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    addressPhone = string;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAddressMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    addressMail = string;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAddressAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    address = string;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setState() {
        if (AwardInfoBean.STATUS_NULL == awardInfo.status) {
            btnAddressCommit.setEnabled(true);
            setAwardInfo();
        } else if (awardInfo.status == AwardInfoBean.STATUS_WAITING) {
            getAwardInfo();
            btnAddressCommit.setEnabled(false);
            btnAddressCommit.setText("修改地址");
        } else if (awardInfo.status == AwardInfoBean.STATUS_SEND) {
            getAwardInfo();
            btnAddressCommit.setEnabled(false);
            btnAddressCommit.setText("已发货");
        }
    }

    private void getAwardInfo() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getMessagePrizeDetail(awardInfo.id), new Observer<Result<AwardInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                setAwardInfo();
            }

            @Override
            public void onNext(Result<AwardInfoBean> awardInfoBeanResult) {
                if (awardInfoBeanResult.result == 1) {
                    AwardInfoBean data = awardInfoBeanResult.data;
                    awardInfo = data;
                    setAwardInfo();
                }
            }
        });
    }

    private void setAwardInfo() {
        if (awardInfo.status == AwardInfoBean.STATUS_NULL) {
            btnAddressCommit.setEnabled(true);
            if (awardInfo.type == AwardInfoBean.PRIZE_TYPE_TRUE) {
                rePrizeAddress.setVisibility(View.VISIBLE);
                btnAddressCommit.setText("提交收货地址");
            } else if (AwardInfoBean.PRIZE_TYPE_VIRTUAL == awardInfo.type) {
                rePrizeAddress.setVisibility(View.GONE);
                address = "";
                btnAddressCommit.setText("提交充值信息");
            }
        } else if (awardInfo.status == AwardInfoBean.STATUS_WAITING) {
            if (awardInfo.type == AwardInfoBean.PRIZE_TYPE_TRUE) {
                rePrizeAddress.setVisibility(View.VISIBLE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("修改地址");
            } else if (AwardInfoBean.PRIZE_TYPE_VIRTUAL == awardInfo.type) {
                rePrizeAddress.setVisibility(View.GONE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("等待充值");
            }
        } else if (awardInfo.status == AwardInfoBean.STATUS_SEND) {
            if (awardInfo.type == AwardInfoBean.PRIZE_TYPE_TRUE) {
                rePrizeAddress.setVisibility(View.VISIBLE);
                reExpressCompany.setVisibility(View.VISIBLE);
                reExpressNo.setVisibility(View.VISIBLE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("已发货");
            } else if (AwardInfoBean.PRIZE_TYPE_VIRTUAL == awardInfo.type) {
                rePrizeAddress.setVisibility(View.GONE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("已充值");
            }
        }
        tvAwardDes.setText(awardInfo.prizeName);
        tvAwardTitle.setText(awardInfo.game + "    " + NumberFormatUtil.formatInteger(awardInfo.rank) + "等奖");
        Glide.with(this).load(awardInfo.prizeUrl).error(R.mipmap.icon_default).into(ivAwardImg);
        tvAwardTime.setText("获奖时间：" + DataUtil.getConvertResult(awardInfo.addTime, DataUtil.Y_M_D));
        if (awardInfo.status > 0) {
            etAddressAddress.setFocusable(false);
            etAddressName.setFocusable(false);
            etAddressPhone.setFocusable(false);
            etAddressMail.setFocusable(false);
            etAddressAddress.setText(awardInfo.userAddress);
            address = awardInfo.userAddress;
            etAddressName.setText(awardInfo.userName);
            addressName = awardInfo.userName;
            etAddressPhone.setText(awardInfo.userPhone);
            addressPhone = awardInfo.userPhone;
            etAddressMail.setText(awardInfo.userEmail);
            addressMail = awardInfo.userEmail;
            tvExpressName.setText(awardInfo.courierCompany);
            tvExpressNo.setText(awardInfo.courierNumber);
        }
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.iv_detail_back, R.id.et_address_name, R.id.et_address_phone, R.id.et_address_mail, R.id.et_address_address, R.id.btn_address_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_detail_back:
                finishCurrentActivity();
                break;
            case R.id.et_address_name:
            case R.id.et_address_phone:
            case R.id.et_address_mail:
            case R.id.et_address_address:
                if (awardInfo.status == 1) {
                    etAddressAddress.setFocusable(true);
                    etAddressAddress.setFocusableInTouchMode(true);
                    etAddressName.setFocusable(true);
                    etAddressName.setFocusableInTouchMode(true);
                    etAddressPhone.setFocusable(true);
                    etAddressPhone.setFocusableInTouchMode(true);
                    etAddressMail.setFocusable(true);
                    etAddressMail.setFocusableInTouchMode(true);
                    btnAddressCommit.setEnabled(true);
                }
                break;
            case R.id.btn_address_commit:
                commitAddress();
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

    private void commitAddress() {
        if (!TextUtils.isEmpty(addressName)) {
            if (!TextUtils.isEmpty(addressPhone) && PhoneUtil.isMobileNumber(addressPhone)) {
                if (!TextUtils.isEmpty(addressMail) && addressMail.endsWith(".com")) {
                    AwardAddressBean awardAddressBean = new AwardAddressBean(awardInfo.id, addressName, addressPhone, address, addressMail);
                    HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).commitAwardAddress(awardAddressBean), new Observer<Result>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showToastShort("提交失败，请重新提交");
                        }

                        @Override
                        public void onNext(Result result) {
                            if (result.result == 1) {
                                ToastUtils.showToastShort("提交成功，注意查收邮件和物流信息");
                                finishCurrentActivity();
                            } else {
                                ToastUtils.showToastShort("提交失败，请重新提交");
                            }
                        }
                    });
                } else {
                    ToastUtils.showToastShort("请输入正确的邮箱地址");
                }
            } else {
                ToastUtils.showToastShort("请输入正确的收件人手机号");
            }
        } else {
            ToastUtils.showToastShort("请确认邮寄信息是否正确");
        }
    }
}
