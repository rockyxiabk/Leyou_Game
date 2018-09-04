package com.leyou.game.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.adapter.mine.GameDetailImageAdapter;
import com.leyou.game.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 游戏详情页面
 *
 * @author : rocky
 * @Create Time : 2017/5/18 上午10:04
 * @Modified By: rocky
 * @Modified Time : 2017/5/18 上午10:04
 */

public class GameDetailActivity extends BaseActivity {

    @BindView(R.id.iv_game_detail_back)
    ImageView ivGameDetailBack;
    @BindView(R.id.iv_game_logo)
    SimpleDraweeView ivGameLogo;
    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.rb_game_score)
    RatingBar rbGameScore;
    @BindView(R.id.tv_game_downloadTime)
    TextView tvGameDownloadTime;
    @BindView(R.id.recycler_game_pic)
    RecyclerView recyclerGamePic;
    @BindView(R.id.tv_recommend_editor)
    TextView tvRecommendEditor;
    @BindView(R.id.tv_recommend_content)
    TextView tvRecommendContent;
    @BindView(R.id.tv_recommend_show)
    TextView tvRecommendShow;
    @BindView(R.id.tv_award_explain)
    TextView tvAwardExplain;
    @BindView(R.id.tv_award_content)
    TextView tvAwardContent;
    @BindView(R.id.tv_award_show)
    TextView tvAwardShow;
    @BindView(R.id.tv_game_explain)
    TextView tvGameExplain;
    @BindView(R.id.tv_game_explain_content)
    TextView tvGameExplainContent;
    @BindView(R.id.tv_game_show)
    TextView tvGameShow;
    @BindView(R.id.btn_start_game)
    Button btnStartGame;
    private List<String> imageList = new ArrayList<>();
    private GameDetailImageAdapter adapter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_game_detail;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        recyclerGamePic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GameDetailImageAdapter(this, imageList);
        recyclerGamePic.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {

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

    @OnClick({R.id.iv_game_detail_back, R.id.tv_recommend_show, R.id.tv_award_show, R.id.tv_game_show, R.id.btn_start_game})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_game_detail_back:
                finishCurrentActivity();
                break;
            case R.id.tv_recommend_show:
                if (tvRecommendShow.getText().toString().equals("展开")) {
                    tvRecommendContent.setMaxLines(Integer.MAX_VALUE);
                    tvRecommendShow.setText("收起");
                } else if (tvRecommendShow.getText().toString().equals("收起")) {
                    tvRecommendContent.setLines(3);
                    tvRecommendShow.setText("展开");
                }
                break;
            case R.id.tv_award_show:
                if (tvAwardShow.getText().toString().equals("展开")) {
                    tvAwardContent.setMaxLines(Integer.MAX_VALUE);
                    tvAwardShow.setText("收起");
                } else if (tvAwardShow.getText().toString().equals("收起")) {
                    tvAwardContent.setLines(3);
                    tvAwardShow.setText("展开");
                }
                break;
            case R.id.tv_game_show:
                if (tvGameShow.getText().toString().equals("展开")) {
                    tvGameExplainContent.setMaxLines(Integer.MAX_VALUE);
                    tvGameShow.setText("收起");
                } else if (tvGameShow.getText().toString().equals("收起")) {
                    tvGameExplainContent.setLines(3);
                    tvGameShow.setText("展开");
                }
                break;
            case R.id.btn_start_game:
                break;
        }
    }
}
