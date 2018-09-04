package com.leyou.game.adapter.fight;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.activity.wolfkill.WolfKillActivity;
import com.leyou.game.bean.FightGameBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/7/14 下午4:38
 * @Modified Time : 2017/7/14 下午4:38
 */
public class FightGameAdapter extends RecyclerView.Adapter<FightGameAdapter.ViewHolder> {

    private Context context;
    private List<FightGameBean> list;

    public FightGameAdapter(Context context, List<FightGameBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_fight_list);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        FightGameBean fightGameBean = list.get(position);
        Glide.with(context).load(fightGameBean.pictureUrl).error(R.mipmap.icon_header_bg).into(holder.ivGameImg);
        holder.ivGameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, WolfKillActivity.class));
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

    public void setWorkerAdapter(List<FightGameBean> gameBeanList) {
        list.clear();
        list.addAll(gameBeanList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_game_img)
        ImageView ivGameImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
