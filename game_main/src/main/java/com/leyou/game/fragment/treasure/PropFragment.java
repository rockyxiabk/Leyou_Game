package com.leyou.game.fragment.treasure;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.treasure.PropAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.ipresenter.treasure.IPropFragment;
import com.leyou.game.presenter.treasure.PropFragmentPresenter;
import com.leyou.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description : 道具商城
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午10:54
 * @Modified By: rocky
 * @Modified Time : 2017/6/30 上午10:54
 */
public class PropFragment extends BaseFragment implements IPropFragment, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.recycler_prop)
    RecyclerView recycler;
    @BindView(R.id.swipeRefresh_prop)
    SwipeRefreshLayout swipeRefresh;
    Unbinder unbinder;
    private PropFragmentPresenter presenter;
    private List<PropBean> list = new ArrayList<>();
    private PropAdapter adapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (null != presenter)
                presenter.getShopProp();
        }
    }

    public PropFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_prop;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(this.getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);

        adapter = new PropAdapter(context, list);
        int SCREEN_WIDTH = ScreenUtil.getInstance(context).getScreenWidth();
        int number;
        if (SCREEN_WIDTH > 540) {
            number = Constants.TREASURE_THREE_720;
        } else {
            number = Constants.TREASURE_THREE_480;
        }
        recycler.setLayoutManager(new GridLayoutManager(context, number));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new PropFragmentPresenter(context, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        presenter.getShopProp();
    }

    @Override
    public void showWorkerPopUpWindow() {

    }

    @Override
    public void showPropInfo(PropBean propBean) {

    }

    @Override
    public void showWorkerData(List<WorkerBean> data) {

    }

    @Override
    public void hiddenWorkerPopUpWindow() {

    }

    @Override
    public void showPropListData(List<PropBean> data) {
        swipeRefresh.setRefreshing(false);
        list.clear();
        if (null != data && data.size() > 0) {
            list.addAll(data);
        }
        adapter.setListAdapter(data);
    }

    @Override
    public void showRefreshTreasure(int type, boolean flag) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void changeLoadingDes(String des) {

    }

    @Override
    public void dismissedLoading() {

    }

    @Override
    public void showMessageToast(String msg) {

    }

    @Override
    public void raceTreasure(TreasureBean treasureBean) {

    }
}
