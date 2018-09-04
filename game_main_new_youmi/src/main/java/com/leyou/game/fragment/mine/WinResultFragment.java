package com.leyou.game.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.WinResultAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.ipresenter.mine.IWinResultFragmentPresenter;
import com.leyou.game.presenter.mine.WinResultFragmentPresenter;
import com.leyou.game.util.SpaceItemDecoration;
import com.leyou.game.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description : 上期获奖名单 游戏获奖列表
 *
 * @author : rocky
 * @Create Time : 2017/4/21 下午2:02
 * @Modified By: rocky
 * @Modified Time : 2017/4/21 下午2:02
 */
public class WinResultFragment extends BaseFragment implements IWinResultFragmentPresenter {
    private static final String TAG = "WinResultFragment";
    private final static String TITLE = "title";
    private final static String MARK = "uniqueMark";
    @BindView(R.id.recycler_win_result)
    RecyclerView recyclerWinResult;
    @BindView(R.id.re_win_result_error)
    RelativeLayout reWinResultError;
    Unbinder unbinder;
    private List<GameWinPriseBean> list = new ArrayList<>();
    private WinResultAdapter adapter;
    private WinResultFragmentPresenter presenter;

    public WinResultFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public static WinResultFragment newInstance(String title, String uniqueMark) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MARK, uniqueMark);
        WinResultFragment fragment = new WinResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_win_result;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        //添加适配器
        adapter = new WinResultAdapter(getContext(), list);
        recyclerWinResult.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerWinResult.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new WinResultFragmentPresenter(getContext(), this, getArguments().getString(MARK));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        unbinder.unbind();
    }

    @Override
    public void showData(List<GameWinPriseBean> resultList) {
        reWinResultError.setVisibility(View.GONE);
        list.clear();
        list.addAll(resultList);
        adapter.refreshAdapterData(resultList);
    }

    @Override
    public void showError() {
        if (null == list || list.size() == 0) {
            reWinResultError.setVisibility(View.VISIBLE);
        }
        ToastUtils.showToastShort(getString(R.string.net_error));
    }

    @OnClick(R.id.re_win_result_error)
    public void onViewClicked() {
        presenter.getWinResultPersonList(getArguments().getString(MARK));
    }
}
