package com.leyou.game.activity.treasure;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.treasure.TreasureRankAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.treasure.TreasureGainBean;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.TreasureApi;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 宝库排行
 *
 * @author : rocky
 * @Create Time : 2017/12/11 下午5:22
 * @Modified By: rocky
 * @Modified Time : 2017/12/11 下午5:22
 */
public class TreasureRankActivity extends BaseActivity {

    @BindView(R.id.iv_dismiss_back)
    ImageView ivDismissBack;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<TreasureGainBean> list = new ArrayList<>();
    private TreasureRankAdapter rankAdapter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_treasure_rank;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        rankAdapter = new TreasureRankAdapter(this, list);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(rankAdapter);

    }

    @Override
    public void initPresenter() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.URL).getTreasureRank(),
                new Observer<ResultArray<TreasureGainBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showToastShort("未查询到相关结果");
                    }

                    @Override
                    public void onNext(ResultArray<TreasureGainBean> resultArray) {
                        if (resultArray.result == 1) {
                            List<TreasureGainBean> data = resultArray.data;
                            rankAdapter.setAdapterData(data);
                        } else {
                            ToastUtils.showToastShort("未查询到相关结果");
                        }
                    }
                });
    }

    @OnClick({R.id.iv_dismiss_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_dismiss_back:
                finishCurrentActivity();
                break;
        }
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
