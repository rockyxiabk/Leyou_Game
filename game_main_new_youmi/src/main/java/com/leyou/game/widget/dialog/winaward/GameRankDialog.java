package com.leyou.game.widget.dialog.winaward;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.win.GameRankAdapter_;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.bean.game.GameRankBean;
import com.leyou.game.ipresenter.win.IGameRankDialog;
import com.leyou.game.presenter.win.GameRankDialogPresenter;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 查看游戏排行榜
 *
 * @author : rocky
 * @Create Time : 2017/10/20 下午2:44
 * @Modified By: rocky
 * @Modified Time : 2017/10/20 下午2:44
 */
public class GameRankDialog extends BaseDialog implements IGameRankDialog {
    private static final String TAG = "GameRankDialog";

    @BindView(R.id.tv_game_name)
    TextView tvGameName;
    @BindView(R.id.iv_rank_null)
    ImageView ivRankNull;
    private Context context;
    @BindView(R.id.iv_game_rank_close)
    ImageView ivGameRankClose;
    @BindView(R.id.recycler_game_rank)
    RecyclerView recyclerGameRank;

    private GameBean gameBean;
    private GameRankDialogPresenter presenter;
    private List<GameRankBean> list = new ArrayList<>();
    private GameRankAdapter_ adapter;
    private LoadingProgressDialog dialog;

    public GameRankDialog(Context context, GameBean gameBean) {
        super(context);
        this.context = context;
        this.gameBean = gameBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_game_rank_;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        tvGameName.setText(gameBean.name);
        adapter = new GameRankAdapter_(context, list);
        recyclerGameRank.setLayoutManager(new LinearLayoutManager(context));
        recyclerGameRank.setAdapter(adapter);

        presenter = new GameRankDialogPresenter(context, this, gameBean.uniqueMark);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        presenter.unSubscribe();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showLoadingView() {
        dialog = new LoadingProgressDialog(context, false);
        dialog.show();
        dialog.setLoadingText(context.getString(R.string.data_loading));
    }

    @Override
    public void showErrorView() {
        recyclerGameRank.setVisibility(View.GONE);
        ivRankNull.setVisibility(View.VISIBLE);
        dialog.dismiss();
    }

    @Override
    public void showListView() {
        recyclerGameRank.setVisibility(View.VISIBLE);
        ivRankNull.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<GameRankBean> list) {
        if (recyclerGameRank.getVisibility() != View.VISIBLE) {
            showListView();
        }
        dialog.dismiss();
        adapter.loadMoreData(list);
    }

    @Override
    public void showError(String error) {
        ToastUtils.showToastShort(error);
    }

    @OnClick({R.id.iv_game_rank_close, R.id.iv_rank_null})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_game_rank_close:
                presenter.unSubscribe();
                dismiss();
                break;
            case R.id.iv_rank_null:
                showLoadingView();
                presenter.getData();
                break;
        }
    }
}
