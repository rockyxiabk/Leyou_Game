package com.leyou.game.adapter.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.bean.game.GameBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/12 上午10:48
 * @Modified Time : 2017/8/12 上午10:48
 */
public class CategoryGameAdapter extends RecyclerView.Adapter<CategoryGameAdapter.ViewHolder> {
    private Context context;
    private List<GameBean> list;

    public CategoryGameAdapter(Context context, List<GameBean> gameList) {
        this.context = context;
        this.list = gameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_category_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GameBean gameBean = list.get(position);
        Glide.with(context).load(gameBean.imgUrl).into(holder.ivGameImg);
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
        @BindView(R.id.iv_category_banner)
        ImageView ivGameImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
