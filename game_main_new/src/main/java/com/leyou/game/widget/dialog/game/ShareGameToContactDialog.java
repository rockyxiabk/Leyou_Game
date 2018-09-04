package com.leyou.game.widget.dialog.game;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.ChooseContactsAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.dao.Friend;
import com.leyou.game.rong.message.ShareGameMessage;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.PhoneContactComparator;
import com.leyou.game.util.PinyinComparator;
import com.leyou.game.util.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Description : 内部好友分享游戏
 *
 * @author : rocky
 * @Create Time : 2017/11/10 下午7:02
 * @Modified Time : 2017/11/10 下午7:02
 */
public class ShareGameToContactDialog extends BaseDialog implements CustomItemClickListener {
    private GameBean gameBean;
    private Context context;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<Friend> list = new ArrayList<>();
    private ChooseContactsAdapter adapter;

    public ShareGameToContactDialog(Context context, GameBean gameBean) {
        super(context);
        this.context = context;
        this.gameBean = gameBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_share_game_to_conversation;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        adapter = new ChooseContactsAdapter(context, list, gameBean);
        adapter.setListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        initData();
    }

    private void initData() {
        list = DBUtil.getInstance(context).queryMyFriendList();
        Collections.sort(list, new PinyinComparator());
        if (null != list && list.size() > 0) {
            adapter.setMyFriendAdapter(list);
        } else {
            ToastUtils.showToastShort("暂无联系人，快去邀请好友吧～");
            dismiss();
        }
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        Friend friend = list.get(position);
        if (!TextUtils.isEmpty(friend.getUserId())) {
            ShareGameMessage gameMessage = ShareGameMessage.obtain(gameBean.getName(), gameBean.getIconUrl(),
                    gameBean.getRecommend(), gameBean.getGameUrl(), gameBean.getUniqueMark()
                    , gameBean.getScreenDirection(), gameBean.getPropaganda());
            Message obtain = Message.obtain(friend.getUserId(), Conversation.ConversationType.PRIVATE, gameMessage);
            RongIM.getInstance().sendMessage(obtain, "您的好友发现了一款好玩的游戏，快来试玩吧", gameBean.getPropaganda(), new IRongCallback.ISendMediaMessageCallback() {
                @Override
                public void onProgress(Message message, int i) {

                }

                @Override
                public void onCanceled(Message message) {

                }

                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onSuccess(Message message) {
                    ToastUtils.showToastShort("分享成功");
                    dismiss();
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    ToastUtils.showToastShort("分享失败");
                    dismiss();
                }
            });
        }
    }
}
