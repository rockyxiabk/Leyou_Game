package com.leyou.game.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.activity.mine.AboutActivity;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.widget.dialog.ExitDialog;
import com.leyou.game.widget.dialog.FeedBackDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_setting_back)
    ImageView ivSettingBack;
    @BindView(R.id.ll_setting_feedback)
    LinearLayout llSettingFeedback;
    @BindView(R.id.ll_setting_about)
    LinearLayout llSettingAbout;
    @BindView(R.id.ll_setting_exit)
    LinearLayout llSettingExit;
    @BindView(R.id.tv_logIn_state)
    TextView tvLogInState;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserData(RefreshEvent event) {
        if (event.getIsRefresh() == RefreshEvent.REFRESH) {
            if (event.getSourceType() == RefreshEvent.MINE) {
                if (UserData.getInstance().isLogIn()) {
                    tvLogInState.setText(getString(R.string.mine_exit));
                    llSettingExit.setVisibility(View.VISIBLE);
                } else {
                    tvLogInState.setText(getString(R.string.register_login));
                }
            }
        }
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        if (UserData.getInstance().isLogIn()) {
            tvLogInState.setText(getString(R.string.mine_exit));
            llSettingExit.setVisibility(View.VISIBLE);
        } else {
            tvLogInState.setText(getString(R.string.register_login));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.iv_setting_back, R.id.ll_setting_feedback, R.id.ll_setting_about, R.id.ll_setting_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting_back:
                finishCurrentActivity();
                break;
            case R.id.ll_setting_feedback:
                FeedBackDialog feedBackDialog = new FeedBackDialog(this);
                feedBackDialog.show();
                break;
            case R.id.ll_setting_about:
                Intent intent_about = new Intent(this, AboutActivity.class);
                startActivity(intent_about);
                break;
            case R.id.ll_setting_exit:
                if (tvLogInState.getText().toString().equalsIgnoreCase(getString(R.string.register_login))) {
                    startActivity(new Intent(this, RegisterActivity.class));
                } else {
                    ExitDialog exitDialog = new ExitDialog(this);
                    exitDialog.show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
