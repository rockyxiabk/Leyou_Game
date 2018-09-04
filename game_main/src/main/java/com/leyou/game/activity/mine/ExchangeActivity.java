package com.leyou.game.activity.mine;

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
import com.leyou.game.fragment.BuyFragment;
import com.leyou.game.fragment.mine.SaleFragment;
import com.leyou.game.ipresenter.mine.IExchangeActivity;
import com.leyou.game.presenter.mine.ExchangeActivityPresenter;
import com.leyou.game.widget.dialog.PurchaseDialog;
import com.leyou.game.widget.dialog.SaleDiamondDialog;
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
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tab_layout_trade_note)
    TabLayout tabLayout;
    @BindView(R.id.vp_trade_note)
    ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private double minPrice;
    private double maxPrice;
    private ExchangeActivityPresenter presenter;


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

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exchange_sale)));
        fragmentList.add(new SaleFragment());
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.exchange_buy)));
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

    @OnClick({R.id.iv_exchange_back, R.id.tv_sale})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_exchange_back:
                finishCurrentActivity();
                break;
            case R.id.tv_sale:
                if (tvSale.getText().toString().equalsIgnoreCase(getString(R.string.exchange_my_buy))) {
                    PurchaseDialog purchaseDialog = new PurchaseDialog(this,minPrice,maxPrice);
                    purchaseDialog.show();
                } else if (tvSale.getText().toString().equalsIgnoreCase(getString(R.string.exchange_my_sale))) {
                    SaleDiamondDialog saleDiamondDialog = new SaleDiamondDialog(this,minPrice,maxPrice);
                    saleDiamondDialog.show();
                }
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            tvSale.setText(getString(R.string.exchange_my_sale));
        } else if (tab.getPosition() == 1) {
            tvSale.setText(getString(R.string.exchange_my_buy));
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

//    private void showNewPersonGuide() {
//
//        // 使用图片
//        final ImageView iv = new ImageView(this);
//        iv.setImageResource(R.mipmap.icon_arrow_exchange_buy);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        guideViewBuy = GuideView.Builder
//                .newInstance(this)
//                .setTargetView(view)//设置目标
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.BOTTOM)
//                .setOnclickExit(true)
//                .setShape(GuideView.MyShape.RECTANGULAR)   // 设置圆形显示区域，
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewBuy.hide();
//                        viewPager.setCurrentItem(1);
//                        tvSale.setText(getString(R.string.exchange_my_buy));
//                    }
//                })
//                .build();
//        boolean isDiamond = SPUtil.getBoolean(this, SPUtil.INDUCE, "exchange");
//        boolean isNewPerson = SPUtil.getBoolean(this, SPUtil.INDUCE, "isNewPerson");
//        if (!isDiamond && isNewPerson) {
//            SPUtil.setBoolean(this, SPUtil.INDUCE, "exchange", true);
//            guideViewBuy.show();
//        }
//    }

    @Override
    public void setCurrentPrice(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
