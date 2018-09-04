package com.leyou.game.adapter.wolfkill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.WolfPropBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.wolfkill
 *
 * @author : rocky
 * @Create Time : 2017/9/5 下午6:48
 * @Modified Time : 2017/9/5 下午6:48
 */
public class ChoosePropAdapter extends RecyclerView.Adapter<ChoosePropAdapter.ViewHolder> {

    private final Context context;
    private final List<WolfPropBean> list;
    private CustomItemClickListener listener;

    public ChoosePropAdapter(Context context, List<WolfPropBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setCustomItemClick(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_prop_role_choose, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        WolfPropBean wolfPropBean = list.get(position);
        Glide.with(context).load(wolfPropBean.pictureUrl).into(holder.ivPropImg);
        holder.tvPropName.setText(wolfPropBean.name);
        holder.rePropItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, position);
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

    public void setPropAdapter(List<WolfPropBean> gameBeanList) {
        list.clear();
        list.addAll(gameBeanList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_prop_img)
        ImageView ivPropImg;
        @BindView(R.id.tv_prop_name)
        TextView tvPropName;
        @BindView(R.id.re_prop_item)
        RelativeLayout rePropItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
