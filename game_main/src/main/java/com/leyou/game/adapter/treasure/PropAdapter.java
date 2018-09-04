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
import com.leyou.game.bean.PropBean;
import com.leyou.game.widget.dialog.BuyPropDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午11:14
 * @Modified Time : 2017/6/30 上午11:14
 */
public class PropAdapter extends RecyclerView.Adapter<PropAdapter.ViewHolder> {
    private Context context;
    private List<PropBean> list;

    public PropAdapter(Context context, List<PropBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_prop);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final PropBean propBean = list.get(position);
        Glide.with(context).load(propBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivPropImg);
        holder.tvPropName.setText(propBean.itemName + "");
        holder.tvPropChipPrice.setText(propBean.itemPrice1 + "");
        holder.tvPropDiamondPrice.setText(propBean.itemPrice2 + "");
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BuyPropDialog(context, propBean).show();
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
        @BindView(R.id.ll_prop_item)
        LinearLayout llItem;
        @BindView(R.id.iv_prop_img)
        ImageView ivPropImg;
        @BindView(R.id.tv_prop_name)
        TextView tvPropName;
        @BindView(R.id.tv_prop_chip_price)
        TextView tvPropChipPrice;
        @BindView(R.id.ll_prop_buy_chip)
        LinearLayout llPropBuyChip;
        @BindView(R.id.tv_prop_diamond_price)
        TextView tvPropDiamondPrice;
        @BindView(R.id.ll_prop_buy_diamond)
        LinearLayout llPropBuyDiamond;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
