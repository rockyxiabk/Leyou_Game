package com.leyou.game.adapter.wolfkill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.bean.WolfPropBean;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.fightdialog.RolePropBuyDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/7/16 下午5:02
 * @Modified Time : 2017/7/16 下午5:02
 */
public class WolfKillPropAdapter extends RecyclerView.Adapter<WolfKillPropAdapter.ViewHolder> {

    private Context context;
    private List<WolfPropBean> list;

    public WolfKillPropAdapter(Context context, List<WolfPropBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_wolf_kill_prop_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final WolfPropBean wolfPropBean = list.get(position);
        Glide.with(context).load(wolfPropBean.pictureUrl).into(holder.ivWolfPropTypeImg);
        holder.tvWolfPropType.setText(wolfPropBean.name);
        holder.tvWolfPropDiamond.setText(wolfPropBean.price + "");
        holder.reItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RolePropBuyDialog(context, wolfPropBean).show();
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

    public void setAdapter(List<WolfPropBean> currentList) {
        list.clear();
        list.addAll(currentList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_wolf_kill_prop_item)
        LinearLayout reItem;
        @BindView(R.id.iv_wolf_prop_type_img)
        ImageView ivWolfPropTypeImg;
        @BindView(R.id.tv_wolf_prop_type)
        TextView tvWolfPropType;
        @BindView(R.id.tv_wolf_prop_diamond)
        TextView tvWolfPropDiamond;
        @BindView(R.id.ll_diamond)
        LinearLayout llDiamond;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
