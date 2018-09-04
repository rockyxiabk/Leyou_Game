package com.leyou.game.adapter.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.adapter.friend.MessageAdapter;
import com.leyou.game.bean.ConsumeBean;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter.mine
 *
 * @author : rocky
 * @Create Time : 2017/8/10 下午4:11
 * @Modified Time : 2017/8/10 下午4:11
 */
public class ConsumeNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ConsumeNoteAdapter";
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private final Context context;
    private final List<ConsumeBean> list;
    private int currentNumber = 1;
    private boolean isLoadAllData = false;

    public ConsumeNoteAdapter(Context context, List<ConsumeBean> list) {
        this.context = context;
        this.list = list;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_consume_list, parent, false);
            return new ConsumeHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        if (holder instanceof ConsumeHolder) {
            ConsumeHolder consumeHolder = (ConsumeHolder) holder;
            ConsumeBean consumeBean = list.get(position);
            Glide.with(context).load(consumeBean.pictureUrl).into(((ConsumeHolder) holder).ivConsumeIcon);
            consumeHolder.tvconsumeListTitle.setText(consumeBean.note);
            consumeHolder.tvConsumeTime.setText(DataUtil.getConvertResult(consumeBean.time, DataUtil.Y_M_D_HM));
            consumeHolder.tvDiamondNumber.setText(consumeBean.virtualCoin + "钻");
            if (!TextUtils.isEmpty(consumeBean.money)) {
                consumeHolder.tvMoney.setVisibility(View.VISIBLE);
                consumeHolder.tvMoney.setText(consumeBean.money + "元");
            } else {
                consumeHolder.tvMoney.setVisibility(View.GONE);
            }

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (!isLoadAllData) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim);
                footViewHolder.ivFooterProgress.startAnimation(animation);
            } else {
                footViewHolder.ivFooterProgress.setVisibility(View.GONE);
                footViewHolder.tvFooterDes.setText(context.getString(R.string.no_more_message));
            }
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

    public void addAdapter(List<ConsumeBean> currentList) {
        list.addAll(currentList);
        notifyDataSetChanged();
    }

    public void setLoadAllData(boolean loadAllData) {
        this.isLoadAllData = loadAllData;
        LogUtil.d(TAG, "----flag:" + isLoadAllData);
    }

    public class ConsumeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_consume_icon)
        ImageView ivConsumeIcon;
        @BindView(R.id.tv_consume_list_title)
        TextView tvconsumeListTitle;
        @BindView(R.id.tv_consume_time)
        TextView tvConsumeTime;
        @BindView(R.id.tv_diamond_number)
        TextView tvDiamondNumber;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.re_message_list_item)
        RelativeLayout reMessageListItem;

        public ConsumeHolder(View itemView) {
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
