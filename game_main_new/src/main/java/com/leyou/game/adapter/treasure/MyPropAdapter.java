package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.treasure.PropBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午11:13
 * @Modified Time : 2017/6/30 上午11:13
 */
public class MyPropAdapter extends RecyclerView.Adapter<MyPropAdapter.ViewHolder> {
    private Context context;
    private List<PropBean> list;
    private CustomItemClickListener listener;

    public MyPropAdapter(Context context, List<PropBean> list, CustomItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_my_prop);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        PropBean propBean = list.get(position);
        Glide.with(context).load(propBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivPropImg);
        holder.tvPropName.setText(propBean.itemName + " x " + propBean.itemNum);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, position);
            }
        });
    }

    public void setListAdapter(List<PropBean> infoBeanList) {
        list.clear();
        if (null != infoBeanList && infoBeanList.size() > 0) {
            list.addAll(infoBeanList);
        }
        notifyDataSetChanged();
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
        @BindView(R.id.ll_my_prop_item)
        LinearLayout llItem;
        @BindView(R.id.iv_prop_img)
        ImageView ivPropImg;
        @BindView(R.id.tv_prop_name)
        TextView tvPropName;
        @BindView(R.id.ll_prop_use)
        LinearLayout llPropUse;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
