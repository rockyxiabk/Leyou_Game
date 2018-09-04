package com.leyou.game.adapter.wolfkill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WolfKillMilitaryBean;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/7/16 下午4:13
 * @Modified Time : 2017/7/16 下午4:13
 */
public class WolfKillMilitaryAdapter extends RecyclerView.Adapter<WolfKillMilitaryAdapter.ViewHolder> {
    private static final String TAG = "WolfKillMilitaryAdapter";
    private Context context;
    private List<WolfKillMilitaryBean> list;
    private boolean isLoadAllData = false;

    public WolfKillMilitaryAdapter(Context context, List<WolfKillMilitaryBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_wolf_kill_military_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WolfKillMilitaryBean militaryBean = list.get(position);
        holder.ivWolfKillHeaderImg.setImageURI(UserData.getInstance().getPictureUrl());
        holder.tvWolfPartTime.setText(DataUtil.getConvertResult(militaryBean.time, DataUtil.HM) + "\n" + DataUtil.getConvertResult(militaryBean.time, DataUtil.Y_M_D));
        holder.tvWolfKillType.setText(militaryBean.type);
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public void addAdapter(List<WolfKillMilitaryBean> currentList) {
        list.addAll(currentList);
        notifyDataSetChanged();
    }

    public void updateAdapter(List<WolfKillMilitaryBean> updateList) {
        if (null != updateList && updateList.size() > 0) {
            list.clear();
            list.addAll(updateList);
            notifyDataSetChanged();
        }
        LogUtil.d(TAG, "---" + list.size());
    }

    public void setLoadAllData(boolean loadAllData) {
        this.isLoadAllData = loadAllData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_wolf_kill_header_img)
        SimpleDraweeView ivWolfKillHeaderImg;
        @BindView(R.id.tv_wolf_kill_type)
        TextView tvWolfKillType;
        @BindView(R.id.iv_wolf_part_state)
        ImageView ivWolfPartState;
        @BindView(R.id.tv_wolf_part_time)
        TextView tvWolfPartTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
