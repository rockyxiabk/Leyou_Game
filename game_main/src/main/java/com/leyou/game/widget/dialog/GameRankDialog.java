package com.leyou.game.widget.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.win.GameRankAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.GameRankBean;
import com.leyou.game.ipresenter.win.IGameRankDialog;
import com.leyou.game.presenter.win.GameRankDialogPresenter;
import com.leyou.game.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 查看游戏排行榜
 *
 * @author : rocky
 * @Create Time : 2017/4/25 下午5:13
 * @Modified Time : 2017/4/25 下午5:13
 */
public class GameRankDialog extends BaseDialog implements IGameRankDialog {

    @BindView(R.id.iv_load_error)
    ImageView ivLoadError;
    @BindView(R.id.re_error_try)
    RelativeLayout reErrorTry;
    @BindView(R.id.iv_loading_progress)
    ImageView ivLoadingProgress;
    @BindView(R.id.re_loading)
    RelativeLayout reLoading;
    private Context context;
    private long markId;
    @BindView(R.id.iv_game_rank_close)
    ImageView ivGameRankClose;
    @BindView(R.id.recycler_game_rank)
    RecyclerView recyclerGameRank;
    private GameRankDialogPresenter presenter;
    private List<GameRankBean> list = new ArrayList<>();
    private GameRankAdapter adapter;
    private Animation loadAnimation;

    public GameRankDialog(Context context, long markId) {
        super(context);
        this.context = context;
        this.markId = markId;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_game_rank;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        loadAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim);

        adapter = new GameRankAdapter(context, list);
        recyclerGameRank.setLayoutManager(new LinearLayoutManager(context));
        recyclerGameRank.setAdapter(adapter);

        presenter = new GameRankDialogPresenter(context, this, markId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        presenter.unSubscribe();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showLoadingView() {
        ivLoadingProgress.startAnimation(loadAnimation);
        reLoading.setVisibility(View.VISIBLE);
        recyclerGameRank.setVisibility(View.GONE);
        reErrorTry.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        reErrorTry.setVisibility(View.VISIBLE);
        recyclerGameRank.setVisibility(View.GONE);
        reLoading.setVisibility(View.GONE);
    }

    @Override
    public void showListView() {
        recyclerGameRank.setVisibility(View.VISIBLE);
        reErrorTry.setVisibility(View.GONE);
        reLoading.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<GameRankBean> list) {
        if (recyclerGameRank.getVisibility() != View.VISIBLE) {
            showListView();
        }
        adapter.loadMoreData(list);
    }

    @Override
    public void showError(String error) {
        ToastUtils.showToastShort(error);
    }

    @OnClick({R.id.iv_game_rank_close, R.id.iv_load_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_game_rank_close:
                presenter.unSubscribe();
                dismiss();
                break;
            case R.id.iv_load_error:
                showLoadingView();
                presenter.getData();
                break;
        }
    }
}
