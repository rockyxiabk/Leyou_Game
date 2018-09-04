package com.leyou.game.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.PlayGameActivity;
import com.leyou.game.adapter.win.GameAwardAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.win.WinGameAwardBean;
import com.leyou.game.event.RefreshWinAwardEvent;
import com.leyou.game.ipresenter.win.IWinAwardFragment;
import com.leyou.game.presenter.win.WinAwardFragmentPresenter;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.GameApi;
import com.leyou.game.widget.dialog.WebViewDialog;
import com.leyou.game.widget.dialog.winaward.GameRankDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;

/**
 * Description : 单个游戏 赢大奖
 *
 * @author : rocky
 * @Create Time : 2017/11/10 下午8:39
 * @Modified Time : 2017/11/10 下午8:39
 */
public class WinAwardFragment extends BaseFragment implements IWinAwardFragment {
    private static final String TAG = "WinAwardFragment";
    @BindView(R.id.ivbtn_win_award_explain)
    ImageButton ivbtnWinAwardExplain;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.iv_game_pic)
    SimpleDraweeView ivGamePic;
    @BindView(R.id.tv_game_score)
    TextView tvGameScore;
    @BindView(R.id.tv_game_rank)
    TextView tvGameRank;
    @BindView(R.id.btn_start_game)
    Button btnStartGame;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.iv_layout_error)
    ImageView ivLayoutError;
    @BindView(R.id.re_load_try)
    RelativeLayout reLoadTry;
    Unbinder unbinder;
    private WinAwardFragmentPresenter presenter;
    private List<WinGameAwardBean> list = new ArrayList<>();
    private GameAwardAdapter adapter;
    private GameBean gameBean = new GameBean();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserData(RefreshWinAwardEvent event) {
        if (event.getEvent() == RefreshWinAwardEvent.REFRESH) {
            if (null != presenter)
                presenter.request();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && null != presenter) {
            presenter.request();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_win_award_fragment;
    }

    @Override
    protected void initView(View rootView, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);

        adapter = new GameAwardAdapter(context, list);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new WinAwardFragmentPresenter(context, this);
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
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ivbtn_win_award_explain, R.id.iv_game_pic, R.id.btn_start_game, R.id.tv_game_rank, R.id.iv_layout_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivbtn_win_award_explain:
                WebViewDialog webViewDialog = new WebViewDialog(context, getString(R.string.game_explain), Constants.WIN_AWARD_EXPLAIN);
                webViewDialog.show();
                break;
            case R.id.tv_game_rank:
                GameRankDialog rankDialog = new GameRankDialog(context, gameBean);
                rankDialog.show();
                break;
            case R.id.iv_game_pic:
            case R.id.btn_start_game:
                Intent intent = new Intent(context, PlayGameActivity.class);
                intent.putExtra("game", gameBean);
                context.startActivity(intent);
                break;
            case R.id.iv_layout_error:
                presenter.request();
                break;
        }
    }

    @Override
    public void showView(boolean isError) {
        if (!isError) {
            nestedScrollView.setVisibility(View.GONE);
            reLoadTry.setVisibility(View.VISIBLE);
        } else {
            nestedScrollView.setVisibility(View.VISIBLE);
            reLoadTry.setVisibility(View.GONE);
        }
    }

    @Override
    public void showGameList(List<GameBean> gameBeenList) {
        if (null != gameBeenList && gameBeenList.size() > 0) {
            gameBean = gameBeenList.get(0);
            adapterPicture(gameBean.bannerUrl);
            tvGameScore.setText("当前成绩：" + String.valueOf(gameBean.score) + "");
            getDataList();
        } else {
            showView(false);
        }
    }

    private void adapterPicture(String bannerUrl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(ScreenUtil.dp2px(context, 10f));
            ivGamePic.getHierarchy().setRoundingParams(roundingParams);
        } else {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(ScreenUtil.dp2px(context, 5f));
            ivGamePic.getHierarchy().setRoundingParams(roundingParams);
        }
        ivGamePic.setImageURI(bannerUrl);
    }

    @Override
    public void showError(String error) {
        ToastUtils.showToastShort(error);
    }

    private void getDataList() {
        HttpUtil.subscribe(HttpUtil.createApi(GameApi.class, Constants.URL).getGameBonus(gameBean.uniqueMark), new Observer<ResultArray<WinGameAwardBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                recycler.setVisibility(View.GONE);
            }

            @Override
            public void onNext(ResultArray<WinGameAwardBean> winGameAwardBeanResultArray) {
                if (winGameAwardBeanResultArray.result == 1) {
                    List<WinGameAwardBean> data = winGameAwardBeanResultArray.data;
                    if (null != data) {
                        recycler.setVisibility(View.VISIBLE);
                        adapter.loadMoreData(data);
                    } else {
                        recycler.setVisibility(View.GONE);
                    }
                } else {
                    recycler.setVisibility(View.GONE);
                }
            }
        });
    }
}
