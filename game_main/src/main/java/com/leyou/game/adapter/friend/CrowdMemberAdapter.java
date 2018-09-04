package com.leyou.game.adapter.friend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.friend.AddCrowdMemberActivity;
import com.leyou.game.activity.friend.CrowdDetailActivity;
import com.leyou.game.activity.friend.DeleteCrowdMemberActivity;
import com.leyou.game.activity.friend.FriendDetailActivity;
import com.leyou.game.dao.Friend;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

/**
 * Description : com.leyou.game.adapter.friend
 *
 * @author : rocky
 * @Create Time : 2017/8/3 下午3:42
 * @Modified Time : 2017/8/3 下午3:42
 */
public class CrowdMemberAdapter extends RecyclerView.Adapter<CrowdMemberAdapter.ViewHolder> implements RongIM.GroupUserInfoProvider {

    private static final String TAG = "CrowdMemberAdapter";
    private String crowdId;
    private Context context;
    private List<Friend> list;

    public CrowdMemberAdapter(Context context, List<Friend> list, String crowdId) {
        this.context = context;
        this.list = list;
        this.crowdId = crowdId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_crowd_memer_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final Friend friend = list.get(position);
        holder.ivMemberHead.setImageURI(friend.getPictureUrl());
        holder.tvMemberNickName.setText(friend.getName());
        RongUserInfoManager.getInstance().setUserInfo(new UserInfo(friend.getUserId(), friend.getNickName(), Uri.parse(friend.getPictureUrl())));

        holder.ivMemberHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equalsIgnoreCase(friend.getUserId())) {//删除好友
                    Intent deleteIntent = new Intent(context, DeleteCrowdMemberActivity.class);
                    deleteIntent.putExtra("crowdId", crowdId);
                    context.startActivity(deleteIntent);
                } else if ("1".equalsIgnoreCase(friend.getUserId())) {//邀请好友
                    Intent addIntent = new Intent(context, AddCrowdMemberActivity.class);
                    addIntent.putExtra("crowdId", crowdId);
                    context.startActivity(addIntent);
                } else {//查看普通成员详情
                    Intent intent = new Intent(context, FriendDetailActivity.class);
                    intent.putExtra("type", FriendDetailActivity.GROUP);
                    intent.putExtra("userId", friend.getUserId());
                    UserInfo userInfo = new UserInfo(friend.getUserId(), friend.getName(), Uri.parse(friend.getPictureUrl()));
                    intent.putExtra("userInfo", userInfo);
                    context.startActivity(intent);
                }
            }
        });
    }

    public void setCrowdMemberAdapter(List<Friend> friends) {
        list.clear();
        if (null != friends && friends.size() > 0) {
            list.addAll(friends);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (null != list && list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public GroupUserInfo getGroupUserInfo(String s, String s1) {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_member_head)
        SimpleDraweeView ivMemberHead;
        @BindView(R.id.tv_member_nick_name)
        TextView tvMemberNickName;
        @BindView(R.id.ll_item)
        RelativeLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
