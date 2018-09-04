package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.FriendApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.Observer;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/7/25 上午10:12
 * @Modified Time : 2017/7/25 上午10:12
 */
public class ConversationSettingDialog extends BaseDialog {

    @BindView(R.id.ll_message_read_status)
    LinearLayout llMessageReadStatus;
    @BindView(R.id.tv_conversation_is_top)
    TextView tvConversationIsTop;
    @BindView(R.id.tv_conversation_delete)
    TextView tvConversationDelete;
    @BindView(R.id.tv_conversation_is_read)
    TextView tvConversationRead;
    private Context context;
    private Conversation conversation;
    private ConversationSettingListener listener;

    public ConversationSettingDialog(Context context, Conversation conversation) {
        super(context);
        this.context = context;
        this.conversation = conversation;
        setCanceledOnTouchOutside(true);
    }

    public void setConversationListener(ConversationSettingListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_conversation_setting;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        boolean top = conversation.isTop();
        if (top) {
            tvConversationIsTop.setText(context.getString(R.string.friend_conversation_top_cancel));
        } else {
            tvConversationIsTop.setText(context.getString(R.string.friend_conversation_top));
        }
        int unreadMessageCount = conversation.getUnreadMessageCount();
        if (unreadMessageCount > 0) {
            llMessageReadStatus.setVisibility(View.VISIBLE);
            tvConversationRead.setText(context.getString(R.string.friend_conversation_read));
        } else {
            llMessageReadStatus.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_conversation_is_read, R.id.tv_conversation_is_top, R.id.tv_conversation_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_conversation_is_read:
                RongIM.getInstance().clearMessagesUnreadStatus(conversation.getConversationType(), conversation.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        listener.success();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        listener.failed();
                    }
                });

                break;
            case R.id.tv_conversation_is_top:
                setCrowdTop();
                RongIM.getInstance().setConversationToTop(conversation.getConversationType(), conversation.getTargetId(), !conversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        listener.success();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        listener.failed();
                    }
                });
                break;
            case R.id.tv_conversation_delete:
                RongIM.getInstance().removeConversation(conversation.getConversationType(), conversation.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        listener.success();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        listener.failed();
                    }
                });
                break;
        }
    }

    private void setCrowdTop() {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).setCrowdTop(conversation.getTargetId(), conversation.isTop() ? 0 : 1), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result result) {

            }
        });
    }

    public interface ConversationSettingListener {
        void success();

        void failed();
    }
}
