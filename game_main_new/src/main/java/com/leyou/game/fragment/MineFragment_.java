package com.leyou.game.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.activity.WebViewActivity;
import com.leyou.game.activity.mine.AboutActivity;
import com.leyou.game.activity.mine.AwardListActivity;
import com.leyou.game.activity.mine.ConsumeNoteActivity;
import com.leyou.game.activity.mine.ExchangeActivity;
import com.leyou.game.activity.mine.FeedBackActivity;
import com.leyou.game.activity.mine.LogInWebActivity;
import com.leyou.game.activity.mine.ModifyUserInfoActivity;
import com.leyou.game.activity.mine.SignActivity;
import com.leyou.game.activity.mine.UptoActivity;
import com.leyou.game.activity.mine.WinResultActivity;
import com.leyou.game.activity.mine.WithCashApplyActivity;
import com.leyou.game.activity.mine.WithCashBindCardActivity;
import com.leyou.game.adapter.game.GameNewAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.event.PriceStateChangeEvent;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.event.SaleEvent;
import com.leyou.game.ipresenter.mine.IMineFragment;
import com.leyou.game.presenter.mine.MineFragmentPresenter;
import com.leyou.game.util.MobClickUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.DragPointView;
import com.leyou.game.widget.dialog.ExitDialog;
import com.leyou.game.widget.dialog.LogInDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description : 新版我的页面
 *
 * @author : rocky
 * @Create Time : 2017/10/17 下午6:42
 * @Modified By: rocky
 * @Modified Time : 2017/10/17 下午6:42
 */
public class MineFragment_ extends BaseFragment implements IMineFragment {
    private static final String TAG = "MineFragment_";

