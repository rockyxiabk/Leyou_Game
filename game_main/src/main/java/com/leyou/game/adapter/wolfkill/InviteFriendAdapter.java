package com.leyou.game.adapter.wolfkill;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.bean.ContactBean;
import com.leyou.game.dao.Friend;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.rong.message.InviteJoinWolfKillMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Description : com.leyou.game.adapter.wolfkill
 *
 * @author : rocky
 * @Create Time : 2017/7/26 下午4:56
 * @Modified Time : 2017/7/26 下午4:56
 */
public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.ViewHolder> {
    private long roomId;
    private Context context;
    private List<Friend> list;
    private ClickResult listener;

    public InviteFriendAdapter(Context context, List<Friend> list, long roomId) {
        this.context = context;
        this.list = list;
        this.roomId = roomId;
    }

    public void setClickResult(ClickResult listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_wolf_kill_invite_friend_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Friend friend = list.get(position);
        holder.ivFriendHeaderImg.setImageURI(friend.getPictureUrl());
        holder.tvFriendName.setText(!TextUtils.isEmpty(friend.getRemarkName()) ? friend.getRemarkName() : friend.getNickName());
        holder.tvInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInviteMessage(friend);
            }
        });
    }

    private void sendInviteMessage(Friend friend) {
        InviteJoinWolfKillMessage message = InviteJoinWolfKillMessage.obtain("狼人杀", "您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！", roomId);
        Message message1 = Message.obtain(friend.getUserId(), Conversation.ConversationType.PRIVATE, message);
        RongIM.getInstance().sendMessage(message1, "您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！", "您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！", new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                ToastUtils.showToastShort("邀请成功");
                listener.inviteResult(true);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                listener.inviteResult(false);
            }
        });
    }

    public void updateAdapter(List<Friend> gameRankBeanList) {
        list.clear();
        if (null != gameRankBeanList && gameRankBeanList.size() > 0) {
            list.addAll(gameRankBeanList);
        }
        notifyDataSetChanged();
    }

    public void loadMoreData(List<Friend> gameRankBeanList) {
        if (null != gameRankBeanList && gameRankBeanList.size() > 0) {
            list.addAll(gameRankBeanList);
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
        @BindView(R.id.iv_friend_header_img)
        SimpleDraweeView ivFriendHeaderImg;
        @BindView(R.id.tv_friend_name)
        TextView tvFriendName;
        @BindView(R.id.tv_invite_friend)
        TextView tvInviteFriend;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickResult {
        void inviteResult(boolean result);
    }
}
