package com.leyou.game.activity.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.leyou.game.R;
import com.leyou.game.adapter.friend.CrowdMemberAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.CrowdEvent;
import com.leyou.game.event.MainTabEvent;
import com.leyou.game.ipresenter.friend.ICrowdDetailActivity;
import com.leyou.game.presenter.friend.CrowdDetailPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.CustomDialog;
import com.leyou.game.widget.dialog.EditNickNameDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/**
 * Description : 群聊管理页面
 *
 * @author : rocky
 * @Create Time : 2017/8/3 上午10:59
 * @Modified By: rocky
 * @Modified Time : 2017/8/3 上午10:59
 */
public class CrowdDetailActivity extends BaseActivity implements ICrowdDetailActivity {

    @BindView(R.id.iv_order_pay_back)
    ImageView ivOrderPayBack;
    @BindView(R.id.tv_crowd_name)
    TextView tvCrowdName;
    @BindView(R.id.recycler_crowd_member)
    RecyclerView recyclerCrowdMember;
    @BindView(R.id.tv_crowd_nick_name)
    TextView tvCrowdNickName;
    @BindView(R.id.re_set_crowd_remark)
    RelativeLayout reSetCrowdRemark;
    @BindView(R.id.tv_crowd_my_nick_name)
    TextView tvCrowdMyNickName;
    @BindView(R.id.re_set_my_nick_name)
    RelativeLayout reSetMyNickName;
    @BindView(R.id.toggle_message_tips)
    ToggleButton toggleMessageTips;
    @BindView(R.id.toggle_conversation_top)
    ToggleButton toggleConversationTop;
    @BindView(R.id.btn_logout_crowd)
    Button btnLogoutCrowd;
    private CrowdMemberAdapter adapter;
    private List<Friend> crowdMemberList = new ArrayList<>();
    private String crowdId;
    private CrowdDetailPresenter presenter;
    private Crowd crowd;
    private Crowd editCrowd = new Crowd();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCrowdInfo(CrowdEvent event) {
        if (event.isDeleteResult()) {
            finishCurrentActivity();
        }
        if (event.isInviteResult()) {
            finishCurrentActivity();
        }
        if (event.getDeleteCrowdMember() > 0) {
            presenter.getCrowdNumber();
        }
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_crowd_detail;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        crowdId = intent.getStringExtra("targetId");
        crowd = intent.getParcelableExtra("crowd");

        adapter = new CrowdMemberAdapter(this, crowdMemberList, crowdId);
        recyclerCrowdMember.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerCrowdMember.setAdapter(adapter);

        toggleConversationTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleConversationTop.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_on));
                    editCrowd.setTop(1);
                    crowd.setTop(1);
                    presenter.editCrowdInfo(editCrowd);
                    presenter.setMessageTop(crowdId, true);
                } else {
                    toggleConversationTop.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
                    editCrowd.setTop(0);
                    crowd.setTop(0);
                    presenter.editCrowdInfo(editCrowd);
                    presenter.setMessageTop(crowdId, false);
                }
            }
        });
        toggleMessageTips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_on));
                    editCrowd.setListenMessage(1);
                    crowd.setListenMessage(1);
                    presenter.editCrowdInfo(editCrowd);
                    presenter.setMessageTips(crowdId, Conversation.ConversationNotificationStatus.DO_NOT_DISTURB);
                } else {
                    toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
                    editCrowd.setListenMessage(0);
                    crowd.setListenMessage(0);
                    presenter.editCrowdInfo(editCrowd);
                    presenter.setMessageTips(crowdId, Conversation.ConversationNotificationStatus.NOTIFY);
                }
            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new CrowdDetailPresenter(this, this, crowdId);
    }

    @OnClick({R.id.iv_order_pay_back, R.id.re_set_crowd_remark, R.id.re_set_my_nick_name, R.id.btn_logout_crowd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.re_set_crowd_remark:
                EditNickNameDialog editCrowdNameDialog = new EditNickNameDialog(this, "修改群名称", crowd.getCrowdName(), 2);
                editCrowdNameDialog.setOnSetNickNameListener(new EditNickNameDialog.setNickNameListener() {
                    @Override
                    public void setNickName(String name) {
                        tvCrowdNickName.setText(name);
                        crowd.setCrowdName(name);
                        editCrowd.setCrowdName(name);
                        presenter.editCrowdInfo(editCrowd);
                    }
                });
                editCrowdNameDialog.show();
                break;
            case R.id.re_set_my_nick_name:
                EditNickNameDialog editNickNameDialog = new EditNickNameDialog(this, "我的群昵称", crowd.getMyName(), 3);
                editNickNameDialog.setOnSetNickNameListener(new EditNickNameDialog.setNickNameListener() {
                    @Override
                    public void setNickName(String name) {
                        tvCrowdMyNickName.setText(name);
                        crowd.setMyName(name);
                        editCrowd.setMyName(name);
                        presenter.editCrowdInfo(editCrowd);
                    }
                });
                editNickNameDialog.show();
                break;
            case R.id.btn_logout_crowd:
                final CustomDialog customDialog = new CustomDialog(this, getResources().getString(R.string.friend_delete_crowd), "确定退出并删除群聊 \"" + crowd.getCrowdName() + "\" ", getResources().getString(R.string.cancel), getResources().getString(R.string.delete));
                customDialog.setCustomListener(new CustomDialog.CustomListener() {
                    @Override
                    public void cancel() {
                        customDialog.dismiss();
                    }

                    @Override
                    public void confirm() {
                        customDialog.dismiss();
                        presenter.quitCrowd();
                    }
                });
                customDialog.show();
                break;
        }
    }

    @Override
    public void showCrowdMemberAdapter(List<Friend> friends) {
        if (null != friends && friends.size() > 0) {
            crowdMemberList.clear();
            crowdMemberList.addAll(friends);
            adapter.setCrowdMemberAdapter(friends);
        }
    }

    @Override
    public void setCrowdInfo(Crowd crowd) {
        crowd.setCrowdId(crowdId);
        this.editCrowd = crowd;
        this.crowd = crowd;
        tvCrowdMyNickName.setText(crowd.getMyName());
        tvCrowdNickName.setText(crowd.getCrowdName());
        if (crowd.getListenMessage() != 0) {
            toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_on));
            toggleMessageTips.setChecked(true);
        } else {
            toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
            toggleMessageTips.setChecked(false);
        }
        if (crowd.getTop() != 0) {
            toggleConversationTop.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_on));
            toggleConversationTop.setChecked(true);
        } else {
            toggleConversationTop.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
            toggleConversationTop.setChecked(false);
        }
        RongIM.getInstance().refreshGroupInfoCache(new Group(crowdId, crowd.getCrowdName(), Uri.parse(crowd.getPictureUrl())));
        DBUtil.getInstance(this).updateCrowd(crowd);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
