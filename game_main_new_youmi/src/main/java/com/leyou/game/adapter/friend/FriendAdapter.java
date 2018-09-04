package com.leyou.game.adapter.friend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.friend.FriendDetailActivity;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.dao.Friend;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.UserInfo;

/**
 * Description :新的好友
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:53
 * @Modified Time : 2017/7/15 下午3:53
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private static final String TAG = "FriendAdapter";
    private Context context;
    private List<Friend> list;
    private CustomItemClickListener listener;

    public FriendAdapter(Context context, List<Friend> list) {
        this.context = context;
        this.list = list;
    }

    public void setCustomItemClickListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_friend_list_item);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        final Friend contactBean = list.get(position);
        LogUtil.d(TAG, contactBean.toString());
        holder.tvFriendIdNo.setText(context.getString(R.string.friend_id_no) + contactBean.getIdNo() + "");
        holder.tvContactName.setText(!TextUtils.isEmpty(contactBean.getName()) ?
                contactBean.getName() : !TextUtils.isEmpty(contactBean.getNickname()) ?
                contactBean.getNickname() : context.getString(R.string.app_name));
        holder.ivFriendHeader.setImageURI(contactBean.getHeadImgUrl());

        Friend friend = DBUtil.getInstance(context).queryFriendByUserId(contactBean.getUserId());
        if (null != friend) {
            if (friend.getStatus() == 3) {
                holder.btnAdd.setVisibility(View.GONE);
            } else {
                holder.btnAdd.setVisibility(View.VISIBLE);
            }
        } else {
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnAdd.setText("添加");
        }
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, position);
            }
        });
        holder.reContactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendDetailActivity.class);
                intent.putExtra("userId", contactBean.getUserId());
                UserInfo userInfo = new UserInfo(contactBean.getUserId(),
                        contactBean.getNickname(), Uri.parse(contactBean.getHeadImgUrl()));
                intent.putExtra("userInfo", userInfo);
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

    public void setAdapterData(List<Friend> data) {
        if (null != data && data.size() > 0) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_friend_header)
        SimpleDraweeView ivFriendHeader;
        @BindView(R.id.tv_friend_idNo)
        TextView tvFriendIdNo;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.btn_add)
        Button btnAdd;
        @BindView(R.id.tv_invite_status)
        TextView tvInviteStatus;
        @BindView(R.id.re_contact_item)
        RelativeLayout reContactItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
