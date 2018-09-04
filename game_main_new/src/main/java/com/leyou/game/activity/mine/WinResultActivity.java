package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.adapter.WinResultFragmentAdapter;
import com.leyou.game.base.BaseFragmentActivity;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.fragment.mine.WinResultFragment;
import com.leyou.game.ipresenter.mine.IWinResultActivity;
import com.leyou.game.presenter.mine.WinResultPresenter;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 上期获奖结果名单
 *
 * @author : rocky
 * @Create Time : 2017/4/18 上午10:36
 * @Modified By: rocky
 * @Modified Time : 2017/4/18 上午10:36
 */
public class WinResultActivity extends BaseFragmentActivity implements IWinResultActivity, TabLayout.OnTabSelectedListener {

    @BindView(R.id.iv_win_result_back)
    ImageView ivWinResultBack;
    @BindView(R.id.tab_layout_win_result)
    TabLayout tabLayout;
    @BindView(R.id.vp_win_result)
    ViewPager viewPager;
    private List<GameBean> list = new ArrayList<>();
    private List<WinResultFragment> fragmentList = new ArrayList<>();
    private WinResultPresenter presenter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_win_result;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    public void initPresenter() {
        presenter = new WinResultPresenter(this, this);
    }

    @OnClick(R.id.iv_win_result_back)
    public void onViewClicked() {
        finishCurrentActivity();
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
    public void showData(List<GameBean> listTab) {
        list.clear();
        fragmentList.clear();
        list.addAll(listTab);
        setFragment();
    }

    @Override
    public void showError() {
        ToastUtils.showToastShort("未知错误");
    }

    private void setFragment() {
        for (int i = 0; i < list.size(); i++) {
            GameBean gamePrizeResult = list.get(i);
            fragmentList.add(WinResultFragment.newInstance(gamePrizeResult.name, gamePrizeResult.uniqueMark));
            tabLayout.addTab(tabLayout.newTab().setText(gamePrizeResult.name));
        }
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new WinResultFragmentAdapter(fragmentList, getSupportFragmentManager()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
