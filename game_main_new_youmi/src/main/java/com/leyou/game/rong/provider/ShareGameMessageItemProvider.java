package com.leyou.game.rong.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.GameDetailActivity_;
import com.leyou.game.activity.PlayGameActivity;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.rong.message.ShareGameMessage;

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
@ProviderTag(messageContent = ShareGameMessage.class)
public class ShareGameMessageItemProvider extends IContainerItemProvider.MessageProvider<ShareGameMessage> {

    @Override
    public void bindView(View view, int i, ShareGameMessage inviteJoinWolfKillMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        ShareGameMessage message = (ShareGameMessage) uiMessage.getContent();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.llInviteLeft.setVisibility(View.GONE);
            holder.llInviteRight.setVisibility(View.VISIBLE);
            holder.ivRightIcon.setImageURI(message.getIconUrl());
            holder.tvRightName.setText(message.getGameName());
            holder.tvRightDes.setText(message.getGameDes());
            holder.tvRightSource.setText(message.getSlogan());
        } else {
            holder.llInviteRight.setVisibility(View.GONE);
            holder.llInviteLeft.setVisibility(View.VISIBLE);
            holder.ivLeftIcon.setImageURI(message.getIconUrl());
            holder.tvLeftName.setText(message.getGameName());
            holder.tvLeftDes.setText(message.getGameDes());
            holder.tvLeftSource.setText(message.getSlogan());
        }
    }

    @Override
    public Spannable getContentSummary(ShareGameMessage inviteJoinWolfKillMessage) {
        return new SpannableString("您的好友邀请你一起来玩狼人杀游戏，快来进入房间吧！");
    }

    @Override
    public void onItemClick(View view, int i, ShareGameMessage inviteJoinWolfKillMessage, UIMessage uiMessage) {
        ShareGameMessage content = (ShareGameMessage) uiMessage.getContent();

        GameBean gameBean = new GameBean();
        gameBean.setName(content.getGameName());
        gameBean.setReadme(content.getGameDes());
        gameBean.setUniqueMark(content.getUniqueMark());
        gameBean.setGameUrl(content.getGameUrl());
        gameBean.setIconUrl(content.getIconUrl());
        gameBean.setScreenDirection(content.getScreenDirection());

        Intent intent = new Intent(view.getContext(), PlayGameActivity.class);
        intent.putExtra("game", gameBean);
        view.getContext().startActivity(intent);
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rc_custom_message_invite_join_wolf_kill_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    public class ViewHolder {
        @BindView(R.id.tv_right_name)
        TextView tvRightName;
        @BindView(R.id.tv_right_des)
        TextView tvRightDes;
        @BindView(R.id.iv_right_icon)
        SimpleDraweeView ivRightIcon;
        @BindView(R.id.tv_right_source)
        TextView tvRightSource;
        @BindView(R.id.ll_invite_right)
        LinearLayout llInviteRight;

        @BindView(R.id.tv_left_name)
        TextView tvLeftName;
        @BindView(R.id.tv_left_des)
        TextView tvLeftDes;
        @BindView(R.id.iv_left_icon)
        SimpleDraweeView ivLeftIcon;
        @BindView(R.id.tv_left_source)
        TextView tvLeftSource;
        @BindView(R.id.ll_invite_left)
        LinearLayout llInviteLeft;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