    @BindView(R.id.tv_title_mine)
    TextView tvTitleMine;
    @BindView(R.id.iv_user_head)
    SimpleDraweeView ivUserHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_wealth_diamond)
    TextView tvUserWealthDiamond;
    @BindView(R.id.tv_user_wealth_money)
    TextView tvUserWealthMoney;
    @BindView(R.id.tv_user_idNo)
    TextView tvUserIDNo;
    @BindView(R.id.tv_mine_setting)
    TextView tvMineSetting;
    @BindView(R.id.ll_upto)
    LinearLayout llUpto;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;
    @BindView(R.id.ll_exchange)
    LinearLayout llExchange;
    @BindView(R.id.ll_consume)
    LinearLayout llConsume;
    @BindView(R.id.ll_last_award)
    LinearLayout llLastAward;
    @BindView(R.id.ll_i_award)
    LinearLayout llIAward;
    @BindView(R.id.tv_point_tips)
    TextView tvPointTips;
    @BindView(R.id.ll_with_cash)
    LinearLayout llWithCash;
    @BindView(R.id.ll_apply_developer)
    LinearLayout llApplyDeveloper;
    @BindView(R.id.ll_feed_back)
    LinearLayout llFeedBack;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.mine_recycler_player)
    RecyclerView mineRecyclerPlayer;
    @BindView(R.id.card_game)
    CardView cardGame;
    @BindView(R.id.cd_logout)
    CardView cdLogout;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    Unbinder unbinder;
    private MineFragmentPresenter presenter;
    private List<GameBean> list = new ArrayList<>();
    private GameNewAdapter adapter;
    boolean flag = false;
    private LogInDialog logInDialog;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserData(RefreshEvent event) {
        if (event.getIsRefresh() == RefreshEvent.REFRESH) {
            if (event.getSourceType() == RefreshEvent.MINE) {
                presenter.getUserDate();
                presenter.getUserWealth();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sellDiamond(SaleEvent event) {
        if (event.getType() == 1) {
            presenter.getUserWealth();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void prizeStateChangeEvent(PriceStateChangeEvent event) {
        presenter.getPrizeDy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && null != presenter) {
            if (UserData.getInstance().isLogIn()) {
                userLogInEd();
                presenter.getUserDate();
                presenter.getUserWealth();
                presenter.getMyPlayedGame();
                presenter.getPrizeDy();
            } else {
                userUnLogIn();
            }
        }
    }

    public MineFragment_() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine_fragment_;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        //添加适配器
        adapter = new GameNewAdapter(getContext(), list);
        mineRecyclerPlayer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mineRecyclerPlayer.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new MineFragmentPresenter(context, this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick({R.id.iv_user_head, R.id.tv_user_name, R.id.tv_mine_setting, R.id.ll_upto, R.id.ll_sign, R.id.ll_exchange, R.id.ll_consume, R.id.ll_last_award, R.id.ll_i_award, R.id.ll_with_cash, R.id.ll_apply_developer, R.id.ll_feed_back, R.id.ll_about, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
            case R.id.tv_user_name:
            case R.id.tv_mine_setting:
                startOtherActivity(ModifyUserInfoActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_upto:
                startOtherActivity(UptoActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_sign:
                // TODO: 2017/11/1 有待修改
//                if (flag) {
//                    Intent rule = new Intent(context, WebViewActivity.class);
//                    rule.putExtra("title", context.getResources().getString(R.string.diamond_convert));
//                    rule.putExtra("url", Constants.DIAMOND_CONVERT_URL);
//                    rule.putExtra("type", 1);
//                    context.startActivity(rule);
//                } else {
                startOtherActivity(SignActivity.class, RegisterActivity.class);
//                }
                flag = !flag;
                break;
            case R.id.ll_exchange:
                MobClickUtil.mobEvent(context, MobClickUtil.EVENT_MINE_EXCHANGE);
                startOtherActivity(ExchangeActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_consume:
                startOtherActivity(ConsumeNoteActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_last_award:
                startOtherActivity(WinResultActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_i_award:
                startOtherActivity(AwardListActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_with_cash:
                if (UserData.getInstance().isBindCard()) {
                    startOtherActivity(WithCashApplyActivity.class, RegisterActivity.class);
                } else {
                    startOtherActivity(WithCashBindCardActivity.class, RegisterActivity.class);
                }
                break;
            case R.id.ll_apply_developer:
                startOtherActivity(LogInWebActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_feed_back:
                startOtherActivity(FeedBackActivity.class, RegisterActivity.class);
                break;
            case R.id.ll_about:
                startOtherActivity(AboutActivity.class, AboutActivity.class);
                break;
            case R.id.tv_logout:
                ExitDialog exitDialog = new ExitDialog(context);
                exitDialog.show();
                break;
        }
    }

    @Override
    public void showUserData(String name, String pictureUrl, String backgroundImage) {
        ivUserHead.setImageURI(pictureUrl);
        tvUserName.setText(name);
        tvUserIDNo.setText(context.getString(R.string.friend_id_no) + UserData.getInstance().getIDNo() + "");
        if (UserData.getInstance().isBindCard()) {
            presenter.getBankInfo();
        }
    }

    @Override
    public void showWealth(int diamondCount, double money) {
        tvUserWealthDiamond.setText("" + diamondCount + "钻石");
        tvUserWealthMoney.setText("" + money + "元");
    }

    @Override
    public void isLogIn(boolean flag) {
        UserData.getInstance().setLogIn(flag);
        if (flag) {
            cdLogout.setVisibility(View.VISIBLE);
        } else {
            cdLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPrizeDynamic(int num) {
        if (num > 0) {
            tvPointTips.setVisibility(View.VISIBLE);
            tvPointTips.setText(String.valueOf(num));
        } else {
            tvPointTips.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setMyPlayedList(List<GameBean> data) {
        if (null != data) {
            cardGame.setVisibility(View.VISIBLE);
            adapter.setAdapterData(data);
        } else {
            cardGame.setVisibility(View.GONE);
        }
    }

    @Override
    public void startOtherActivity(Class<?> cls, Class<?> unLogIn) {
        if (UserData.getInstance().isLogIn()) {
            Intent intent_user = new Intent(getContext(), cls);
            context.startActivity(intent_user);
        } else {
            Intent register = new Intent(getContext(), unLogIn);
            context.startActivity(register);
        }
    }

    public void userUnLogIn() {
        logInDialog = new LogInDialog(context, false);
        logInDialog.show();
    }

    public void userLogInEd() {
        if (null != logInDialog) {
            logInDialog.dismiss();
        }
    }
}
