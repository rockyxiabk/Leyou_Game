package com.leyou.game.adapter.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.R;
import com.leyou.game.activity.mine.AwardDetailActivity;
import com.leyou.game.bean.AwardInfoBean;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.NumberFormatUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.adapter
 *
 * @author : rocky
 * @Create Time : 2017/6/14 下午4:43
 * @Modified Time : 2017/6/14 下午4:43
 */
public class AwardAdapter extends RecyclerView.Adapter<AwardAdapter.ViewHolder> {

    private Context context;
    private List<AwardInfoBean> list;

    public AwardAdapter(Context context, List<AwardInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_win_award_user_list);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        AwardInfoBean bean = list.get(position);
        Glide.with(context).load(bean.prizeUrl).error(R.mipmap.icon_default).into(holder.ivAwardImg);
        holder.tvAwardDes.setText(bean.prizeName);
        holder.tvAwardTime.setText("获奖时间：" + DataUtil.getConvertResult(bean.addTime, DataUtil.Y_M_D));
        holder.tvAwardTitle.setText(bean.game + "    " + NumberFormatUtil.formatInteger(bean.rank) + "等奖");
        switch (bean.status) {//0未填写地址 1待发货2.已发货
            case 0:
                holder.tvAwardState.setText("未填写地址");
                break;
            case 1:
                holder.tvAwardState.setText("待发货");
                break;
            case 2:
                holder.tvAwardState.setText("已发货");
                break;
        }
        holder.tvAwardState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AwardDetailActivity.class);
                intent.putExtra("awardInfo", list.get(position));
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        holder.reAwardRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AwardDetailActivity.class);
                intent.putExtra("awardInfo", list.get(position));
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    public void setListAdapter(List<AwardInfoBean> infoBeanList) {
        list.clear();
        if (null != infoBeanList && infoBeanList.size() > 0) {
            list.addAll(infoBeanList);
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
        @BindView(R.id.re_award_root)
        LinearLayout reAwardRoot;
        @BindView(R.id.iv_award_img)
        ImageView ivAwardImg;
        @BindView(R.id.tv_award_title)
        TextView tvAwardTitle;
        @BindView(R.id.tv_award_des)
        TextView tvAwardDes;
        @BindView(R.id.tv_award_time)
        TextView tvAwardTime;
        @BindView(R.id.tv_award_state)
        TextView tvAwardState;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
