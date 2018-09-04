package com.leyou.game.rong.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.rong.message.DiamondRedPacketMessage;
import com.leyou.game.util.ToastUtils;

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
@ProviderTag(messageContent = DiamondRedPacketMessage.class)
public class DiamondRedPacketMessageItemProvider extends IContainerItemProvider.MessageProvider<DiamondRedPacketMessage> {

    @Override
    public void bindView(View view, int i, DiamondRedPacketMessage inviteJoinWolfKillMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
//        DiamondRedPacketMessage message = (DiamondRedPacketMessage) uiMessage.getContent();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.llInviteLeft.setVisibility(View.GONE);
            holder.llInviteRight.setVisibility(View.VISIBLE);
        } else {
            holder.llInviteRight.setVisibility(View.GONE);
            holder.llInviteLeft.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Spannable getContentSummary(DiamondRedPacketMessage inviteJoinWolfKillMessage) {
        return new SpannableString("[钻石红包]");
    }

    @Override
    public void onItemClick(View view, int i, DiamondRedPacketMessage inviteJoinWolfKillMessage, UIMessage uiMessage) {
//        DiamondRedPacketMessage content = (DiamondRedPacketMessage) uiMessage.getContent();
//
//        GameBean gameBean = new GameBean();
//        gameBean.setName(content.getGameName());
//        gameBean.setReadme(content.getGameDes());
//        gameBean.setUniqueMark(content.getUniqueMark());
//        gameBean.setGameUrl(content.getGameUrl());
//        gameBean.setIconUrl(content.getIconUrl());
//        gameBean.setScreenDirection(content.getScreenDirection());
//
//        Intent intent = new Intent(view.getContext(), GameDetailActivity_.class);
//        intent.putExtra("game", gameBean);
//        view.getContext().startActivity(intent);
        ToastUtils.showToastShort("暂未实现");
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rc_custom_message_diamond_red_packet_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    public class ViewHolder {
        @BindView(R.id.re_invite_right)
        RelativeLayout llInviteRight;

        @BindView(R.id.re_invite_left)
        RelativeLayout llInviteLeft;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
