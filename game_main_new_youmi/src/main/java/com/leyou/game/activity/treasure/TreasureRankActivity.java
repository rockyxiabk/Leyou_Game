package com.leyou.game.activity.treasure;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
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
import pl.droidsonroids.gif.GifImageView;
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
    @BindView(R.id.re_top_rank)
    RelativeLayout reTopRank;
    @BindView(R.id.tv_user1_qiang_number)
    TextView tvUser1QiangNumber;
    @BindView(R.id.tv_user1_wa_number)
    TextView tvUser1WaNumber;
    @BindView(R.id.iv_user1_header)
    SimpleDraweeView ivUser1Header;
    @BindView(R.id.tv_user1_name)
    TextView tvUser1Name;
    @BindView(R.id.tv_user2_qiang_number)
    TextView tvUser2QiangNumber;
    @BindView(R.id.tv_user2_wa_number)
    TextView tvUser2WaNumber;
    @BindView(R.id.iv_user2_header)
    SimpleDraweeView ivUser2Header;
    @BindView(R.id.tv_user2_name)
    TextView tvUser2Name;
    @BindView(R.id.iv_vs_gif)
    GifImageView ivVsGif;
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
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.URL).getTreasureRank2(),
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
                            setTopViewData(data);
                        } else {
                            reTopRank.setVisibility(View.GONE);
                        }
                    }
                });
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.URL).getTreasureRank1(),
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

    private void setTopViewData(List<TreasureGainBean> topGainBean) {
        if (null != topGainBean && topGainBean.size() > 0) {
            if (topGainBean.size() == 1) {
                reTopRank.setVisibility(View.GONE);
            } else if (topGainBean.size() == 2) {
                TreasureGainBean gainBean1 = topGainBean.get(0);
                ivUser1Header.setImageURI(gainBean1.getHeadImgUrl());
                tvUser1Name.setText(gainBean1.getNickname());
                tvUser1QiangNumber.setText("" + gainBean1.getOccupyNum() + "");
                tvUser1WaNumber.setText("" + gainBean1.getFreeGaiNum() + "");

                TreasureGainBean gainBean2 = topGainBean.get(1);
                ivUser2Header.setImageURI(gainBean2.getHeadImgUrl());
                tvUser2Name.setText(gainBean2.getNickname());
                tvUser2QiangNumber.setText("" + gainBean2.getOccupyNum() + "");
                tvUser2WaNumber.setText("" + gainBean2.getFreeGaiNum() + "");
            } else {
                reTopRank.setVisibility(View.GONE);
            }
        }
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
        ivVsGif.setBackgroundResource(R.mipmap.icon_treasure_rank_vs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

}
