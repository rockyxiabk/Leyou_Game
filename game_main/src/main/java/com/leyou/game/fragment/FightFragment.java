package com.leyou.game.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.fight.FightGameAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.FightGameBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.ipresenter.fight.IFightGameFragment;
import com.leyou.game.presenter.game.FightFragmentPresenter;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LogInDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description : 对战页面
 *
 * @author : rocky
 * @Create Time : 2017/7/11 上午11:06
 * @Modified By: rocky
 * @Modified Time : 2017/7/11 上午11:06
 */
public class FightFragment extends BaseFragment implements IFightGameFragment, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FightFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recycler;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    @BindView(R.id.re_load_try)
    RelativeLayout reLoadTry;
    Unbinder unbinder;
    private List<FightGameBean> list = new ArrayList<>();
    private FightGameAdapter adapter;
    private FightFragmentPresenter presenter;
    private LogInDialog logInDialog;

    public FightFragment() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && UserData.getInstance().isLogIn()) {
            userLogInEd();
        } else if (getUserVisibleHint() && !UserData.getInstance().isLogIn()) {
            userUnLogIn();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_fight;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(this.getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);

        //添加适配器
        adapter = new FightGameAdapter(getContext(), list);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new FightFragmentPresenter(context, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        presenter.getFightData();
    }

    @Override
    public void showRefreshData(List<FightGameBean> data) {
        if (null != data && data.size() > 0) {
            list.clear();
            list.addAll(data);
            adapter.setWorkerAdapter(data);
        } else {
            isShowNullListView(true);
        }
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
    public void userUnLogIn() {
        logInDialog = new LogInDialog(context, false);
        logInDialog.show();
    }

    @Override
    public void userLogInEd() {
        if (null != logInDialog) {
            logInDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        ToastUtils.showToastShort(msg);
    }

    @OnClick(R.id.iv_layout_error)
    public void onViewClicked() {
        isShowNullListView(false);
        swipeRefresh.setRefreshing(true);
        presenter.getFightData();
    }
}
