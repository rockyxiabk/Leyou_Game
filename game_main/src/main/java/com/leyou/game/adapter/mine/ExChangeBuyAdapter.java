package com.leyou.game.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.bean.ExChangeBuyBean;
import com.leyou.game.bean.ExChangeSaleBean;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.widget.dialog.SellDiamondDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : 交易所列表适配器
 *
 * @author : rocky
 * @Create Time : 2017/4/20 下午3:09
 * @Modified Time : 2017/4/20 下午3:09
 */
public class ExChangeBuyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ExChangeBuyAdapter";
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private List<ExChangeBuyBean> list;
    private int currentNumber = 1;
    private boolean isLoadAllData = false;

    public ExChangeBuyAdapter(Context context, List<ExChangeBuyBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_exchange_buy_list, parent, false);
            return new ExChangeHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        holder1.setIsRecyclable(false);
        if (holder1 instanceof ExChangeHolder) {
            ExChangeHolder holder = (ExChangeHolder) holder1;
            final ExChangeBuyBean exChangeBean = list.get(position);
            holder.btnExchangeBuy.setText(exChangeBean.price + "元/钻");
            holder.tvDiamondNumber.setText(exChangeBean.currVirtualCoin + "钻石");
            holder.reBuyItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SellDiamondDialog sellDiamondDialog = new SellDiamondDialog(context, exChangeBean);
                    sellDiamondDialog.show();
                }
            });
        } else if (holder1 instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder1;
            if (!isLoadAllData) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim);
                footViewHolder.ivFooterProgress.startAnimation(animation);
            } else {
                footViewHolder.ivFooterProgress.setVisibility(View.GONE);
                footViewHolder.tvFooterDes.setText(context.getString(R.string.no_more_data));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + currentNumber == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size() + currentNumber;
        }
        return ret;
    }

    public void addAdapter(List<ExChangeBuyBean> currentList) {
        list.addAll(currentList);
        notifyDataSetChanged();
    }

    public void updateAdapter(List<ExChangeBuyBean> updateList) {
        list.clear();
        list.addAll(updateList);
        notifyDataSetChanged();
    }

    public void setLoadAllData(boolean loadAllData) {
        this.isLoadAllData = loadAllData;
    }

    public class ExChangeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_buy_list_item)
        RelativeLayout reBuyItem;
        @BindView(R.id.iv_exchange_diamond)
        ImageView ivExchangeDiamond;
        @BindView(R.id.tv_diamond_number)
        TextView tvDiamondNumber;
        @BindView(R.id.btn_exchange_buy)
        Button btnExchangeBuy;

        public ExChangeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_footer_progress)
        ImageView ivFooterProgress;
        @BindView(R.id.tv_footer_des)
        TextView tvFooterDes;

        public FootViewHolder(View temView) {
            super(temView);
            ButterKnife.bind(this, itemView);
        }
    }
}
