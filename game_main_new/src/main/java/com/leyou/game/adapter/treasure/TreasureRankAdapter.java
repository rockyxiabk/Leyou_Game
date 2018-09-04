package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.treasure.TreasureGainBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description :新的好友
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:53
 * @Modified Time : 2017/7/15 下午3:53
 */
public class TreasureRankAdapter extends RecyclerView.Adapter<TreasureRankAdapter.ViewHolder> {
    private static final String TAG = "TreasureRankAdapter";
    private Context context;
    private List<TreasureGainBean> list;
    private CustomItemClickListener listener;

    public TreasureRankAdapter(Context context, List<TreasureGainBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setCustomItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_treasure_rank_list_item);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final TreasureGainBean gainBean = list.get(position);
        holder.ivUserHeader.setImageURI(gainBean.getHeadImgUrl());
        holder.tvUserName.setText(gainBean.getNickname());
        holder.tvTreasureAttachGain.setText(gainBean.getOccupyNum() + "");
        holder.tvTreasureRefreshGain.setText(gainBean.getFreeGaiNum() + "");
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void setAdapterData(List<TreasureGainBean> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user_header)
        SimpleDraweeView ivUserHeader;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_treasure_attach_gain)
        TextView tvTreasureAttachGain;
        @BindView(R.id.tv_treasure_refresh_gain)
        TextView tvTreasureRefreshGain;
        @BindView(R.id.re_item)
        RelativeLayout reItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
