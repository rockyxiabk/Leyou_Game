package com.leyou.game.adapter.game;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.PlayGameActivity;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.widget.dialog.LogInDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 游戏页面推荐的游戏
 *
 * @author : rocky
 * @Create Time : 2017/8/12 上午10:48
 * @Modified Time : 2017/8/12 上午10:48
 */
public class RecommendGameAdapter extends RecyclerView.Adapter<RecommendGameAdapter.ViewHolder> {

    private Context context;
    private List<GameBean> list;
    private CustomItemClickListener listener;

    public RecommendGameAdapter(Context context, List<GameBean> gameList, CustomItemClickListener listener) {
        this.context = context;
        this.list = gameList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_recommend_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final GameBean gameBean = list.get(position);
        holder.ivGameIcon.setImageURI(gameBean.iconUrl);
        holder.tvGameName.setText(gameBean.name);
        holder.ivGameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserData.getInstance().isLogIn()) {
                    listener.onItemClick(null, position);
                } else {
                    new LogInDialog(context, false).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void setAdapterData(List<GameBean> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_icon)
        SimpleDraweeView ivGameIcon;
        @BindView(R.id.tv_game_name)
        TextView tvGameName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
