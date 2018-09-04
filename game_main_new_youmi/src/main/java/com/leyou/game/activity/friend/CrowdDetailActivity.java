package com.leyou.game.activity.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.adapter.friend.CrowdMemberAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.CrowdEvent;
import com.leyou.game.ipresenter.friend.ICrowdDetailActivity;
import com.leyou.game.presenter.friend.CrowdDetailPresenter;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.CustomDialog;
import com.leyou.game.widget.dialog.mine.EditTextDialog;
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
    @BindView(R.id.tv_crowd_edit)
    TextView tvCrowdEdit;
    @BindView(R.id.iv_crowd_header)
    SimpleDraweeView ivCrowdHeader;
    @BindView(R.id.tv_crowd_id_no)
    TextView tvCrowdIdNo;
    @BindView(R.id.tv_crowd_introduce)
    TextView tvCrowdIntroduce;
    @BindView(R.id.recycler_crowd_member)
    RecyclerView recyclerCrowdMember;
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
                    editCrowd.setIsTop(1);
                    crowd.setIsTop(1);
                    presenter.editCrowdTop(editCrowd);
                    presenter.setMessageTop(crowdId, true);
                } else {
                    toggleConversationTop.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
                    editCrowd.setIsTop(0);
                    crowd.setIsTop(0);
                    presenter.editCrowdTop(editCrowd);
                    presenter.setMessageTop(crowdId, false);
                }
            }
        });
        toggleMessageTips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_on));
                    editCrowd.setIsShielding(1);
                    crowd.setIsShielding(1);
                    presenter.editCrowdIsSheliding(editCrowd);
                    presenter.setMessageTips(crowdId, Conversation.ConversationNotificationStatus.DO_NOT_DISTURB);
                } else {
                    toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
                    editCrowd.setIsShielding(0);
                    crowd.setIsShielding(0);
                    presenter.editCrowdIsSheliding(editCrowd);
                    presenter.setMessageTips(crowdId, Conversation.ConversationNotificationStatus.NOTIFY);
                }
            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new CrowdDetailPresenter(this, this, crowdId);
    }

    @OnClick({R.id.iv_order_pay_back, R.id.tv_crowd_edit, R.id.re_set_my_nick_name, R.id.btn_logout_crowd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_order_pay_back:
                finishCurrentActivity();
                break;
            case R.id.tv_crowd_edit:
                Intent intent3 = new Intent(this, CreateCrowdInfoActivity.class);
                intent3.putExtra("type", 2);
                intent3.putExtra("crowdInfo", crowd);
                startActivity(intent3);
                break;
//            case R.id.re_set_crowd_remark:
//                EditTextDialog editTextDialog = new EditTextDialog(this, "修改群名称", crowd.getName(), getResources().getString(R.string.confirm));
//                editTextDialog.setOnConfirmListener(new EditTextDialog.OnConfirmListener() {
//                    @Override
//                    public void onFinished(String name) {
//                        tvCrowdNickName.setText(name);
//                        crowd.setName(name);
//                        editCrowd.setName(name);
//                        presenter.editCrowdName(crowd);
//                    }
//                });
//                editTextDialog.show();
//                break;
            case R.id.re_set_my_nick_name:
                EditTextDialog editDialog = new EditTextDialog(this, "我的群昵称",
                        TextUtils.isEmpty(crowd.getMyName()) ? "" : crowd.getMyName(), getResources().getString(R.string.confirm));
                editDialog.setOnConfirmListener(new EditTextDialog.OnConfirmListener() {
                    @Override
                    public void onFinished(String name) {
                        tvCrowdMyNickName.setText(name);
                        crowd.setMyName(name);
                        editCrowd.setMyName(name);
                        presenter.editCrowdMyComment(editCrowd);
                    }
                });
                editDialog.show();
                break;
            case R.id.btn_logout_crowd:
                final CustomDialog customDialog = new CustomDialog(this, getResources().getString(R.string.friend_delete_crowd),
                        "确定退出并删除群聊 \"" + crowd.getName() + "\" ", getResources().getString(R.string.cancel), getResources().getString(R.string.delete));
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
        crowd.setGroupId(crowdId);
        this.editCrowd = crowd;
        this.crowd = crowd;
        tvCrowdMyNickName.setText(crowd.getMyName());
        tvCrowdName.setText(crowd.getName());
        ivCrowdHeader.setImageURI(crowd.getHeadImgUrl());
        tvCrowdIdNo.setText("群号码:" + crowd.getGroupNo() + "");
        tvCrowdIntroduce.setText(crowd.getIntroduction());
        if (crowd.getIsShielding() != 0) {
            toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_on));
            toggleMessageTips.setChecked(true);
        } else {
            toggleMessageTips.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
            toggleMessageTips.setChecked(false);
        }
        if (crowd.getIsTop() != 0) {
            toggleConversationTop.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_on));
            toggleConversationTop.setChecked(true);
        } else {
            toggleConversationTop.setBackgroundDrawable(getResources().getDrawable(R.mipmap.icon_toggle_off));
            toggleConversationTop.setChecked(false);
        }
        if (crowd.isMaster == 1) {
            tvCrowdEdit.setVisibility(View.VISIBLE);
        } else {
            tvCrowdEdit.setVisibility(View.GONE);
        }
        RongIM.getInstance().refreshGroupInfoCache(new Group(crowdId, crowd.getName(), Uri.parse(crowd.getHeadImgUrl())));
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
        presenter.getCrowdDetail();
        presenter.getCrowdNumber();
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
