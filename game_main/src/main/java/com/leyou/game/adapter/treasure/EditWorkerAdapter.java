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
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.bean.WorkerBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/6 下午1:51
 * @Modified Time : 2017/6/6 下午1:51
 */
public class EditWorkerAdapter extends RecyclerView.Adapter<EditWorkerAdapter.ViewHolder> {

    private Context context;
    private List<WorkerBean> list;
    private CustomItemClickListener mItemClickListener;

    public EditWorkerAdapter(Context context, List<WorkerBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_treasure_edit_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        WorkerBean workerBean = list.get(position);
        Glide.with(context).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivTreasureWorkerImg);
        holder.tvTreasureWorkerPower.setText(workerBean.attribute + "");
        float progress = (workerBean.attribute * 1.0f / (workerBean.maxAttribute * 1.0f)) * 100;
        holder.roundPrPower.setProgress(progress);
        holder.tvTreasureWorkerPhy.setText(workerBean.phyPower + "");
        float phyProgress = (workerBean.phyPower / (100.0f + workerBean.starLevel * 10)) * 100;
        holder.roundPrPhy.setProgress(phyProgress);
        if (workerBean.starLevel > 0) {
            holder.ivWorkerStar.setVisibility(View.VISIBLE);
            switch (workerBean.starLevel) {
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
        } else {
            holder.ivWorkerStar.setVisibility(View.GONE);
        }
    }

    public void setWorkerAdapter(List<WorkerBean> workerBeen) {
        list.clear();
        if (null != workerBeen && workerBeen.size() > 0) {
            list.addAll(workerBeen);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_treasure_worker_img)
        ImageView ivTreasureWorkerImg;

        @BindView(R.id.iv_worker_star)
        ImageView ivWorkerStar;

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

        @BindView(R.id.tv_treasure_worker_choose)
        TextView tvTreasureWorkerChoose;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }
    }

    public void setOnItemClickListener(CustomItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
