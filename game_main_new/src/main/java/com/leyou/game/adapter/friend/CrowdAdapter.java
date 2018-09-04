package com.leyou.game.adapter.friend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.friend.ApplyCrowdDetailActivity;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.dao.Crowd;
import com.leyou.game.util.LogUtil;

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
public class CrowdAdapter extends RecyclerView.Adapter<CrowdAdapter.ViewHolder> {
    private static final String TAG = "NewContactsAdapter";
    private Context context;
    private List<Crowd> list;
    private CustomItemClickListener listener;

    public CrowdAdapter(Context context, List<Crowd> list) {
        this.context = context;
        this.list = list;
    }

    public void setCustomItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_crowd_list_item);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final Crowd contactBean = list.get(position);
        LogUtil.d(TAG, contactBean.toString());
        holder.tvCrowdName.setText(contactBean.getName());
        holder.ivCrowdHeader.setImageURI(contactBean.getHeadImgUrl());
        holder.tvCrowdIntroduce.setText("群简介:" + contactBean.getIntroduction() + "");
        holder.reContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ApplyCrowdDetailActivity.class);
                intent.putExtra("crowdInfo", contactBean);
                context.startActivity(intent);
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
        @BindView(R.id.tv_crowd_name)
        TextView tvCrowdName;
        @BindView(R.id.tv_crowd_introduce)
        TextView tvCrowdIntroduce;
        @BindView(R.id.re_contact_item)
        RelativeLayout reContactItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
