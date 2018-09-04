package com.leyou.game.activity.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.activity.PictureDetailActivity;
import com.leyou.game.adapter.friend.FriendPlayedGameAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.GameBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.FriendDeleteEvent;
import com.leyou.game.ipresenter.friend.IFriendDetail;
import com.leyou.game.presenter.friend.FriendDetailActivityPresenter;
import com.leyou.game.rong.RongCloudEvent;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.CustomDialog;
import com.leyou.game.widget.dialog.EditNickNameDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Description : 好友详情
 *
 * @author : rocky
 * @Create Time : 2017/7/23 下午4:40
 * @Modified By: rocky
 * @Modified Time : 2017/7/23 下午4:40
 */
public class FriendDetailActivity extends BaseActivity implements IFriendDetail {

    public static final int PRIVATE = 1;
    public static final int GROUP = 3;

    @BindView(R.id.iv_contact_header)
    SimpleDraweeView ivContactHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.re_set_remark)
    RelativeLayout reSetRemark;
    @BindView(R.id.tv_friend_phone_number)
    TextView tvFriendPhoneNumber;
    @BindView(R.id.re_phone)
    RelativeLayout rePhone;
    @BindView(R.id.tv_friend_address)
    TextView tvFriendAddress;
    @BindView(R.id.recycler_game)
    RecyclerView recyclerGame;
    @BindView(R.id.ll_played_game)
    LinearLayout llPlayedGame;
    @BindView(R.id.btn_send_message)
    Button btnSendMessage;
    @BindView(R.id.btn_friend_status)
    Button btnFriendStatus;
    @BindView(R.id.btn_friend_delete)
    Button btnFriendDelete;
    private UserInfo userInfo;
    private Friend friend = new Friend();
    private String userId;
    private FriendDetailActivityPresenter presenter;
    private List<GameBean> gameList = new ArrayList<>();
    private FriendPlayedGameAdapter adapter;
    private int type;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_friend_detail;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        userId = getIntent().getStringExtra("userId");
        userInfo = getIntent().getParcelableExtra("userInfo");
        friend.setNickName(userInfo.getName());
        friend.setPictureUrl(userInfo.getPortraitUri().toString());
        friend.setUserId(userId);
        Friend queryFriendByUserId = DBUtil.getInstance(this).queryFriendByUserId(userId);
        if (null != queryFriendByUserId) {
            friend = queryFriendByUserId;
            setFriendUI();
        }

        adapter = new FriendPlayedGameAdapter(this, gameList);
        recyclerGame.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerGame.setAdapter(adapter);

    }

    private void setFriendUI() {
        ivContactHeader.setImageURI(friend.getPictureUrl());
        tvName.setText(!TextUtils.isEmpty(friend.getRemarkName()) ? friend.getRemarkName() : friend.getNickName());
        tvNickName.setText("昵称：" + friend.getNickName());
        tvFriendPhoneNumber.setText(friend.getPhone());
        tvFriendAddress.setText(friend.getPhoneAddress());
        btnFriendDelete.setVisibility(View.GONE);
        btnFriendStatus.setVisibility(View.GONE);
        reSetRemark.setVisibility(View.GONE);
        switch (friend.getStatus()) {
            case Friend.NO_SYSTEM://不在平台内 显示邀请好友
                btnSendMessage.setVisibility(View.GONE);
                btnFriendStatus.setVisibility(View.VISIBLE);
                btnFriendStatus.setText("邀请好友");
                btnFriendDelete.setVisibility(View.GONE);
                break;
            case Friend.FRIEND://我的好友
                reSetRemark.setVisibility(View.VISIBLE);
                tvName.setVisibility(View.VISIBLE);
                tvNickName.setVisibility(View.VISIBLE);
                btnSendMessage.setVisibility(View.VISIBLE);
                btnFriendStatus.setVisibility(View.GONE);
                btnFriendDelete.setVisibility(View.VISIBLE);
                break;
            case Friend.ADDING_FRIEND://是否通过验证
                btnSendMessage.setVisibility(View.GONE);
                btnFriendStatus.setVisibility(View.VISIBLE);
                btnFriendStatus.setText("通过验证");
                btnFriendDelete.setVisibility(View.GONE);
                break;
            case Friend.SYSTEM_NO_FRIEND://平台内非好友
                btnSendMessage.setVisibility(View.GONE);
                btnFriendStatus.setVisibility(View.VISIBLE);
                btnFriendStatus.setText("添加好友");
                btnFriendDelete.setVisibility(View.GONE);
                break;
            case Friend.ADDING_WAITING_CONFIRM://等待验证
                btnSendMessage.setVisibility(View.GONE);
                btnFriendStatus.setVisibility(View.VISIBLE);
                btnFriendStatus.setText("等待验证");
                btnFriendStatus.setEnabled(false);
                break;
        }
        if (friend.getUserId().equalsIgnoreCase(UserData.getInstance().getId())) {
            reSetRemark.setVisibility(View.GONE);
            btnSendMessage.setVisibility(View.GONE);
            btnFriendStatus.setVisibility(View.GONE);
            btnFriendDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPresenter() {
        presenter = new FriendDetailActivityPresenter(this, this, userId);
    }

    @OnClick({R.id.iv_order_pay_back, R.id.iv_contact_header, R.id.re_set_remark, R.id.re_phone, R.id.btn_send_message, R.id.btn_friend_status, R.id.btn_friend_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.iv_contact_header:
                Intent headerIntent = new Intent(this, PictureDetailActivity.class);
                headerIntent.putExtra("friend", friend);
                startActivity(headerIntent);
                break;
            case R.id.re_set_remark:
                if (null != friend && !TextUtils.isEmpty(friend.getRemarkName())) {
                    EditNickNameDialog editNickNameDialog = new EditNickNameDialog(this, "修改备注", friend.getRemarkName(), 1);
                    editNickNameDialog.setOnSetNickNameListener(new EditNickNameDialog.setNickNameListener() {
                        @Override
                        public void setNickName(String name) {
                            friend.setRemarkName(name);
                            setFriendUI();
                            presenter.setRemarkName(friend.getUserId(), name);
                        }
                    });
                    editNickNameDialog.show();
                } else {
                    EditNickNameDialog editNickNameDialog = new EditNickNameDialog(this, "设置备注", "", 1);
                    editNickNameDialog.setOnSetNickNameListener(new EditNickNameDialog.setNickNameListener() {
                        @Override
                        public void setNickName(String name) {
                            friend.setRemarkName(name);
                            setFriendUI();
                            presenter.setRemarkName(friend.getUserId(), name);
                        }
                    });
                    editNickNameDialog.show();
                }
                break;
            case R.id.re_phone:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + friend.getPhone()));
                startActivity(intent);
                break;
            case R.id.btn_send_message:
                finishCurrentActivity();
                switch (type) {
                    case PRIVATE:
                        break;
                    case GROUP:
                        RongCloudEvent.getInstance().removeActivity(10090);
                        break;
                }
                RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, userId, userInfo.getName());
                break;
            case R.id.btn_friend_status:
                switch (friend.getStatus()) {
                    case Friend.NO_SYSTEM:
                        break;
                    case Friend.FRIEND:
                        break;
                    case Friend.ADDING_FRIEND:
                        presenter.agreeAddFriend(friend, 1);
                        break;
                    case Friend.SYSTEM_NO_FRIEND:
                        presenter.addFriend(friend);
                        break;
                    case Friend.ADDING_WAITING_CONFIRM:
                        break;
                }
                break;
            case R.id.btn_friend_delete:
                final CustomDialog customDialog = new CustomDialog(this, getResources().getString(R.string.friend_delete), "确定将联系人 \"" + friend.getRemarkName() + "\" 删除，同时删除与该联系人的所有聊天记录", getResources().getString(R.string.cancel), getResources().getString(R.string.delete));
                customDialog.setCustomListener(new CustomDialog.CustomListener() {
                    @Override
                    public void cancel() {
                        customDialog.dismiss();
                    }

                    @Override
                    public void confirm() {
                        customDialog.dismiss();
                        presenter.deleteFriend(friend.getUserId());
                    }
                });
                customDialog.show();
                break;
        }
    }

    @Override
    public void setFriendInfo(Friend friend) {
        if (null != friend) {
            this.friend = friend;
            setFriendUI();
            Friend queryFriendByUserId = DBUtil.getInstance(this).queryFriendByUserId(friend.getUserId());
            if (null != queryFriendByUserId && Friend.FRIEND == queryFriendByUserId.getSource()) {
                friend.setSource(Friend.FRIEND);
            }
            DBUtil.getInstance(this).updateFriend(friend);
            RongUserInfoManager.getInstance().setUserInfo(new UserInfo(friend.getUserId(), friend.getNickName(), Uri.parse(friend.getPictureUrl())));
        }
    }

    @Override
    public void showDeleteFriendResult(boolean result, String userId) {
        if (result) {
            showMessageToast("好友删除成功");
            DBUtil.getInstance(this).deleteFriend(DBUtil.getInstance(this).queryFriendByUserId(userId));
            EventBus.getDefault().post(new FriendDeleteEvent(true, userId));
            finishCurrentActivity();
        } else {
            showMessageToast("好友删除失败，请检查网络配置");
        }
    }

    @Override
    public void setGameList(boolean isLoad, List<GameBean> list) {
        if (isLoad) {
            llPlayedGame.setVisibility(View.VISIBLE);
            this.gameList = list;
            adapter.setAdapterData(list);
        } else {
            llPlayedGame.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }
}
