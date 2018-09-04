package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.util.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/3 下午2:01
 * @Modified Time : 2017/6/3 下午2:01
 */
public class ChooseWorkerAdapter extends RecyclerView.Adapter<ChooseWorkerAdapter.WorkerHolder> {
    private Context context;
    private List<WorkerBean> list;
    private ChoseItemClickListener listener;
    private Map<Integer, Boolean> map = new HashMap<>();
    private int choseCount = 0;
    private int selectNum = 5;
    private boolean isSelected = false;

    public ChooseWorkerAdapter(Context context, List<WorkerBean> list) {
        this.context = context;
        this.list = list;
    }

    public ChooseWorkerAdapter(Context context, List<WorkerBean> list, boolean flag) {
        this.context = context;
        this.list = list;
        isSelected = flag;
    }

    public void setChoseItemClickListener(ChoseItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public WorkerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_treasure_modify_choose_worker_list);
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

    private void onWorkerHolder(final WorkerHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final WorkerBean workerBean = list.get(position);

        holder.tvChooseWorkerPower.setText(workerBean.attribute + "/" + workerBean.maxAttribute);
        float progress = (workerBean.attribute * 1.0f / (workerBean.maxAttribute * 1.0f)) * 100;
        holder.roundPrPower.setProgress(progress);
        holder.tvChooseWorkerPhy.setText(workerBean.phyPower + "/" + (100 + workerBean.starLevel * 10));
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
            holder.ivWorkerStar.setVisibility(View.INVISIBLE);
        }
        holder.checkBox.setChecked(map.get(position));
        Glide.with(context).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(holder.ivTreasureWorkerImg);
        holder.reItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemSelector(position);
                holder.checkBox.setChecked(map.get(position));
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemSelector(position);
                holder.checkBox.setChecked(map.get(position));
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

    private void resetMap() {
        map.clear();
        for (int i = 0; i < list.size(); i++) {
            map.put(i, isSelected);
        }
    }

    private void setItemSelector(int position) {
        Boolean aBoolean = map.get(position);
        if (choseCount < selectNum) {
            if (aBoolean) {
                choseCount--;
                map.put(position, false);
            } else {
                choseCount++;
                map.put(position, true);
            }
            listener.choseMap(map);
        } else {
            if (aBoolean) {
                choseCount--;
                map.put(position, false);
                listener.choseMap(map);
            } else {
                ToastUtils.showToastShort("最多选择5个矿工！");
            }
        }
    }

    public void setWorkerAdapter(List<WorkerBean> workerBeanList) {
        list.clear();
        if (null != workerBeanList && workerBeanList.size() > 0) {
            list.addAll(workerBeanList);
        }
        resetMap();
        notifyDataSetChanged();
    }

    public void setSelectNum(int num) {
        this.selectNum = num;
    }

    public void setChoseCount(int chose) {
        this.choseCount = chose;
    }

    public int getChoseCount() {
        return choseCount;
    }

    public class WorkerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_item)
        RelativeLayout reItem;
        @BindView(R.id.iv_treasure_worker_img)
        ImageView ivTreasureWorkerImg;
        @BindView(R.id.iv_worker_star)
        ImageView ivWorkerStar;
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
        @BindView(R.id.ll_round_upgrade)
        LinearLayout llRoundUpgrade;

        public WorkerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ChoseItemClickListener {
        void choseMap(Map<Integer, Boolean> map);
    }

}
