package com.leyou.game.activity.treasure;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.WinResultFragmentAdapter;
import com.leyou.game.base.BaseFragmentActivity;
import com.leyou.game.fragment.treasure.WorkerComposeFragment;
import com.leyou.game.widget.dialog.WebViewDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 合成矿工页面
 *
 * @author : rocky
 * @Create Time : 2017/5/3 上午10:48
 * @Modified By: rocky
 * @Modified Time : 2017/5/3 上午10:48
 */
public class WorkerComposeActivity extends BaseFragmentActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.iv_composed_back)
    ImageView ivComposedBack;
    @BindView(R.id.tv_composed_explain)
    TextView tvComposedExplain;
    @BindView(R.id.tab_layout_composed)
    TabLayout tabLayout;
    @BindView(R.id.vp_composed)
    ViewPager viewPager;
    private List<WorkerComposeFragment> fragmentList = new ArrayList<>();
    private String[] workerType;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_worker_compose;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        workerType = getResources().getStringArray(R.array.worker_array);
        tabLayout.setOnTabSelectedListener(this);
        for (int i = 0; i < workerType.length; i++) {
            fragmentList.add(WorkerComposeFragment.newInstance(workerType[i], i + 2));
            tabLayout.addTab(tabLayout.newTab().setText(workerType[i]));
        }
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new WinResultFragmentAdapter(fragmentList, getSupportFragmentManager()));
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.iv_composed_back, R.id.tv_composed_explain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_composed_back:
                finishCurrentActivity();
                break;
            case R.id.tv_composed_explain:
                WebViewDialog webViewDialog = new WebViewDialog(this, getString(R.string.treasure_composed_explain), Constants.TREASURE_WORKER_COMPOSE);
                webViewDialog.show();
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
