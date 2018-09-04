package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.bean.WorkerBean;

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
public class ChoseWorkerAdapter extends RecyclerView.Adapter<ChoseWorkerAdapter.WorkerHolder> {
    private Context context;
    private List<WorkerBean> list;
    private DeleteItemClickListener listener;

    public ChoseWorkerAdapter(Context context, List<WorkerBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setDeleteItemListener(DeleteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ChoseWorkerAdapter.WorkerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_treasure_list);
        ChoseWorkerAdapter.WorkerHolder workerHolder = new ChoseWorkerAdapter.WorkerHolder(itemView);
        return workerHolder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ChoseWorkerAdapter.WorkerHolder holder, int position) {
        onWorkerHolder(holder, position);
    }

    private void onWorkerHolder(ChoseWorkerAdapter.WorkerHolder holder, int position) {
        holder.setIsRecyclable(false);
        final WorkerBean workerBean = list.get(position);
        holder.tvTreasureWorkerPower.setText("力量值+" + workerBean.attribute);
        Glide.with(context).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivTreasureWorkerImg);
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
        list.addAll(workerBeanList);
        notifyDataSetChanged();
    }

    public class WorkerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_treasure_worker_img)
        ImageView ivTreasureWorkerImg;
        @BindView(R.id.tv_treasure_worker_power)
        TextView tvTreasureWorkerPower;

        public WorkerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.delete(v, getPosition());
        }
    }

    public interface DeleteItemClickListener {
        void delete(View view, int position);
    }
}
