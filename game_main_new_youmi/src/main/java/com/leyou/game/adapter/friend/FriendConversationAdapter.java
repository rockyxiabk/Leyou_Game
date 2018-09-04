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
import com.leyou.game.fragment.FriendFragment_;
import com.leyou.game.ipresenter.friend.IFriendFragment;
import com.leyou.game.rong.RongCloudEvent;
import com.leyou.game.rong.message.DiamondRedPacketMessage;
import com.leyou.game.rong.message.InviteJoinWolfKillMessage;
import com.leyou.game.rong.message.ShareGameMessage;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.RongYunUtil;
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
        MessageContent messageContent = conversation.getLatestMessage();
        UserInfo userInfo = messageContent.getUserInfo();
        if (null == userInfo) {
            userInfo = new UserInfo("admin", "", Uri.parse("http://baidu.com"));
        }
        switch (conversation.getConversationType()) {
            case NONE:
                break;
            case PRIVATE:
                Friend friendInfo = getFriendInfo(conversation.getTargetId());
                holder.ivConversationHeader.setImageURI(friendInfo.getHeadImgUrl());
                holder.tvConversationName.setText(!TextUtils.isEmpty(friendInfo.getComment()) ?
                        friendInfo.getComment() : friendInfo.getNickname());
                break;
            case DISCUSSION:
                break;
            case GROUP:
                Group groupInfo = getGroupInfo(conversation.getTargetId());
                LogUtil.e(TAG, groupInfo.getPortraitUri().toString());
                holder.ivConversationHeader.setImageURI(groupInfo.getPortraitUri());
                String name = "";
                if (!TextUtils.isEmpty(groupInfo.getName())) {
                    name = groupInfo.getName().length() > 10 ? "群聊" : groupInfo.getName();
                } else {
                    name = "群聊";
                }
                holder.tvConversationName.setText(name);
                break;
            case CHATROOM:
                break;
            case CUSTOMER_SERVICE:
                break;
            case SYSTEM:
                holder.ivConversationHeader.setImageResource(R.mipmap.icon_system_notice);
                if ("admin".equalsIgnoreCase(conversation.getSenderUserId())) {
                    holder.tvConversationName.setText("群聊申请");
                } else {
                    holder.tvConversationName.setText("好友申请");
                }
                break;
        }

        if (messageContent instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) messageContent;
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = "";
                    if (userInfo.getName().equalsIgnoreCase("admin")) {
                        text = textMessage.getContent();
                    } else {
                        text = userInfo.getName() + ":" + textMessage.getContent();
                    }
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText(textMessage.getContent());
                    break;
            }
        } else if (messageContent instanceof InformationNotificationMessage) {
            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
            holder.tvConversationDes.setText(informationNotificationMessage.getMessage());
        } else if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = userInfo.getName() + ":" + contactNotificationMessage.getMessage();
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText(contactNotificationMessage.getMessage());
                    break;
            }
        } else if (messageContent instanceof ImageMessage) {
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = userInfo.getName() + ":[图片]";
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText("[图片]");
                    break;
            }
        } else if (messageContent instanceof VoiceMessage) {
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = userInfo.getName() + ":[语音信息]";
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText("[语音信息]");
                    break;
            }
        } else if (messageContent instanceof LocationMessage) {
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = userInfo.getName() + ":[位置]";
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText("[位置]");
                    break;
            }
        } else if (messageContent instanceof FileMessage) {
            switch (conversation.getConversationType()) {
                case PRIVATE:
                    holder.tvConversationDes.setText("[文件]");
                    break;
                case GROUP:
                    String text = userInfo.getName() + ":[文件]";
                    holder.tvConversationDes.setText(text);
                    break;
            }
        } else if (messageContent instanceof InviteJoinWolfKillMessage) {
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = userInfo.getName() + ":您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！";
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText("您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！");
                    break;
            }
        } else if (messageContent instanceof DiamondRedPacketMessage) {
            switch (conversation.getConversationType()) {
                case PRIVATE:
                    holder.tvConversationDes.setText("[钻石红包]");
                    break;
                case GROUP:
                    String text = userInfo.getName() + ":[钻石红包]";
                    holder.tvConversationDes.setText(text);
                    break;
            }
        } else if (messageContent instanceof ShareGameMessage) {
            ShareGameMessage shareGameMessage = (ShareGameMessage) messageContent;
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = userInfo.getName() + ":" + shareGameMessage.getGameName() + ":" + shareGameMessage.getSlogan();
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText(shareGameMessage.getGameName() + ":" + shareGameMessage.getSlogan());
                    break;
            }
        } else {
            switch (conversation.getConversationType()) {
                case GROUP:
                    String text = userInfo.getName() + ":[未知消息]";
                    holder.tvConversationDes.setText(text);
                    break;
                default:
                    holder.tvConversationDes.setText("[未知消息]");
                    break;
            }
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
            userInfo = new UserInfo(userId, "陌生人", Uri.parse("http://www.baidu.com"));
        }
        return userInfo;
    }

    private Friend getFriendInfo(String userId) {
        Friend friend = DBUtil.getInstance(context).queryFriendByUserId(userId);
        if (null == friend) {
            friend = new Friend();
            UserInfo userInfo = getUserInfo(userId);
            friend.setUserId(userInfo.getUserId());
            friend.setNickname(userInfo.getName());
            friend.setHeadImgUrl(userInfo.getPortraitUri().toString());
            RongCloudEvent.getInstance().getUserInfo(userId);
        }
        return friend;
    }

    @Override
    public Group getGroupInfo(String groupId) {
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(groupId);
        if (null == groupInfo) {
            groupInfo = new Group(groupId, "群聊", Uri.parse("http://www/baidu.com"));
            RongCloudEvent.getInstance().getGroupInfo(groupId);
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
