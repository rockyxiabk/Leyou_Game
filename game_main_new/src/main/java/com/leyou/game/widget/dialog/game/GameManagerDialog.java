package com.leyou.game.widget.dialog.game;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.game.RecommendGameAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.ResultArray;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.newapi.GameApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 游戏页面管理弹窗
 *
 * @author : rocky
 * @Create Time : 2017/11/18 下午5:56
 * @Modified Time : 2017/11/18 下午5:56
 */
public class GameManagerDialog extends BaseDialog implements CustomItemClickListener {
    private Context context;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_exit)
    LinearLayout llExit;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    private OnViewClickListener onViewClickListener;
    private List<GameBean> gameList;
    private RecommendGameAdapter adapter;

    public GameManagerDialog(Context context, List<GameBean> recommendList) {
        super(context);
        this.context = context;
        this.gameList = recommendList;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_game_manager;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        adapter = new RecommendGameAdapter(context, gameList, this);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler.setAdapter(adapter);
        recycler.setNestedScrollingEnabled(false);
    }

    @OnClick({R.id.ll_exit, R.id.ll_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_exit:
                onViewClickListener.exit();
                break;
            case R.id.ll_share:
                onViewClickListener.shareGame();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        onViewClickListener.startGame(gameList.get(position));
    }

    public interface OnViewClickListener {
        void startGame(GameBean gameBean);

        void shareGame();

        void exit();
    }
}
