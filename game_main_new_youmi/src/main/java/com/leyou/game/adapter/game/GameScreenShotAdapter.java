package com.leyou.game.adapter.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description :游戏详情页面截图适配器
 *
 * @author : rocky
 * @Create Time : 2017/10/22 上午11:48
 * @Modified Time : 2017/10/22 上午11:48
 */
public class GameScreenShotAdapter extends RecyclerView.Adapter<GameScreenShotAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public GameScreenShotAdapter(Context context, List<String> gameList) {
        this.context = context;
        this.list = gameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_screen_shot_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivGameScreenShot.setImageURI(list.get(position));
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void setAdapterData(List<String> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_screen_shot)
        SimpleDraweeView ivGameScreenShot;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
