package com.leyou.game.adapter.fight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.dao.SearchHistory;
import com.leyou.game.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 游戏排行分类数据适配器
 *
 * @author : rocky
 * @Create Time : 2017/9/21 上午11:36
 * @Modified Time : 2017/9/21 上午11:36
 */
public class GameSearchHistoryAdapter extends RecyclerView.Adapter<GameSearchHistoryAdapter.ViewHolder> {
    private static final String TAG = "GameRankClassifyAdapter";
    private Context context;
    private List<SearchHistory> list;

    public GameSearchHistoryAdapter(Context context, List<SearchHistory> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_fight_game_rank_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogUtil.d(TAG, "----" + position);
        holder.ivGameLogo.setImageResource(R.mipmap.icon_default);
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void refreshAdapterData(List<SearchHistory> listInfo) {
        list.clear();
        list.addAll(listInfo);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_logo)
        SimpleDraweeView ivGameLogo;
        @BindView(R.id.btn_play_game)
        Button btnPlayGame;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;
        @BindView(R.id.iv_game_new)
        ImageView ivGameNew;
        @BindView(R.id.iv_game_hot)
        ImageView ivGameHot;
        @BindView(R.id.tv_game_type_count)
        TextView tvGameTypeCount;
        @BindView(R.id.tv_game_description)
        TextView tvGameDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
