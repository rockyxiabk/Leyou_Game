package com.leyou.game.activity.game;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.adapter.game.CategoryGameAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.ipresenter.game.ICategory;
import com.leyou.game.presenter.game.CategoryPresenter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 游戏-更多游戏
 *
 * @author : rocky
 * @Create Time : 2017/10/31 下午9:32
 * @Modified By: rocky
 * @Modified Time : 2017/10/31 下午9:32
 */
public class CategoryGameActivity extends BaseActivity implements ICategory {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<GameBean> list = new ArrayList<>();
    private CategoryGameAdapter adapter;
    private CategoryPresenter presenter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_category_game;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        adapter = new CategoryGameAdapter(this, list);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {
        presenter = new CategoryPresenter(this, this);
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

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishCurrentActivity();
    }

    @Override
    public void showDataList(List<GameBean> dataList) {
        adapter.setAdapterData(dataList);
    }

    @Override
    public void showMessage(String msg) {

    }
}
