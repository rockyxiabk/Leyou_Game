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
import com.leyou.game.bean.user.WithCashNoteBean;
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
public class WithCashNoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ConsumeNoteAdapter";
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private final Context context;
    private final List<WithCashNoteBean> list;
    private int currentNumber = 1;
    private boolean isLoadAllData = false;

    public WithCashNoteAdapter(Context context, List<WithCashNoteBean> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.layout_with_cash_note_list, parent, false);
            return new ConsumeHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConsumeHolder) {
            ConsumeHolder consumeHolder = (ConsumeHolder) holder;
            WithCashNoteBean consumeBean = list.get(position);
            consumeHolder.tvApplyTime.setText(DataUtil.getConvertResult(consumeBean.createTime, DataUtil.Y_M_D_HMS));
            consumeHolder.tvMoney.setText("-" + consumeBean.money + "元");
            switch (consumeBean.status) {
                case 1://1初始申请状态
                    consumeHolder.tvState.setText("待审核");
                    consumeHolder.tvState.setTextColor(context.getResources().getColor(R.color.orange_fe));
                    break;
                case 2://2审核通过
                    consumeHolder.tvState.setText("待打款");
                    consumeHolder.tvState.setTextColor(context.getResources().getColor(R.color.orange_fe));
                    break;
                case 3://3已打款
                    consumeHolder.tvState.setText("已打款");
                    consumeHolder.tvState.setTextColor(context.getResources().getColor(R.color.blue_44));
                    break;
                case 4://4审核未通过
                    consumeHolder.tvState.setText("审核失败，账户异常");
                    consumeHolder.tvState.setTextColor(context.getResources().getColor(R.color.red_f2));
                    break;
                default:
                    consumeHolder.tvState.setText("审核失败，账户异常");
                    consumeHolder.tvState.setTextColor(context.getResources().getColor(R.color.red_f2));
                    break;
            }
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
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
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size() + currentNumber;
        }
        return ret;
    }

    public void addAdapter(List<WithCashNoteBean> currentList) {
        list.clear();
        list.addAll(currentList);
        notifyDataSetChanged();
    }

    public void loadMoreData(List<WithCashNoteBean> currentList) {
        list.addAll(currentList);
        notifyDataSetChanged();
    }

    public void setLoadAllData(boolean loadAllData) {
        this.isLoadAllData = loadAllData;
        LogUtil.d(TAG, "----isLoadAllData:" + isLoadAllData);
    }

    public class ConsumeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_apply_time)
        TextView tvApplyTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_state)
        TextView tvState;

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
