package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.widget.dialog.EmployWorkerDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Description : com.leyou.game.fragment
 *
 * @author : rocky
 * @Create Time : 2017/4/26 下午7:27
 * @Modified Time : 2017/4/26 下午7:27
 */
public class TreasureCanEmployWorkerAdapter extends RecyclerView.Adapter<TreasureCanEmployWorkerAdapter.WorkerHolder> {

    private Context context;
    private List<WorkerBean> list;

    public TreasureCanEmployWorkerAdapter(Context context, List<WorkerBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public WorkerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_treasure_list);
        WorkerHolder workerHolder = new WorkerHolder(itemView);
        return workerHolder;
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
    public void onBindViewHolder(WorkerHolder holder, int position) {
        onWorkerHolder(holder, position);
    }

    private void onWorkerHolder(WorkerHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (position >= list.size()) {
            Glide.with(context).load("").error(R.mipmap.icon_worker_default).into(holder.ivTreasureWorkerImg);
            holder.tvTreasureWorkerPower.setText("0");
            holder.roundPrPower.setProgress(0f);
            holder.tvTreasureWorkerPhy.setText("0");
            holder.roundPrPhy.setProgress(0f);
            holder.ivTreasureWorkerImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            final WorkerBean workerBean = list.get(position);
            holder.tvTreasureWorkerPower.setText(workerBean.attribute + "");
            float progress = (workerBean.attribute * 1.0f / (workerBean.maxAttribute * 1.0f)) * 100;
            holder.roundPrPower.setProgress(progress);
            holder.tvTreasureWorkerPhy.setText(100 + "");
            holder.roundPrPhy.setProgress(100f);
            Glide.with(context).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivTreasureWorkerImg);
            holder.ivTreasureWorkerImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmployWorkerDialog employWorkerDialog = new EmployWorkerDialog(context, workerBean);
                    employWorkerDialog.show();
                }
            });
            Integer typeId = Integer.valueOf(workerBean.typeId + "");
            holder.gifNewworker.setVisibility(View.VISIBLE);
            if (typeId == 1) {
                holder.gifNewworker.setImageResource(R.mipmap.icon_worker_new_org);
            } else if (typeId == 2) {
                holder.gifNewworker.setImageResource(R.mipmap.icon_worker_new_red);
            } else {
                holder.gifNewworker.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 5;
        return ret;
    }

    public void setWorkerAdapter(List<WorkerBean> workerBeanList) {
        list.clear();
        if (null != workerBeanList && workerBeanList.size() > 0) {
            list.addAll(workerBeanList);
        }
        notifyDataSetChanged();
    }

    public class WorkerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_treasure_worker_img)
        ImageView ivTreasureWorkerImg;
        @BindView(R.id.round_pr_phy)
        RoundCornerProgressBar roundPrPhy;
        @BindView(R.id.tv_treasure_worker_phy)
        TextView tvTreasureWorkerPhy;
        @BindView(R.id.round_pr_power)
        RoundCornerProgressBar roundPrPower;
        @BindView(R.id.tv_treasure_worker_power)
        TextView tvTreasureWorkerPower;
        @BindView(R.id.ll_round)
        LinearLayout llRound;
        @BindView(R.id.gif_new_worker)
        GifImageView gifNewworker;

        public WorkerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
