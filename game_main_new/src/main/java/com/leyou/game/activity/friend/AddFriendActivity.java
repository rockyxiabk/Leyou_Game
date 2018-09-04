package com.leyou.game.activity.friend;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.WinResultFragmentAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.fragment.friend.AddCrowdFragment;
import com.leyou.game.fragment.friend.AddFriendFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 添加好友页面-主要包含推荐好友和搜索跳转
 *
 * @author : rocky
 * @Create Time : 2017/12/5 下午7:56
 * @Modified By: rocky
 * @Modified Time : 2017/12/5 下午7:56
 */
public class AddFriendActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "AddFriendActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.re_toolbar)
    RelativeLayout reToolbar;
    @BindView(R.id.tab_layout_trade_note)
    TabLayout tabLayout;
    @BindView(R.id.vp_trade_note)
    ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.friend_search_find_friend)));
        fragmentList.add(new AddFriendFragment());
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.friend_search_find_crowd)));
        fragmentList.add(new AddCrowdFragment());
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new WinResultFragmentAdapter(fragmentList, getSupportFragmentManager()));
    }

    @Override
    public void initPresenter() {
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
        }
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
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
