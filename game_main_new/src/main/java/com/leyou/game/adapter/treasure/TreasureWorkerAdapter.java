package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.treasure.WorkerBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/23 下午1:33
 * @Modified Time : 2017/6/23 下午1:33
 */
public class TreasureWorkerAdapter extends RecyclerView.Adapter<TreasureWorkerAdapter.ViewHolder> {
    private Context context;
    private List<WorkerBean> list;

    public TreasureWorkerAdapter(Context context, List<WorkerBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_treasure_worker_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        WorkerBean workerBean = list.get(position);
        holder.ivTreasureWorkerImg.setImageURI(workerBean.pictureUrl);
        holder.tvTreasureWorkerPower.setText("+" + workerBean.attribute);
    }

    public void setTreasureWorkerAdapter(List<WorkerBean> workerList) {
        list.clear();
        if (null != workerList && workerList.size() > 0) {
            list.addAll(workerList);
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
        @BindView(R.id.iv_treasure_worker_img)
        SimpleDraweeView ivTreasureWorkerImg;
        @BindView(R.id.tv_treasure_worker_power)
        TextView tvTreasureWorkerPower;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
