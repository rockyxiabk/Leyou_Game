package com.leyou.game.activity.treasure;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.adapter.WinResultFragmentAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.fragment.MyPropFragment;
import com.leyou.game.fragment.treasure.PropFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.iv_prop_back)
    ImageView ivPropBack;
    @BindView(R.id.tab_layout_prop)
    TabLayout tabLayout;
    @BindView(R.id.vp_prop)
    ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_prop;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.treasure_prop_mine)));
        fragmentList.add(new MyPropFragment());
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.treasure_prop_buy)));
        fragmentList.add(new PropFragment());
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new WinResultFragmentAdapter(fragmentList, getSupportFragmentManager()));
        viewPager.setCurrentItem(1);
    }

    @Override
    public void initPresenter() {

    }

    @OnClick(R.id.iv_prop_back)
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
}
