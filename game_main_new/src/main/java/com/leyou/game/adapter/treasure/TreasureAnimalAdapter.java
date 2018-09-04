package com.leyou.game.adapter.treasure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leyou.game.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/22 上午9:06
 * @Modified Time : 2017/6/22 上午9:06
 */
public class TreasureAnimalAdapter extends RecyclerView.Adapter<TreasureAnimalAdapter.WorkerHolder> {
    private Context context;
    private List<String> list = new ArrayList<>();
    private int type_animal = 1;

    public TreasureAnimalAdapter(Context context, int type_animal) {
        this.context = context;
        this.setTypeAnimal(type_animal);
    }

    @Override
    public WorkerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_treasure_modify_animal_list);
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
        switch (type_animal) {
            case 1:
                holder.ivTreasureAnimalImg.setImageResource(R.mipmap.icon_treasure_animal_wolf);
                break;
            case 2:
                holder.ivTreasureAnimalImg.setImageResource(R.mipmap.icon_treasure_animal_bear);
                break;
            case 3:
                holder.ivTreasureAnimalImg.setImageResource(R.mipmap.icon_treasure_animal_lion);
                break;
            case 4:
                holder.ivTreasureAnimalImg.setImageResource(R.mipmap.icon_treasure_animal_tiger);
                break;
            case 5:
                holder.ivTreasureAnimalImg.setImageResource(R.mipmap.icon_treasure_animal_dragon);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return 5;
    }

    public void setTypeAnimal(int type_animal) {
        this.type_animal = type_animal;
        notifyDataSetChanged();
    }

    public class WorkerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_treasure_animal_img)
        ImageView ivTreasureAnimalImg;

        public WorkerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

