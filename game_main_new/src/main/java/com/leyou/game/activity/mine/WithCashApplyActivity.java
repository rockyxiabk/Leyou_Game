package com.leyou.game.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.mine.IWithCashApply;
import com.leyou.game.presenter.mine.WithCashApplyPresenter;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.mine.ApplyCashSuccessDialog;
import com.leyou.game.widget.dialog.mine.ScreenShotDialog;
import com.leyou.game.widget.dialog.mine.ShareApplyCashDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 已绑定银行卡 申请
 *
 * @author : rocky
 * @Create Time : 2017/8/13 下午5:02
 * @Modified By: rocky
 * @Modified Time : 2017/8/13 下午5:02
 */
public class WithCashApplyActivity extends BaseActivity implements IWithCashApply {

    @BindView(R.id.iv_cash_back)
    ImageView ivCashBack;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.iv_cash_note)
    ImageView ivCashNote;
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
    private SHARE_MEDIA shareType;//要分享到的渠道
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("plat", "platform" + platform);
            commitApply();
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
        return R.layout.activity_with_cash_apply;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        tvCardName.setText(UserData.getInstance().getBankName());
        tvCardNumber.setText(UserData.getInstance().getCardNum());
        tvCurrentMoney.setText("余额:" + UserData.getInstance().getMoney() + "");
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

    @OnClick({R.id.iv_cash_back, R.id.iv_cash_note, R.id.tv_un_bind_card, R.id.et_with_cash_card_number, R.id.btn_with_cash_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cash_back:
                KeyBoardUtils.closeKeyboard(etWithCashCardNumber, this);
                finishCurrentActivity();
                break;
            case R.id.iv_cash_note:
                startActivity(new Intent(this, WithCashNoteActivity.class));
                break;
            case R.id.tv_un_bind_card:
                startActivity(new Intent(this, WithCashBindCardActivity.class));
                finishCurrentActivity();
                break;
            case R.id.et_with_cash_card_number:
                break;
            case R.id.btn_with_cash_commit:
                if (0 < money && money <= UserData.getInstance().getMoney()) {
                    showWithCashShareView();
                } else {
                    showMessageToast("请输入正确的金额");
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

    @Override
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

    @Override
    public void shareTo(SHARE_MEDIA type) {
        shareType = type;
        ScreenShotDialog screenShotDialog = new ScreenShotDialog(this, money + "");
        screenShotDialog.setOnViewClickListener(new ScreenShotDialog.OnViewClickListener() {
            @Override
            public void share(String path) {
                startShare(path);
            }
        });
        screenShotDialog.show();
    }

    @Override
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

    @Override
    public void commitApply() {
        showLoading();
        changeLoadingDes("提交申请中...");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.commitApply(money);
            }
        }, 500);
    }

    @Override
    public void showApplyCashResult(boolean result, String msg) {
        dismissedLoading();
        ToastUtils.showToastShort(msg);
        if (result) {
            ApplyCashSuccessDialog successDialog = new ApplyCashSuccessDialog(this);
            successDialog.setOnClickListener(new ApplyCashSuccessDialog.OnViewClickListener() {
                @Override
                public void exit() {
                    finishCurrentActivity();
                }
            });
            successDialog.show();
        }
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(this, false);
        loadingProgressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != loadingProgressDialog) {
            loadingProgressDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != loadingProgressDialog) {
            loadingProgressDialog.dismiss();
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
