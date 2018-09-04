package com.leyou.game.activity.wolfkill;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.wolfkill.WolfKillMilitaryAdapter;
import com.leyou.game.adapter.wolfkill.WolfKillRoleAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.WolfKillMilitaryBean;
import com.leyou.game.bean.WolfRoleBean;
import com.leyou.game.ipresenter.fight.IWolfKillScore;
import com.leyou.game.presenter.game.WolfKillScorePresenter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 狼人杀战绩
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午1:02
 * @Modified By: rocky
 * @Modified Time : 2017/7/15 下午1:02
 */
public class WolfKillScoreActivity extends BaseActivity implements IWolfKillScore, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "WolfKillScoreActivity";
    @BindView(R.id.iv_wolf_score_back)
    ImageView ivWolfScoreBack;
    @BindView(R.id.recycler_my_role)
    RecyclerView recyclerMyRole;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.re_no_data)
    RelativeLayout reNoData;
    private WolfKillScorePresenter presenter;
    private List<WolfKillMilitaryBean> list = new ArrayList<>();
    private WolfKillMilitaryAdapter militaryAdapter;
    private List<WolfRoleBean> roleList = new ArrayList<>();
    private WolfKillRoleAdapter roleAdapter;
    private boolean isLoading;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_wolf_kill_score;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        roleAdapter = new WolfKillRoleAdapter(this, roleList);
        recyclerMyRole.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerMyRole.setAdapter(roleAdapter);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(this.getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);
        militaryAdapter = new WolfKillMilitaryAdapter(this, list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == militaryAdapter.getItemCount()) {
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                presenter.loadMoreMilitary();
                                isLoading = false;
                            }
                        }, 10);
                    }
                }
            }
        });
        recycler.setAdapter(militaryAdapter);
    }

    @Override
    public void initPresenter() {
        presenter = new WolfKillScorePresenter(this, this);
    }

    @OnClick(R.id.iv_wolf_score_back)
    public void onViewClicked() {
        finishCurrentActivity();
    }

    @Override
    public void onRefresh() {
        militaryAdapter.setLoadAllData(false);
        presenter.getMilitaryList();
    }

    @Override
    public void showWolfRolePercent(List<WolfRoleBean> data) {
        roleAdapter.setAdapter(data);
    }

    @Override
    public void refreshFightResultList(List<WolfKillMilitaryBean> data) {
        swipeRefresh.setRefreshing(false);
        if (null != data && data.size() > 0) {
            reNoData.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.VISIBLE);
            militaryAdapter.updateAdapter(data);
        } else {
            swipeRefresh.setVisibility(View.GONE);
            reNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreFightResultList(List<WolfKillMilitaryBean> data) {
        swipeRefresh.setRefreshing(false);
        militaryAdapter.addAdapter(data);
    }

    @Override
    public void setMilitaryIsLoadAll(boolean flag) {
        militaryAdapter.setLoadAllData(flag);
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
