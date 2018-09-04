package com.leyou.game.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.event.PriceStateChangeEvent;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.util.PhoneUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.GameApi;
import com.leyou.game.widget.dialog.mine.ScreenShotDialog;
import com.leyou.game.widget.dialog.mine.ScreenShotPrizeDialog;
import com.leyou.game.widget.dialog.mine.ShareApplyCashDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

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
    SimpleDraweeView ivAwardImg;
    @BindView(R.id.tv_award_title)
    TextView tvAwardTitle;
    @BindView(R.id.tv_award_rank)
    TextView tvAwardRank;
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
    private GameWinPriseBean awardInfo;
    private String addressName;
    private String addressPhone;
    private String addressMail;
    private String address = "官方地址";
    private SHARE_MEDIA shareType;
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("plat", "platform" + platform);
            commitAddress();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showMessageToast("分享取消，申请提现失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showMessageToast("分享取消，申请提现失败");
        }
    };

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
            awardInfo = new GameWinPriseBean();
            String winId = intent.getStringExtra("winId");
            int parseInt = Integer.parseInt(winId);
            awardInfo.winId = parseInt;
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
        if (GameWinPriseBean.STATUS_NULL == awardInfo.status) {
            btnAddressCommit.setEnabled(true);
            setAwardInfo();
        } else if (awardInfo.status == GameWinPriseBean.STATUS_WAITING) {
            getAwardInfo();
            btnAddressCommit.setEnabled(false);
            btnAddressCommit.setText("修改地址");
        } else if (awardInfo.status == GameWinPriseBean.STATUS_SEND) {
            getAwardInfo();
            btnAddressCommit.setEnabled(false);
            btnAddressCommit.setText("已发货");
        }
    }

    private void getAwardInfo() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getPrizeDetail(awardInfo.winId), new Observer<Result<GameWinPriseBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                setAwardInfo();
            }

            @Override
            public void onNext(Result<GameWinPriseBean> awardInfoBeanResult) {
                if (awardInfoBeanResult.result == 1) {
                    GameWinPriseBean data = awardInfoBeanResult.data;
                    awardInfo = data;
                    setAwardInfo();
                }
            }
        });
    }

    private void setAwardInfo() {
        if (awardInfo.status == GameWinPriseBean.STATUS_NULL) {
            btnAddressCommit.setEnabled(true);
            if (awardInfo.prizeType == GameWinPriseBean.PRIZE_TYPE_TRUE) {
                rePrizeAddress.setVisibility(View.VISIBLE);
                btnAddressCommit.setText("提交收货地址");
            } else if (GameWinPriseBean.PRIZE_TYPE_VIRTUAL == awardInfo.prizeType) {
                rePrizeAddress.setVisibility(View.GONE);
                address = "";
                btnAddressCommit.setText("提交充值信息");
            }
        } else if (awardInfo.status == GameWinPriseBean.STATUS_WAITING) {
            if (awardInfo.prizeType == GameWinPriseBean.PRIZE_TYPE_TRUE) {
                rePrizeAddress.setVisibility(View.VISIBLE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("修改地址");
            } else if (GameWinPriseBean.PRIZE_TYPE_VIRTUAL == awardInfo.prizeType) {
                rePrizeAddress.setVisibility(View.GONE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("等待充值");
            }
        } else if (awardInfo.status == GameWinPriseBean.STATUS_SEND) {
            if (awardInfo.prizeType == GameWinPriseBean.PRIZE_TYPE_TRUE) {
                rePrizeAddress.setVisibility(View.VISIBLE);
                reExpressCompany.setVisibility(View.VISIBLE);
                reExpressNo.setVisibility(View.VISIBLE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("已发货");
                btnAddressCommit.setVisibility(View.GONE);
            } else if (GameWinPriseBean.PRIZE_TYPE_VIRTUAL == awardInfo.prizeType) {
                rePrizeAddress.setVisibility(View.GONE);
                btnAddressCommit.setEnabled(false);
                btnAddressCommit.setText("已充值");
                btnAddressCommit.setVisibility(View.GONE);
            }
        }
        tvAwardTitle.setText(awardInfo.prizeName);
        tvAwardRank.setText("" + NumberFormatUtil.formatInteger(awardInfo.prizeRank) + "等奖");
        ivAwardImg.setImageURI(awardInfo.prizeImgUrl);
        tvAwardTime.setText("" + DataUtil.getConvertResult(awardInfo.createDate, DataUtil.Y_M_D) + "");
        if (awardInfo.status > 0) {
            etAddressAddress.setFocusable(false);
            etAddressName.setFocusable(false);
            etAddressPhone.setFocusable(false);
            etAddressMail.setFocusable(false);
            etAddressAddress.setText(awardInfo.address);
            address = awardInfo.address;
            etAddressName.setText(awardInfo.realName);
            addressName = awardInfo.realName;
            etAddressPhone.setText(awardInfo.phone);
            addressPhone = awardInfo.phone;
            etAddressMail.setText(awardInfo.email);
            addressMail = awardInfo.email;
            tvExpressName.setText(awardInfo.expressCompany);
            tvExpressNo.setText(awardInfo.expressNo);
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
                if (awardInfo.status != GameWinPriseBean.STATUS_SEND) {
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
                if (GameWinPriseBean.STATUS_NULL == awardInfo.status) {
                    if (!TextUtils.isEmpty(addressName)) {
                        if (!TextUtils.isEmpty(addressPhone) && PhoneUtil.isMobileNumber(addressPhone)) {
                            if (!TextUtils.isEmpty(addressMail) && addressMail.endsWith(".com")) {
                                showWithCashShareView();
                            } else {
                                ToastUtils.showToastShort("请输入正确的邮箱地址");
                            }
                        } else {
                            ToastUtils.showToastShort("请输入正确的收件人手机号");
                        }
                    } else {
                        ToastUtils.showToastShort("请确认邮寄信息是否正确");
                    }
                } else {
                    commitAddress();
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void commitAddress() {
        if (!TextUtils.isEmpty(addressName)) {
            if (!TextUtils.isEmpty(addressPhone) && PhoneUtil.isMobileNumber(addressPhone)) {
                if (!TextUtils.isEmpty(addressMail) && addressMail.endsWith(".com")) {
                    HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).commitPrizeAddress(awardInfo.winId,
                            addressName, addressPhone, address, addressMail), new Observer<Result>() {
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
                                EventBus.getDefault().post(new PriceStateChangeEvent(1));
                                finishCurrentActivity();
                            } else {
                                ToastUtils.showToastShort(result.msg);
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

    public void showWithCashShareView() {
        final ShareApplyCashDialog cashDialog = new ShareApplyCashDialog(this);
        cashDialog.setOnViewClickListener(new ShareApplyCashDialog.OnViewClickListener() {
            @Override
            public void shareChannel(SHARE_MEDIA type) {
                shareTo(type);
                cashDialog.dismiss();
            }

            @Override
            public void exit() {
                cashDialog.dismiss();
            }
        });
        cashDialog.show();
    }

    public void shareTo(SHARE_MEDIA type) {
        shareType = type;
        ScreenShotPrizeDialog screenShotDialog = new ScreenShotPrizeDialog(this, awardInfo);
        screenShotDialog.setOnViewClickListener(new ScreenShotPrizeDialog.OnViewClickListener() {
            @Override
            public void share(String path) {
                startShare(path);
            }
        });
        screenShotDialog.show();
    }

    public void startShare(String path) {
        try {
            File file = new File(path);
            UMImage umImage = new UMImage(this, file);
            umImage.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
            umImage.compressFormat = Bitmap.CompressFormat.JPEG;
            new ShareAction(this)
                    .withMedia(umImage)
                    .setPlatform(shareType)
                    .setCallback(umShareListener)
                    .share();
        } catch (Exception e) {
            e.printStackTrace();
            showMessageToast("图片错误");
        }
    }

    private void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }
}
