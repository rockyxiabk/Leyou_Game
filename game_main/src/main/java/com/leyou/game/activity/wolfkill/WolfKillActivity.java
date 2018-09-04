package com.leyou.game.activity.wolfkill;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.WebViewActivity;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WolfKillRoomBean;
import com.leyou.game.bean.WolfKillRoomInfoBean;
import com.leyou.game.bean.WolfRoleBean;
import com.leyou.game.ipresenter.fight.IWolfKillActivity;
import com.leyou.game.presenter.game.WolfKillActivityPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.WolfKillApi;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.fightdialog.CreateHomeDialog;
import com.leyou.game.widget.fightdialog.FindHomeDialog;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 狼人杀游戏
 *
 * @author : rocky
 * @Create Time : 2017/7/23 下午3:47
 * @Modified By: rocky
 * @Modified Time : 2017/7/23 下午3:47
 */
public class WolfKillActivity extends BaseActivity implements IWolfKillActivity {
    private static final String TAG = "WolfKillActivity";
    @BindView(R.id.iv_wolfKill_back)
    ImageView ivBack;
    @BindView(R.id.iv_fight_head)
    SimpleDraweeView ivFightHead;
    @BindView(R.id.tv_wolfKill_name)
    TextView tvWolfKillGame;
    @BindView(R.id.tv_fight_des)
    TextView tvFightDes;
    @BindView(R.id.re_fight_user)
    RelativeLayout reFightUser;
    @BindView(R.id.iv_fight_six)
    ImageView ivFightSix;
    @BindView(R.id.iv_fight_nine)
    ImageView ivFightNine;
    @BindView(R.id.iv_fight_twelve)
    ImageView ivFightTwelve;
    @BindView(R.id.iv_fight_create_home)
    ImageView ivFightCreateHome;
    @BindView(R.id.iv_fight_find_home)
    ImageView ivFightFindHome;
    @BindView(R.id.iv_fight_prop)
    ImageView ivFightProp;
    @BindView(R.id.iv_fight_game_rule)
    ImageView ivFightGameRule;
    private WolfKillRoomBean wolfKillRoomBean;
    private WolfKillActivityPresenter presenter;
    private LoadingProgressDialog progressDialog;
    private int currentType;

    @PermissionYes(10847)
    private void getPermissionYes(List<String> grantedPermissions) {
        autoJoinRoom(currentType);
    }

    @PermissionNo(10847)
    private void getPermissionNo(List<String> deniedPermissions) {
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_wolf_kill;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    public void initPresenter() {
        presenter = new WolfKillActivityPresenter(this, this);
    }

    @Override
    public void showLoading() {
        progressDialog = new LoadingProgressDialog(this, false);
        progressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != progressDialog) {
            progressDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setUserRote(int gameCount, String winRate) {
        tvFightDes.setText("总局数：  " + gameCount + "      胜率：  " + winRate);
        tvWolfKillGame.setText(UserData.getInstance().getNickname());
        ivFightHead.setImageURI(UserData.getInstance().getPictureUrl());
    }

    @OnClick({R.id.iv_wolfKill_back, R.id.re_fight_user, R.id.iv_fight_six, R.id.iv_fight_nine, R.id.iv_fight_twelve, R.id.iv_fight_create_home, R.id.iv_fight_find_home, R.id.iv_fight_prop, R.id.iv_fight_game_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wolfKill_back:
                finishCurrentActivity();
                break;
            case R.id.re_fight_user:
                startActivity(new Intent(this, WolfKillScoreActivity.class));
                break;
            case R.id.iv_fight_six:
                findJoinRoom(6);
                break;
            case R.id.iv_fight_nine:
                findJoinRoom(9);
                break;
            case R.id.iv_fight_twelve:
                findJoinRoom(12);
                break;
            case R.id.iv_fight_create_home:
                new CreateHomeDialog(this).show();
                break;
            case R.id.iv_fight_find_home:
                new FindHomeDialog(this).show();
                break;
            case R.id.iv_fight_prop:
                startActivity(new Intent(this, WolfKillPropActivity.class));
                break;
            case R.id.iv_fight_game_rule:
                Intent rule = new Intent(this, WebViewActivity.class);
                rule.putExtra("title", "狼人杀游戏规则");
                rule.putExtra("url", Constants.FIGHT_GAME_WOLF_KILL_RULE);
                rule.putExtra("type", 0);
                startActivity(rule);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    private void findJoinRoom(int type) {
        currentType = type;
        if (AndPermission.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {
            showLoading();
            changeLoadingDes("查找房间中...");
            presenter.autoJoinRoom(type);
        } else {
            AndPermission.with(this).requestCode(10847).callback(this).permission(Manifest.permission.RECORD_AUDIO).start();
        }
    }

    @Override
    public void autoJoinRoom(final long roomId) {
        if (roomId > 0) {
            changeLoadingDes("进入房间中...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissedLoading();
                    Intent intent = new Intent(WolfKillActivity.this, WolfKillFightActivity.class);
                    intent.putExtra("roomId", roomId);
                    startActivity(intent);
                }
            }, 1200);
        } else {
            changeLoadingDes("创建房间中...");
            presenter.createRoom(currentType);
        }
    }

    @Override
    public void createJoinRoom(final WolfKillRoomBean roomBean) {
        if (null != roomBean) {
            changeLoadingDes("进入房间中...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissedLoading();
                    Intent intent = new Intent(WolfKillActivity.this, WolfKillFightActivity.class);
                    intent.putExtra("roomId", roomBean.roomId);
                    startActivity(intent);
                }
            }, 1200);
        } else {
            dismissedLoading();
            ToastUtils.showToastShort("进入房间失败");
        }
    }
}
