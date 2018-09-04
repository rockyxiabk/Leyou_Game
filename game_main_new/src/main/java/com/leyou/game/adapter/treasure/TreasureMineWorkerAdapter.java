package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.widget.dialog.treasury.AddWorkerPlaceDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/3 下午2:01
 * @Modified Time : 2017/6/3 下午2:01
 */
public class TreasureMineWorkerAdapter extends RecyclerView.Adapter<TreasureMineWorkerAdapter.WorkerHolder> {
    private static final String TAG = "TreasureMineWorkerAdapte";
    private Context context;
    private List<WorkerBean> list;
    private CustomItemClickListener listener;

    public TreasureMineWorkerAdapter(Context context, List<WorkerBean> list, CustomItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public TreasureMineWorkerAdapter.WorkerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_treasure_my_worker_list);
        TreasureMineWorkerAdapter.WorkerHolder workerHolder = new TreasureMineWorkerAdapter.WorkerHolder(itemView);
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
    public void onBindViewHolder(TreasureMineWorkerAdapter.WorkerHolder holder, int position) {
        onWorkerHolder(holder, position);
    }

    private void onWorkerHolder(TreasureMineWorkerAdapter.WorkerHolder holder, final int position) {
        final WorkerBean workerBean = list.get(position);
        if (workerBean.id.equalsIgnoreCase("0")) {
            holder.llRound.setVisibility(View.GONE);
            holder.tvWorkerAdd.setVisibility(View.VISIBLE);
            holder.tvWorkerAdd.setText("添加矿工位");
            holder.ivWorkerStar.setVisibility(View.GONE);
            holder.ivWorkerState.setVisibility(View.GONE);
        } else if (workerBean.id.equalsIgnoreCase("1")) {
            holder.llRound.setVisibility(View.GONE);
            holder.tvWorkerAdd.setVisibility(View.VISIBLE);
            holder.tvWorkerAdd.setText("未雇佣矿工");
            holder.ivWorkerStar.setVisibility(View.GONE);
            holder.ivWorkerState.setVisibility(View.GONE);
        } else {
            holder.tvWorkerAdd.setVisibility(View.GONE);
            holder.llRound.setVisibility(View.VISIBLE);

            holder.tvTreasureWorkerPower.setText((int) (workerBean.attribute + workerBean.starLevel * 0.2 * workerBean.maxAttribute)
                    + "/" + (int) ((1 + workerBean.starLevel * 0.2) * workerBean.maxAttribute));
            float progress = (workerBean.attribute * 1.0f / (workerBean.maxAttribute * 1.0f)) * 100;
            holder.roundPrPower.setProgress(progress);

            holder.tvTreasureWorkerPhy.setText(workerBean.phyPower + "");
            float phyProgress = (workerBean.phyPower / (100.0f + workerBean.starLevel * 10)) * 100;
            holder.roundPrPhy.setProgress(phyProgress);

            if (!TextUtils.isEmpty(workerBean.treasureId)) {
                holder.ivWorkerState.setImageResource(R.mipmap.icon_worker_working);
                holder.ivWorkerState.setVisibility(View.VISIBLE);
                holder.ivWorkerState.setVisibility(View.VISIBLE);
            } else {
                if (workerBean.phyPower < 10) {
                    holder.ivWorkerState.setImageResource(R.mipmap.icon_worker_hungry);
                    holder.ivWorkerState.setVisibility(View.VISIBLE);
                } else {
                    holder.ivWorkerState.setVisibility(View.GONE);
                }
            }
            holder.ivWorkerStar.setVisibility(View.VISIBLE);
            switch (workerBean.starLevel) {
                case 0:
                    holder.ivWorkerStar.setVisibility(View.GONE);
                    break;
                case 1:
                    holder.ivWorkerStar.setImageResource(R.mipmap.icon_star_one);
                    break;
                case 2:
                    holder.ivWorkerStar.setImageResource(R.mipmap.icon_star_two);
                    break;
                case 3:
                    holder.ivWorkerStar.setImageResource(R.mipmap.icon_star_three);
                    break;
                case 4:
                    holder.ivWorkerStar.setImageResource(R.mipmap.icon_star_four);
                    break;
                case 5:
                    holder.ivWorkerStar.setImageResource(R.mipmap.icon_star_five);
                    break;
            }
        }
        Glide.with(context).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivTreasureWorkerImg);
        holder.ivTreasureWorkerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workerBean.id.equalsIgnoreCase("0")) {
                    AddWorkerPlaceDialog dialog = new AddWorkerPlaceDialog(context, 10);
                    dialog.show();
                } else if (workerBean.id.length() > 5) {
                    listener.onItemClick(null, position);
                }
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
        } else {
            list.add(new WorkerBean("0"));
        }
        notifyDataSetChanged();
    }

    public class WorkerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_treasure_worker_img)
        ImageView ivTreasureWorkerImg;
        @BindView(R.id.iv_worker_start)
        ImageView ivWorkerStar;
        @BindView(R.id.tv_treasure_worker_power)
        TextView tvTreasureWorkerPower;
        @BindView(R.id.tv_treasure_worker_add)
        TextView tvWorkerAdd;
        @BindView(R.id.iv_worker_state)
        ImageView ivWorkerState;
        @BindView(R.id.ll_round)
        LinearLayout llRound;
        @BindView(R.id.round_pr_phy)
        RoundCornerProgressBar roundPrPhy;
        @BindView(R.id.tv_treasure_worker_phy)
        TextView tvTreasureWorkerPhy;
        @BindView(R.id.round_pr_power)
        RoundCornerProgressBar roundPrPower;

        public WorkerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
