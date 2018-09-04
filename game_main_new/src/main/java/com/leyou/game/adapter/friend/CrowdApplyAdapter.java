package com.leyou.game.adapter.friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.dao.Crowd;
import com.leyou.game.ipresenter.friend.ICrowdApply;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description :新的好友
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:53
 * @Modified Time : 2017/7/15 下午3:53
 */
public class CrowdApplyAdapter extends RecyclerView.Adapter<CrowdApplyAdapter.ViewHolder> {
    private static final String TAG = "NewContactsAdapter";
    private Context context;
    private List<Crowd> list;
    private ICrowdApply iCrowdApply;
    private CustomItemClickListener listener;

    public CrowdApplyAdapter(Context context, List<Crowd> list, ICrowdApply iCrowdApply) {
        this.context = context;
        this.list = list;
        this.iCrowdApply = iCrowdApply;
    }

    public void setCustomItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_crowd_apply_list_item);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final Crowd crowd = list.get(position);
        holder.tvApplyName.setText(crowd.getMyName());
        holder.ivCrowdHeader.setImageURI(crowd.getHeadImgUrl());
        holder.tvApplyDes.setText("申请加入" + crowd.getName() + "");

        switch (crowd.status) {
            case 1:
                holder.btnAgree.setVisibility(View.VISIBLE);
                holder.btnIgnore.setVisibility(View.VISIBLE);
                holder.tvStatus.setVisibility(View.GONE);
                break;
            case 2:
                holder.btnAgree.setVisibility(View.GONE);
                holder.btnIgnore.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("已处理");
                break;
            case 3:
                holder.btnAgree.setVisibility(View.GONE);
                holder.btnIgnore.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("已忽略");
                break;
        }
        holder.btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCrowdApply.agreeApply(crowd);
            }
        });
        holder.btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCrowdApply.ignoreApply(crowd);
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

    public void setAdapterData(List<Crowd> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_crowd_header)
        SimpleDraweeView ivCrowdHeader;
        @BindView(R.id.tv_apply_name)
        TextView tvApplyName;
        @BindView(R.id.tv_apply_des)
        TextView tvApplyDes;
        @BindView(R.id.btn_ignore)
        Button btnIgnore;
        @BindView(R.id.btn_agree)
        Button btnAgree;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.re_contact_item)
        RelativeLayout reContactItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
