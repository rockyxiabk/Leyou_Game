package com.leyou.game.activity.friend;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.WinResultFragmentAdapter;
import com.leyou.game.base.BaseFragmentActivity;
import com.leyou.game.bean.GamePrizeResult;
import com.leyou.game.fragment.friend.FriendRankFragment;
import com.leyou.game.ipresenter.friend.IFriendRankActivity;
import com.leyou.game.presenter.friend.FriendRankActivityPresenter;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.WebViewDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 好友排行
 *
 * @author : rocky
 * @Create Time : 2017/7/23 下午5:12
 * @Modified By: rocky
 * @Modified Time : 2017/7/23 下午5:12
 */
public class FriendRankActivity extends BaseFragmentActivity implements IFriendRankActivity, TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_friend_rank)
    TabLayout tabLayout;
    @BindView(R.id.vp_friend_rank)
    ViewPager viewPager;
    private FriendRankActivityPresenter presenter;
    private List<GamePrizeResult> list = new ArrayList<>();
    private List<FriendRankFragment> fragmentList = new ArrayList<>();

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_friend_rank;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    public void initPresenter() {
        presenter = new FriendRankActivityPresenter(this, this);
    }

    @OnClick({R.id.iv_win_result_back, R.id.tv_friend_rank_explain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_win_result_back:
                finishCurrentActivity();
                break;
            case R.id.tv_friend_rank_explain:
                new WebViewDialog(this, "奖励说明", Constants.FRIEND_AWARD_EXPLAIN).show();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void showData(List<GamePrizeResult> listTab) {
        list.clear();
        fragmentList.clear();
        list.addAll(listTab);
        setFragment();
    }

    private void setFragment() {
        for (int i = 0; i < list.size(); i++) {
            GamePrizeResult gamePrizeResult = list.get(i);
            fragmentList.add(FriendRankFragment.newInstance(gamePrizeResult.name, gamePrizeResult.mark));
            tabLayout.addTab(tabLayout.newTab().setText(gamePrizeResult.name));
        }
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new WinResultFragmentAdapter(fragmentList, getSupportFragmentManager()));
    }


    @Override
    public void showError() {
        ToastUtils.showToastShort("未知错误");
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
        presenter.destroy();
    }
}
