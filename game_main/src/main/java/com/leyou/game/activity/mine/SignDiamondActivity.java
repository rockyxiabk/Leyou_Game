package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.ipresenter.mine.ISignDiamond;
import com.leyou.game.presenter.treasure.SignDiamondPresenter;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.SignSuccessDialog;
import com.leyou.game.widget.dialog.WebViewDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 签到领取钻石页面
 *
 * @author : rocky
 * @Create Time : 2017/4/18 上午10:50
 * @Modified By: rocky
 * @Modified Time : 2017/4/18 上午10:50
 */
public class SignDiamondActivity extends BaseActivity implements ISignDiamond {

    @BindView(R.id.iv_sign_back)
    ImageView ivSignBack;
    @BindView(R.id.tv_sign_diamond_number)
    TextView tvSignDiamondNumber;
    @BindView(R.id.tv_signed_day1_state)
    TextView tvSignedDay1State;
    @BindView(R.id.iv_day1_signed)
    ImageView ivDay1Signed;
    @BindView(R.id.tv_signed_day2_state)
    TextView tvSignedDay2State;
    @BindView(R.id.iv_day2_signed)
    ImageView ivDay2Signed;
    @BindView(R.id.tv_signed_day3_state)
    TextView tvSignedDay3State;
    @BindView(R.id.iv_day3_signed)
    ImageView ivDay3Signed;
    @BindView(R.id.tv_signed_day4_state)
    TextView tvSignedDay4State;
    @BindView(R.id.iv_day4_signed)
    ImageView ivDay4Signed;
    @BindView(R.id.tv_signed_day5_state)
    TextView tvSignedDay5State;
    @BindView(R.id.iv_day5_signed)
    ImageView ivDay5Signed;
    @BindView(R.id.tv_signed_day6_state)
    TextView tvSignedDay6State;
    @BindView(R.id.iv_day6_signed)
    ImageView ivDay6Signed;
    @BindView(R.id.iv_day7_signed)
    ImageView ivDay7Signed;
    @BindView(R.id.btn_sign)
    Button btnSign;
    private SignDiamondPresenter presenter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_sign_diamond;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    public void initPresenter() {
        presenter = new SignDiamondPresenter(this, this);
    }

    @OnClick({R.id.iv_sign_back, R.id.tv_explain, R.id.btn_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sign_back:
                EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, 1));
                finishCurrentActivity();
                break;
            case R.id.tv_explain:
                WebViewDialog webViewDialog = new WebViewDialog(this, getString(R.string.sign_explain), Constants.MINE_SIGNED);
                webViewDialog.show();
                break;
            case R.id.btn_sign:
                presenter.toSign();
                break;
        }
    }

    private void setSignDayState(int day) {
        tvSignedDay1State.setVisibility(View.GONE);
        tvSignedDay2State.setVisibility(View.GONE);
        tvSignedDay3State.setVisibility(View.GONE);
        tvSignedDay4State.setVisibility(View.GONE);
        tvSignedDay5State.setVisibility(View.GONE);
        tvSignedDay6State.setVisibility(View.GONE);

        ivDay1Signed.setVisibility(View.GONE);
        ivDay2Signed.setVisibility(View.GONE);
        ivDay3Signed.setVisibility(View.GONE);
        ivDay4Signed.setVisibility(View.GONE);
        ivDay5Signed.setVisibility(View.GONE);
        ivDay6Signed.setVisibility(View.GONE);
        ivDay7Signed.setVisibility(View.GONE);
        if (day <= 7) {
            switch (day) {
                case 1:
                    ivDay1Signed.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvSignedDay1State.setVisibility(View.VISIBLE);
                    ivDay2Signed.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    tvSignedDay1State.setVisibility(View.VISIBLE);
                    tvSignedDay2State.setVisibility(View.VISIBLE);
                    ivDay3Signed.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    tvSignedDay1State.setVisibility(View.VISIBLE);
                    tvSignedDay2State.setVisibility(View.VISIBLE);
                    tvSignedDay3State.setVisibility(View.VISIBLE);
                    ivDay4Signed.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    tvSignedDay1State.setVisibility(View.VISIBLE);
                    tvSignedDay2State.setVisibility(View.VISIBLE);
                    tvSignedDay3State.setVisibility(View.VISIBLE);
                    tvSignedDay4State.setVisibility(View.VISIBLE);
                    ivDay5Signed.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    tvSignedDay1State.setVisibility(View.VISIBLE);
                    tvSignedDay2State.setVisibility(View.VISIBLE);
                    tvSignedDay3State.setVisibility(View.VISIBLE);
                    tvSignedDay4State.setVisibility(View.VISIBLE);
                    tvSignedDay5State.setVisibility(View.VISIBLE);
                    ivDay6Signed.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    tvSignedDay1State.setVisibility(View.VISIBLE);
                    tvSignedDay2State.setVisibility(View.VISIBLE);
                    tvSignedDay3State.setVisibility(View.VISIBLE);
                    tvSignedDay4State.setVisibility(View.VISIBLE);
                    tvSignedDay5State.setVisibility(View.VISIBLE);
                    tvSignedDay6State.setVisibility(View.VISIBLE);
                    ivDay7Signed.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        } else {
            tvSignedDay1State.setVisibility(View.VISIBLE);
            tvSignedDay2State.setVisibility(View.VISIBLE);
            tvSignedDay3State.setVisibility(View.VISIBLE);
            tvSignedDay4State.setVisibility(View.VISIBLE);
            tvSignedDay5State.setVisibility(View.VISIBLE);
            tvSignedDay6State.setVisibility(View.VISIBLE);
            ivDay7Signed.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDayAndDiamondNum(int day, int number) {
        tvSignDiamondNumber.setText(String.valueOf(number));
        setSignDayState(day);
        changeSignButtonState(day);
    }

    private void changeSignButtonState(int day) {
        String lastStampTime = SPUtil.getString(this, SPUtil.SETTING, "lastStampTime");
        if (!TextUtils.isEmpty(lastStampTime)) {
            Long aLong = Long.valueOf(lastStampTime);
            String lastTime = DataUtil.getConvertResult(aLong, DataUtil.YMD);
            String currentTime = DataUtil.getConvertResult(System.currentTimeMillis(), DataUtil.YMD);
            if (currentTime.equalsIgnoreCase(lastTime)) {
                btnSign.setText("连续签到" + ((day > 7) ? 7 : day) + "天");
                btnSign.setTextColor(getResources().getColor(R.color.white));
                btnSign.setBackgroundResource(R.mipmap.icon_mine_signed_button);
            } else {
                btnSign.setText("立即签到");
                btnSign.setTextColor(getResources().getColor(R.color.theme_color));
                btnSign.setBackgroundResource(R.mipmap.icon_mine_sign_un_button);
            }
        } else {
            btnSign.setText("立即签到");
            btnSign.setTextColor(getResources().getColor(R.color.theme_color));
            btnSign.setBackgroundResource(R.mipmap.icon_mine_sign_un_button);
        }
    }

    @Override
    public void showSignSuccess(int virtualCoin) {
        ToastUtils.showToastShort(getString(R.string.sign_success));
        new SignSuccessDialog(this, virtualCoin).show();
        presenter.getSignDiamond();
    }

    @Override
    public void showSignFailed() {
        showMessage(getString(R.string.sign_failed));
    }

    @Override
    public void showMessage(String msg) {
        ToastUtils.showToastShort(msg);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, 1));
            finishCurrentActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
