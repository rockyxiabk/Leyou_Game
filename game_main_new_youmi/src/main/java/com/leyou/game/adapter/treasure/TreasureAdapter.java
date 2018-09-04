package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.treasure.TreasureBean;
import com.leyou.game.widget.CountDownTimeTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/12 上午10:18
 * @Modified Time : 2017/6/12 上午10:18
 */
public class TreasureAdapter extends RecyclerView.Adapter<TreasureAdapter.ViewHolder> {

    private Context context;
    private List<TreasureBean> list;
    private CustomItemClickListener mItemClickListener;
    private int position0 = 0;

    public TreasureAdapter(Context context, List<TreasureBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_treasure_time_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        if (null != list && list.size() > 0 && list.size() > position) {
            TreasureBean treasureBean = list.get(position);
            if (position == position0) {
                holder.tvTreasureTime.setSelected(true);
                holder.tvTreasureTime.setTextColor(context.getResources().getColor(R.color.white));

                long mMillisInFuture = treasureBean.residueTime - System.currentTimeMillis();
                holder.tvTreasureTime.setMillisInFuture(mMillisInFuture);
                holder.tvTreasureTime.setCountdownInterval(1000);
                holder.tvTreasureTime.start();
                if (mMillisInFuture <= 0 && treasureBean.residueTime != 0) {
                    startFlick(holder.tvTreasureTime);
                } else {
                    stopFlick(holder.tvTreasureTime);
                }
            } else {
                holder.tvTreasureTime.setSelected(false);
                holder.tvTreasureTime.setTextColor(context.getResources().getColor(R.color.black));

                long mMillisInFuture = treasureBean.residueTime - System.currentTimeMillis();
                holder.tvTreasureTime.setMillisInFuture(mMillisInFuture);
                holder.tvTreasureTime.setCountdownInterval(1000);
                holder.tvTreasureTime.start();
                if (mMillisInFuture <= 0 && treasureBean.residueTime != 0) {
                    startFlick(holder.tvTreasureTime);
                } else {
                    stopFlick(holder.tvTreasureTime);
                }
            }
        } else {
            stopFlick(holder.tvTreasureTime);
            holder.tvTreasureTime.setText("空");
        }
    }

    /**
     * 开启View闪烁效果
     */
    private void startFlick(View view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0.4f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 取消View闪烁效果
     */
    private void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
    }

    public void setTreasureAdapter(List<TreasureBean> treasureBeanList) {
        list.clear();
        if (null != treasureBeanList && treasureBeanList.size() > 0) {
            list.addAll(treasureBeanList);
            position0 = 0;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int ret = 5;
        return ret;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_treasure_time)
        CountDownTimeTextView tvTreasureTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            position0 = getPosition();
            mItemClickListener.onItemClick(v, getPosition());
        }
    }

    public void setOnItemClickListener(CustomItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
