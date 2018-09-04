package com.leyou.game.rong.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.leyou.game.R;
import com.leyou.game.rong.message.InviteJoinWolfKillMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Description : com.leyou.game.widget.rong
 *
 * @author : rocky
 * @Create Time : 2017/7/28 下午3:36
 * @Modified Time : 2017/7/28 下午3:36
 */
@ProviderTag(messageContent = InviteJoinWolfKillMessage.class)
public class InviteJoinWolfKillMessageItemProvider extends IContainerItemProvider.MessageProvider<InviteJoinWolfKillMessage> {
    @Override
    public void bindView(View view, int i, InviteJoinWolfKillMessage inviteJoinWolfKillMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.llInviteLeft.setVisibility(View.GONE);
            holder.llInviteRight.setVisibility(View.VISIBLE);
        } else {
            holder.llInviteRight.setVisibility(View.GONE);
            holder.llInviteLeft.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Spannable getContentSummary(InviteJoinWolfKillMessage inviteJoinWolfKillMessage) {
        return new SpannableString("您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！");
    }

    @Override
    public void onItemClick(View view, int i, InviteJoinWolfKillMessage inviteJoinWolfKillMessage, UIMessage uiMessage) {
        // TODO: 2017/9/19 实现狼人杀跳转
//        InviteJoinWolfKillMessage content = (InviteJoinWolfKillMessage) uiMessage.getContent();
//        long gameRoomId = content.getGameRoomId();
//        Intent intent = new Intent(view.getContext(), WolfKillFightActivity.class);
//        intent.putExtra("roomId",gameRoomId);
//        view.getContext().startActivity(intent);
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rc_custom_message_invite_join_wolf_kill_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    public class ViewHolder {
        @BindView(R.id.ll_invite_left)
        LinearLayout llInviteLeft;
        @BindView(R.id.ll_invite_right)
        LinearLayout llInviteRight;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
