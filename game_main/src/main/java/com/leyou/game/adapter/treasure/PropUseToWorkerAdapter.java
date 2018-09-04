package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.bean.PropBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/30 下午3:51
 * @Modified Time : 2017/6/30 下午3:51
 */
public class PropUseToWorkerAdapter extends RecyclerView.Adapter<PropUseToWorkerAdapter.ViewHolder> {
    private Context context;
    private List<PropBean> list;
    private onSelectorProp onSelectorProp;
    private int selectorPosition = -1;

    public PropUseToWorkerAdapter(Context context, List<PropBean> list, onSelectorProp onSelectorProp) {
        this.context = context;
        this.list = list;
        this.onSelectorProp = onSelectorProp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_prop_choose);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final PropBean propBean = list.get(position);
        if (selectorPosition == position) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }
        Glide.with(context).load(propBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivPropImg);
        holder.tvPropName.setText(propBean.itemName + "x" + propBean.itemNum);
        holder.ivPropImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != selectorPosition) {
                    selectorPosition = position;
                }
                onSelectorProp.selectorProp(propBean);
                notifyDataSetChanged();
            }
        });
    }

    public void resetSelectorPosition() {
        this.selectorPosition = -1;
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
        @BindView(R.id.iv_prop_img)
        ImageView ivPropImg;
        @BindView(R.id.cb_checked_prop)
        CheckBox checkBox;
        @BindView(R.id.tv_prop_name)
        TextView tvPropName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface onSelectorProp {
        void selectorProp(PropBean propBean);
    }
}
