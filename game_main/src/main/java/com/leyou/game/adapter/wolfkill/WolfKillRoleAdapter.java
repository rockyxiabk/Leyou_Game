package com.leyou.game.adapter.wolfkill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.WolfRoleBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/7/16 下午4:38
 * @Modified Time : 2017/7/16 下午4:38
 */
public class WolfKillRoleAdapter extends RecyclerView.Adapter<WolfKillRoleAdapter.ViewHolder> {

    private final Context context;
    private final List<WolfRoleBean> list;

    public WolfKillRoleAdapter(Context context, List<WolfRoleBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_wolf_kill_role_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WolfRoleBean wolfRoleBean = list.get(position);
        holder.ivWolfKillRoleImg.setImageURI(wolfRoleBean.rolePicture);
        holder.tvWolfKillRolePercent.setText(wolfRoleBean.winRate);
    }

    public void setAdapter(List<WolfRoleBean> data) {
        if (null != data && data.size() > 0) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_wolf_kill_role_img)
        SimpleDraweeView ivWolfKillRoleImg;
        @BindView(R.id.tv_wolf_kill_role_percent)
        TextView tvWolfKillRolePercent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
