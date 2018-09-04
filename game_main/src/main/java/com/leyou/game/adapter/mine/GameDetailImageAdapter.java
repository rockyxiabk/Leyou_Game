package com.leyou.game.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leyou.game.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/5/18 上午11:44
 * @Modified Time : 2017/5/18 上午11:44
 */
public class GameDetailImageAdapter extends RecyclerView.Adapter<GameDetailImageAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public GameDetailImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_game_detail_imgage, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivGameDetailImage.setImageResource(R.mipmap.induce_three);
    }

    @Override
    public int getItemCount() {
        int ret = 4;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_detail_image)
        ImageView ivGameDetailImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
