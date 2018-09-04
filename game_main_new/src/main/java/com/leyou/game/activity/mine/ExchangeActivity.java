package com.leyou.game.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.WinResultFragmentAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.fragment.mine.BuyFragment;
import com.leyou.game.fragment.mine.SaleFragment;
import com.leyou.game.ipresenter.mine.IExchangeActivity;
import com.leyou.game.presenter.mine.ExchangeActivityPresenter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 交易所页面
 *
 * @author : rocky
 * @Create Time : 2017/4/18 上午10:47
 * @Modified By: rocky
 * @Modified Time : 2017/4/18 上午10:47
 */
public class ExchangeActivity extends BaseActivity implements IExchangeActivity, TabLayout.OnTabSelectedListener {

    private static final String TAG = "ExchangeActivity";
    @BindView(R.id.iv_exchange_back)
    ImageView ivExchangeBack;
    @BindView(R.id.tv_exchange_type)
    TextView tv_exchange_type;
    @BindView(R.id.tab_layout_trade_note)
    TabLayout tabLayout;
    @BindView(R.id.vp_trade_note)
    ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private double minPrice;
    private double maxPrice;
    private ExchangeActivityPresenter presenter;
    private int exChangeType = 0;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_exchange;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exchange_my_buy)));
        fragmentList.add(new SaleFragment());
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exchange_my_sale)));
        fragmentList.add(new BuyFragment());
        tabLayout.setOnTabSelectedListener(this);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(new WinResultFragmentAdapter(fragmentList, getSupportFragmentManager()));

    }

    @Override
    public void initPresenter() {
        presenter = new ExchangeActivityPresenter(this, this);
    }

    @OnClick({R.id.iv_exchange_back, R.id.tv_exchange_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_exchange_back:
                finishCurrentActivity();
                break;
            case R.id.tv_exchange_type:
                DiamondBean diamondBean = new DiamondBean();
                diamondBean.minPrice = minPrice;
                diamondBean.maxPrice = maxPrice;
                if (exChangeType == 0) {
                    Intent intent = new Intent(this, PurchaseDiamondActivity.class);
                    intent.putExtra("diamondBean", diamondBean);
                    startActivity(intent);
                } else if (exChangeType == 1) {
                    Intent intent = new Intent(this, SaleDiamondActivity.class);
                    intent.putExtra("diamondBean", diamondBean);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 1) {
            exChangeType = 1;
            tv_exchange_type.setText(getString(R.string.exchange_sale));
        } else if (tab.getPosition() == 0) {
            exChangeType = 0;
            tv_exchange_type.setText(getString(R.string.exchange_buy));
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setCurrentPrice(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
