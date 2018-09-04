package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.bean.treasure.WorkerBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/7/3 下午3:51
 * @Modified Time : 2017/7/3 下午3:51
 */
public class ChooseWorkerUsePropAdapter extends RecyclerView.Adapter<ChooseWorkerUsePropAdapter.ViewHolder> {

    private Context context;
    private List<WorkerBean> list;
    private ChoseItemClickListener listener;
    private int selectorPosition = 0;

    public ChooseWorkerUsePropAdapter(Context context, List<WorkerBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setChoseItemClickListener(ChoseItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_treasure_choose_worker_use_prop_list);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    /**
     * 获得布局
     *
     * @param parent
     * @param layoutRes
     * @return
     */
    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final WorkerBean workerBean = list.get(position);

        holder.tvChooseWorkerPower.setText((int) (workerBean.attribute + workerBean.starLevel * 0.2 * workerBean.maxAttribute)
                + "/" + (int) ((1 + workerBean.starLevel * 0.2) * workerBean.maxAttribute));
        float progress = (workerBean.attribute * 1.0f / (workerBean.maxAttribute * 1.0f)) * 100;
        holder.roundPrPower.setProgress(progress);
        holder.tvChooseWorkerPhy.setText(workerBean.phyPower + "");
        float phyProgress = (workerBean.phyPower / (100.0f + workerBean.starLevel * 10)) * 100;
        holder.roundPrPhy.setProgress(phyProgress);
        if (workerBean.starLevel > 0) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            switch (workerBean.starLevel) {
                case 1:
                    holder.ratingBar.setImageResource(R.mipmap.icon_star_one);
                    break;
                case 2:
                    holder.ratingBar.setImageResource(R.mipmap.icon_star_two);
                    break;
                case 3:
                    holder.ratingBar.setImageResource(R.mipmap.icon_star_three);
                    break;
                case 4:
                    holder.ratingBar.setImageResource(R.mipmap.icon_star_four);
                    break;
                case 5:
                    holder.ratingBar.setImageResource(R.mipmap.icon_star_five);
                    break;
            }
        } else {
            holder.ratingBar.setVisibility(View.GONE);
        }

        if (selectorPosition == position) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }
        Glide.with(context).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivTreasureWorkerImg);
        holder.ivTreasureWorkerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != selectorPosition) {
                    selectorPosition = position;
                }
                listener.choseWorker(workerBean);
                notifyDataSetChanged();
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != selectorPosition) {
                    selectorPosition = position;
                }
                listener.choseWorker(workerBean);
                notifyDataSetChanged();
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

    public void setWorkerAdapter(List<WorkerBean> workerBeanList) {
        list.clear();
        if (null != workerBeanList && workerBeanList.size() > 0) {
            list.addAll(workerBeanList);
            selectorPosition = 0;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_treasure_worker_img)
        ImageView ivTreasureWorkerImg;
        @BindView(R.id.iv_worker_start)
        ImageView ratingBar;
        @BindView(R.id.cb_checked_worker)
        CheckBox checkBox;
        @BindView(R.id.round_pr_phy)
        RoundCornerProgressBar roundPrPhy;
        @BindView(R.id.tv_choose_worker_phy)
        TextView tvChooseWorkerPhy;
        @BindView(R.id.round_pr_power)
        RoundCornerProgressBar roundPrPower;
        @BindView(R.id.tv_choose_worker_power)
        TextView tvChooseWorkerPower;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ChoseItemClickListener {
        void choseWorker(WorkerBean workerBean);
    }
}
