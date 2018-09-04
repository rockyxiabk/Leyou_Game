package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.adapter.WinResultFragmentAdapter;
import com.leyou.game.base.BaseFragmentActivity;
import com.leyou.game.fragment.mine.TradeNoteFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 交易记录页面
 *
 * @author : rocky
 * @Create Time : 2017/5/2 下午2:28
 * @Modified By: rocky
 * @Modified Time : 2017/5/2 下午2:28
 */
public class TradeNoteActivity extends BaseFragmentActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.iv_trade_note_back)
    ImageView ivTradeNoteBack;
    @BindView(R.id.tab_layout_trade_note)
    TabLayout tabLayout;
    @BindView(R.id.vp_trade_note)
    ViewPager viewPager;
    private List<TradeNoteFragment> fragmentList = new ArrayList<>();

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_trade_note;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.trade_buy)));
        fragmentList.add(TradeNoteFragment.newInstance(1));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.trade_sale)));
        fragmentList.add(TradeNoteFragment.newInstance(0));
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new WinResultFragmentAdapter(fragmentList, getSupportFragmentManager()));
    }

    @Override
    public void initPresenter() {

    }

    @OnClick(R.id.iv_trade_note_back)
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
