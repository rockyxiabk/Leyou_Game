package com.leyou.game.activity.wolfkill;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.wolfkill.WolfKillPropAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.WolfPropBean;
import com.leyou.game.ipresenter.fight.IWolfKillProp;
import com.leyou.game.presenter.game.WolfKillPropActivityPresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 狼人杀 道具商城
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午1:02
 * @Modified By: rocky
 * @Modified Time : 2017/7/15 下午1:02
 */
public class WolfKillPropActivity extends BaseActivity implements IWolfKillProp, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.iv_wolf_prop_back)
    ImageView ivWolfPropBack;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    @BindView(R.id.re_load_try)
    RelativeLayout reLoadTry;
    private List<WolfPropBean> list = new ArrayList<>();
    private WolfKillPropAdapter adapter;
    private WolfKillPropActivityPresenter presenter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_wolf_kill_prop;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(this.getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);
        adapter = new WolfKillPropAdapter(this, list);
        int spanCount;
        if (ScreenUtil.getInstance(this).getScreenWidth() > 540) {
            spanCount = 3;
        } else {
            spanCount = 2;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {
        presenter = new WolfKillPropActivityPresenter(this, this);
    }

    @OnClick({R.id.iv_wolf_prop_back, R.id.iv_layout_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wolf_prop_back:
                finishCurrentActivity();
                break;
            case R.id.iv_layout_error:
                isShowNullListView(false);
                presenter.getPropList();
                break;
        }
    }

    @Override
    public void onRefresh() {
        presenter.getPropList();
    }

    @Override
    public void showPropList(List<WolfPropBean> data) {
        swipeRefresh.setRefreshing(false);
        adapter.setAdapter(data);
    }

    @Override
    public void isShowNullListView(boolean flag) {
        swipeRefresh.setRefreshing(false);
        if (flag) {
            swipeRefresh.setVisibility(View.GONE);
            reLoadTry.setVisibility(View.VISIBLE);
        } else {
            reLoadTry.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
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
}
