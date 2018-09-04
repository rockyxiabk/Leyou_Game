package com.leyou.game.adapter.friend;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.CustomOnLongClickListener;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.rong.RongCloudEvent;
import com.leyou.game.rong.message.InviteJoinWolfKillMessage;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.widget.DragPointView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.NotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Description : com.leyou.game.adapter.friend
 *
 * @author : rocky
 * @Create Time : 2017/7/22 下午2:39
 * @Modified Time : 2017/7/22 下午2:39
 */
public class FriendConversationAdapter extends RecyclerView.Adapter<FriendConversationAdapter.ViewHolder> implements RongIM.UserInfoProvider, RongIM.GroupInfoProvider {
    private static final String TAG = "FriendConversationAdapter";
    private Context context;
    private List<Conversation> list;
    private CustomItemClickListener listener;
    private CustomOnLongClickListener onLongClickListener;

    public FriendConversationAdapter(Context context, List<Conversation> list) {
        this.context = context;
        this.list = list;
        RongIM.setUserInfoProvider(this, true);
    }

    public void setCustomOnItemListener(CustomItemClickListener listener) {
        this.listener = listener;
    }

    public void setCustomOnLongClickListener(CustomOnLongClickListener listener) {
        onLongClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = inflate(parent, R.layout.layout_friend_conversation_list);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        Conversation conversation = list.get(position);
        switch (conversation.getConversationType()) {
            case NONE:
                break;
            case PRIVATE:
                Friend friendInfo = getFriendInfo(conversation.getTargetId());
                holder.ivConversationHeader.setImageURI(friendInfo.getPictureUrl());
                holder.tvConversationName.setText(!TextUtils.isEmpty(friendInfo.getRemarkName()) ? friendInfo.getRemarkName() : friendInfo.getNickName());
                break;
            case DISCUSSION:
                break;
            case GROUP:
                Group groupInfo = getGroupInfo(conversation.getTargetId());
                LogUtil.e(TAG, groupInfo.getPortraitUri().toString());
                holder.ivConversationHeader.setImageURI(groupInfo.getPortraitUri());
                holder.tvConversationName.setText(groupInfo.getName());
                break;
            case CHATROOM:
                break;
            case CUSTOMER_SERVICE:
                break;
            case SYSTEM:
                holder.ivConversationHeader.setImageResource(R.mipmap.icon_system_notice);
                holder.tvConversationName.setText("系统消息");
                break;
        }

        MessageContent messageContent = conversation.getLatestMessage();
        if (messageContent instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) messageContent;
            holder.tvConversationDes.setText(textMessage.getContent());
        } else if (messageContent instanceof InformationNotificationMessage) {
            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
            holder.tvConversationDes.setText(informationNotificationMessage.getMessage());
        } else if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            holder.tvConversationDes.setText(contactNotificationMessage.getMessage());
        } else if (messageContent instanceof ImageMessage) {
            holder.tvConversationDes.setText("[图片]");
        } else if (messageContent instanceof VoiceMessage) {
            holder.tvConversationDes.setText("[语音信息]");
        } else if (messageContent instanceof LocationMessage) {
            holder.tvConversationDes.setText("[位置]");
        } else if (messageContent instanceof FileMessage) {
            holder.tvConversationDes.setText("[文件]");
        } else if (messageContent instanceof InviteJoinWolfKillMessage) {
            holder.tvConversationDes.setText("您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！");
        } else {
            holder.tvConversationDes.setText("[未知消息]");
        }
        if (conversation.getUnreadMessageCount() == 0) {
            holder.tvMessageTips.setVisibility(View.GONE);
        } else if (conversation.getUnreadMessageCount() > 0 && conversation.getUnreadMessageCount() < 100) {
            holder.tvMessageTips.setVisibility(View.VISIBLE);
            holder.tvMessageTips.setText("" + conversation.getUnreadMessageCount());
        } else {
            holder.tvMessageTips.setVisibility(View.VISIBLE);
            holder.tvMessageTips.setText(context.getString(R.string.no_read_message));
        }
        holder.tvTime.setText(DataUtil.msg_distance_time(conversation.getReceivedTime(), System.currentTimeMillis()));
        switch (conversation.getNotificationStatus()) {
            case DO_NOT_DISTURB:
                holder.ivMessageNotTips.setVisibility(View.VISIBLE);
                break;
            case NOTIFY:
                holder.ivMessageNotTips.setVisibility(View.GONE);
                break;
        }
        if (conversation.isTop()) {
            holder.reItemColor.setBackgroundResource(R.drawable.item_top_selector);
        } else {
            holder.reItemColor.setBackgroundResource(R.drawable.item_selector);
        }
        holder.reItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(null, position);
            }
        });
        holder.reItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickListener.onItemLongClick(null, position);
                return true;
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

    public void setAdapter(List<Conversation> currentList) {
        if (null != currentList && currentList.size() > 0) {
            list.clear();
            list.addAll(currentList);
        }
        notifyDataSetChanged();
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        if (null == userInfo) {
            userInfo = new UserInfo(userId, "陌生人", Uri.parse("http://www/baidu.com"));
        }
        return userInfo;
    }

    private Friend getFriendInfo(String userId) {
        Friend friend = DBUtil.getInstance(context).queryFriendByUserId(userId);
        if (null == friend) {
            friend = new Friend();
            UserInfo userInfo = getUserInfo(userId);
            friend.setUserId(userInfo.getUserId());
            friend.setNickName(userInfo.getName());
            friend.setPictureUrl(userInfo.getPortraitUri().toString());
        }
        return friend;
    }

    @Override
    public Group getGroupInfo(String groupId) {
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(groupId);
        if (null == groupInfo) {
            groupInfo = new Group(groupId, "群聊", Uri.parse("http://www/baidu.com"));
        }
        return groupInfo;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_contact_item)
        RelativeLayout reItem;
        @BindView(R.id.re_item_color)
        RelativeLayout reItemColor;
        @BindView(R.id.iv_conversation_header)
        SimpleDraweeView ivConversationHeader;
        @BindView(R.id.tv_message_tips)
        DragPointView tvMessageTips;
        @BindView(R.id.tv_conversation_name)
        TextView tvConversationName;
        @BindView(R.id.tv_conversation_des)
        TextView tvConversationDes;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_message_not_tips)
        ImageView ivMessageNotTips;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
