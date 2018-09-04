package com.leyou.game.activity.wolfkill;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agora.AGEventHandler;
import com.agora.ConstantApp;
import com.agora.EngineConfig;
import com.agora.MyEngineEventHandler;
import com.agora.WorkerThread;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WolfKillRoomInfoBean;
import com.leyou.game.bean.WolfRoleBean;
import com.leyou.game.event.MainTabEvent;
import com.leyou.game.event.WolfKillRoomEvent;
import com.leyou.game.ipresenter.fight.IWolfKillFightActivity;
import com.leyou.game.presenter.game.WolfKillFightActivityPresenter;
import com.leyou.game.util.AgoraInstanceUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.SoftKeyBoardListener;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.WolfKillClockView;
import com.leyou.game.widget.dialog.CustomDialog;
import com.leyou.game.widget.fightdialog.InviteFriendPlayDialog;
import com.leyou.game.widget.fightdialog.SnatchWolfKillIdentityDialog;
import com.leyou.game.widget.fightdialog.WolfKillIdentityDialog;
import com.leyou.game.widget.fightdialog.WolfKillPlayerDetailDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngineForGaming;

/**
 * Description : 对战游戏页面
 *
 * @author : rocky
 * @Create Time : 2017/7/11 下午2:58
 * @Modified By: rocky
 * @Modified Time : 2017/7/11 下午2:58
 */
public class WolfKillFightActivity extends BaseActivity implements
        IWolfKillFightActivity,
        AGEventHandler,
        WolfKillClockView.InternalCountTimeListener {
    private static final String TAG = "WolfKillFightActivity";

    @BindView(R.id.iv_wolf_kill_fight_back)
    ImageView ivWolfKillFightBack;
    @BindView(R.id.tv_wolf_kill_home_id)
    TextView tvWolfKillHomeId;
    @BindView(R.id.tv_wolf_my_identify)
    TextView tvWolfMyIdentify;
    @BindView(R.id.tv_wolf_role_number)
    TextView tvWolfRoleNumber;
    @BindView(R.id.tv_wolf_game_state)
    TextView tvWolfGameState;
    //倒计时时钟
    @BindView(R.id.clockView)
    WolfKillClockView clockView;

    @BindView(R.id.iv_user_head1)
    SimpleDraweeView ivUserHead1;
    @BindView(R.id.iv_user_head1_state)
    SimpleDraweeView ivUserHead1State;
    @BindView(R.id.tv_user_game_state1)
    TextView tvUserGameState1;
    @BindView(R.id.tv_user_name1)
    TextView tvUserName1;
    @BindView(R.id.tv_user_home_owner1)
    TextView tvUserHomeOwner1;
    @BindView(R.id.tv_user_identify1)
    TextView tvUserIdentify1;

    @BindView(R.id.iv_user_head2)
    SimpleDraweeView ivUserHead2;
    @BindView(R.id.iv_user_head2_state)
    SimpleDraweeView ivUserHead2State;
    @BindView(R.id.tv_user_game_state2)
    TextView tvUserGameState2;
    @BindView(R.id.tv_user_name2)
    TextView tvUserName2;
    @BindView(R.id.tv_user_home_owner2)
    TextView tvUserHomeOwner2;
    @BindView(R.id.tv_user_identify2)
    TextView tvUserIdentify2;

    @BindView(R.id.iv_user_head3)
    SimpleDraweeView ivUserHead3;
    @BindView(R.id.iv_user_head3_state)
    SimpleDraweeView ivUserHead3State;
    @BindView(R.id.tv_user_game_state3)
    TextView tvUserGameState3;
    @BindView(R.id.tv_user_name3)
    TextView tvUserName3;
    @BindView(R.id.tv_user_home_owner3)
    TextView tvUserHomeOwner3;
    @BindView(R.id.tv_user_identify3)
    TextView tvUserIdentify3;

    @BindView(R.id.iv_user_head4)
    SimpleDraweeView ivUserHead4;
    @BindView(R.id.iv_user_head4_state)
    SimpleDraweeView ivUserHead4State;
    @BindView(R.id.tv_user_game_state4)
    TextView tvUserGameState4;
    @BindView(R.id.tv_user_name4)
    TextView tvUserName4;
    @BindView(R.id.tv_user_home_owner4)
    TextView tvUserHomeOwner4;
    @BindView(R.id.tv_user_identify4)
    TextView tvUserIdentify4;

    @BindView(R.id.iv_user_head5)
    SimpleDraweeView ivUserHead5;
    @BindView(R.id.iv_user_head5_state)
    SimpleDraweeView ivUserHead5State;
    @BindView(R.id.tv_user_game_state5)
    TextView tvUserGameState5;
    @BindView(R.id.tv_user_name5)
    TextView tvUserName5;
    @BindView(R.id.tv_user_home_owner5)
    TextView tvUserHomeOwner5;
    @BindView(R.id.tv_user_identify5)
    TextView tvUserIdentify5;

    @BindView(R.id.iv_user_head6)
    SimpleDraweeView ivUserHead6;
    @BindView(R.id.iv_user_head6_state)
    SimpleDraweeView ivUserHead6State;
    @BindView(R.id.tv_user_game_state6)
    TextView tvUserGameState6;
    @BindView(R.id.tv_user_name6)
    TextView tvUserName6;
    @BindView(R.id.tv_user_home_owner6)
    TextView tvUserHomeOwner6;
    @BindView(R.id.tv_user_identify6)
    TextView tvUserIdentify6;

    @BindView(R.id.iv_user_head7)
    SimpleDraweeView ivUserHead7;
    @BindView(R.id.iv_user_head7_state)
    SimpleDraweeView ivUserHead7State;
    @BindView(R.id.tv_user_game_state7)
    TextView tvUserGameState7;
    @BindView(R.id.tv_user_name7)
    TextView tvUserName7;
    @BindView(R.id.tv_user_home_owner7)
    TextView tvUserHomeOwner7;
    @BindView(R.id.tv_user_identify7)
    TextView tvUserIdentify7;

    @BindView(R.id.iv_user_head8)
    SimpleDraweeView ivUserHead8;
    @BindView(R.id.iv_user_head8_state)
    SimpleDraweeView ivUserHead8State;
    @BindView(R.id.tv_user_game_state8)
    TextView tvUserGameState8;
    @BindView(R.id.tv_user_name8)
    TextView tvUserName8;
    @BindView(R.id.tv_user_home_owner8)
    TextView tvUserHomeOwner8;
    @BindView(R.id.tv_user_identify8)
    TextView tvUserIdentify8;

    @BindView(R.id.iv_user_head9)
    SimpleDraweeView ivUserHead9;
    @BindView(R.id.iv_user_head9_state)
    SimpleDraweeView ivUserHead9State;
    @BindView(R.id.tv_user_game_state9)
    TextView tvUserGameState9;
    @BindView(R.id.tv_user_name9)
    TextView tvUserName9;
    @BindView(R.id.tv_user_home_owner9)
    TextView tvUserHomeOwner9;
    @BindView(R.id.tv_user_identify9)
    TextView tvUserIdentify9;

    @BindView(R.id.ll_wolf_left)
    LinearLayout llWolfLeft;

    @BindView(R.id.iv_user_head10)
    SimpleDraweeView ivUserHead10;
    @BindView(R.id.iv_user_head10_state)
    SimpleDraweeView ivUserHead10State;
    @BindView(R.id.tv_user_game_state10)
    TextView tvUserGameState10;
    @BindView(R.id.tv_user_name10)
    TextView tvUserName10;
    @BindView(R.id.tv_user_home_owner10)
    TextView tvUserHomeOwner10;
    @BindView(R.id.tv_user_identify10)
    TextView tvUserIdentify10;

    @BindView(R.id.iv_user_head11)
    SimpleDraweeView ivUserHead11;
    @BindView(R.id.iv_user_head11_state)
    SimpleDraweeView ivUserHead11State;
    @BindView(R.id.tv_user_game_state11)
    TextView tvUserGameState11;
    @BindView(R.id.tv_user_name11)
    TextView tvUserName11;
    @BindView(R.id.tv_user_home_owner11)
    TextView tvUserHomeOwner11;
    @BindView(R.id.tv_user_identify11)
    TextView tvUserIdentify11;

    @BindView(R.id.iv_user_head12)
    SimpleDraweeView ivUserHead12;
    @BindView(R.id.iv_user_head12_state)
    SimpleDraweeView ivUserHead12State;
    @BindView(R.id.tv_user_game_state12)
    TextView tvUserGameState12;
    @BindView(R.id.tv_user_name12)
    TextView tvUserName12;
    @BindView(R.id.tv_user_home_owner12)
    TextView tvUserHomeOwner12;
    @BindView(R.id.tv_user_identify12)
    TextView tvUserIdentify12;

    @BindView(R.id.ll_wolf_right)
    LinearLayout llWolfRight;
    @BindView(R.id.iv_wolf_ready_game)
    ImageView ivWolfReadyGame;
    @BindView(R.id.activity_wolf_home_fight)
    LinearLayout activityWolfHomeFight;
    @BindView(R.id.ll_send_message_voice)
    LinearLayout llSendMessageVoice;
    @BindView(R.id.iv_wolf_send_message)
    ImageView ivWolfSendMessage;
    @BindView(R.id.iv_wolf_send_voice)
    ImageView ivWolfSendVoice;
    @BindView(R.id.frame_edit_message)
    LinearLayout llEditMessage;
    @BindView(R.id.et_wolf_message_edit)
    EditText edMessage;
    @BindView(R.id.iv_wolf_message_send)
    ImageView ivMessageSend;
    @BindView(R.id.key_board_view)
    View view;

    private WorkerThread workerThread;
    private EngineConfig engineConfig;
    private MyEngineEventHandler eventHandler;
    private volatile boolean mAudioMuted = false;//允许麦克风
    private volatile int mAudioRouting = -1; // Default
    private boolean isReadyGame = false;
    private WolfKillFightActivityPresenter presenter;
    private boolean isClockFinish = true;// 显示倒计时UI
    private WolfKillRoomInfoBean roomInfoBean = new WolfKillRoomInfoBean();
    private List<WolfRoleBean> gamePlayerList = new ArrayList<>();//当前游戏房间的玩家
    private WolfRoleBean currentPlayer = new WolfRoleBean();//当前玩家

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void roomEvent(WolfKillRoomEvent event) {
        if (event.isRefresh()) {
            presenter.getRoomInfoAndPlayer();
        }
    }

    @Override
    public void initWindows() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_wolf_kill_fight;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        AgoraInstanceUtil.getInstance(this).initWorkerThread();
        workerThread = AgoraInstanceUtil.getInstance(this).getWorkerThread();
        engineConfig = AgoraInstanceUtil.getInstance(this).getWorkerThread().getEngineConfig();
        eventHandler = AgoraInstanceUtil.getInstance(this).getWorkerThread().eventHandler();
        eventHandler.addEventHandler(this);
        engineConfig.mChannel = intent.getLongExtra("roomId", 0) + "";
        workerThread.getRtcEngine().enableAudio();
        ivWolfSendVoice.setSelected(true);
        ivWolfSendMessage.setSelected(true);
        sendVoice();
        openMute();
        ivWolfSendVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtil.d(TAG, "---down---");
                        sendVoice();
                        break;
                    case MotionEvent.ACTION_UP:
                        LogUtil.d(TAG, "---up---");
                        sendVoice();
                        break;
                }
                return false;
            }
        });
        ivWolfSendMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LogUtil.d(TAG, "---down---");
                        break;
                    case MotionEvent.ACTION_UP:
                        LogUtil.d(TAG, "---up---");
                        break;
                }
                return false;
            }
        });
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = height;
                llEditMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                llSendMessageVoice.setVisibility(View.VISIBLE);
                llEditMessage.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = height;
            }
        });
        tvWolfKillHomeId.setText(engineConfig.mChannel + "房间");
        clockView.setCountDownClockListener(this);
    }

    private void sendVoice() {
        RtcEngineForGaming rtcEngine = workerThread.getRtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);
    }

    private void openMute() {
        RtcEngineForGaming rtcEngine = workerThread.getRtcEngine();
        rtcEngine.setEnableSpeakerphone(mAudioRouting != 3);
    }

    @Override
    public void initPresenter() {
        presenter = new WolfKillFightActivityPresenter(this, this, engineConfig.mChannel);
    }

    @OnClick({R.id.iv_wolf_kill_fight_back, R.id.iv_wolf_ready_game, R.id.iv_wolf_send_voice, R.id.iv_wolf_send_message, R.id.iv_wolf_message_send, R.id.iv_user_head1, R.id.iv_user_head1_state, R.id.iv_user_head2, R.id.iv_user_head2_state, R.id.iv_user_head3, R.id.iv_user_head3_state, R.id.iv_user_head7, R.id.iv_user_head7_state, R.id.iv_user_head8, R.id.iv_user_head8_state, R.id.iv_user_head9, R.id.iv_user_head9_state, R.id.iv_user_head4, R.id.iv_user_head4_state, R.id.iv_user_head5, R.id.iv_user_head5_state, R.id.iv_user_head6, R.id.iv_user_head6_state, R.id.iv_user_head10, R.id.iv_user_head10_state, R.id.iv_user_head11, R.id.iv_user_head11_state, R.id.iv_user_head12, R.id.iv_user_head12_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wolf_kill_fight_back:
                final CustomDialog customDialog = new CustomDialog(this, "退出游戏", "要退出游戏吗？", getString(R.string.confirm), getString(R.string.cancel));
                customDialog.setCustomListener(new CustomDialog.CustomListener() {
                    @Override
                    public void cancel() {
                        customDialog.dismiss();
                        presenter.quitRoom(roomInfoBean.roomId);
                    }

                    @Override
                    public void confirm() {
                        customDialog.dismiss();
                    }
                });
                customDialog.show();

                break;
            case R.id.iv_wolf_ready_game:
                isReadyGame = !isReadyGame;
                presenter.prepareGame(isReadyGame);
                break;
            case R.id.iv_wolf_send_message:
                llSendMessageVoice.setVisibility(View.VISIBLE);
                KeyBoardUtils.openKeyboard(edMessage, this);
                break;
            case R.id.iv_wolf_send_voice:
                break;
            case R.id.iv_wolf_message_send:
                llEditMessage.setVisibility(View.GONE);
                KeyBoardUtils.closeKeyboard(edMessage, this);
                break;
            case R.id.iv_user_head1:
            case R.id.iv_user_head1_state:
                showCurrentStatusDialog(0);
                break;
            case R.id.iv_user_head2:
            case R.id.iv_user_head2_state:
                showCurrentStatusDialog(1);
                break;
            case R.id.iv_user_head3:
            case R.id.iv_user_head3_state:
                showCurrentStatusDialog(2);
                break;
            case R.id.iv_user_head4:
            case R.id.iv_user_head4_state:
                showCurrentStatusDialog(3);
                break;
            case R.id.iv_user_head5:
            case R.id.iv_user_head5_state:
                showCurrentStatusDialog(4);
                break;
            case R.id.iv_user_head6:
            case R.id.iv_user_head6_state:
                showCurrentStatusDialog(5);
                break;
            case R.id.iv_user_head7:
            case R.id.iv_user_head7_state:
                showCurrentStatusDialog(6);
                break;
            case R.id.iv_user_head8:
            case R.id.iv_user_head8_state:
                showCurrentStatusDialog(7);
                break;
            case R.id.iv_user_head9:
            case R.id.iv_user_head9_state:
                showCurrentStatusDialog(8);
                break;
            case R.id.iv_user_head10:
            case R.id.iv_user_head10_state:
                showCurrentStatusDialog(9);
                break;
            case R.id.iv_user_head11:
            case R.id.iv_user_head11_state:
                showCurrentStatusDialog(10);
                break;
            case R.id.iv_user_head12:
            case R.id.iv_user_head12_state:
                showCurrentStatusDialog(11);
                break;
        }
    }

    private void showCurrentStatusDialog(int position) {
        if (isClockFinish) {
            if (position < gamePlayerList.size()) {
                //显示玩家信息
                WolfRoleBean wolfRoleBean = gamePlayerList.get(position);
                new WolfKillPlayerDetailDialog(this, wolfRoleBean, currentPlayer.role).show();
            } else {
                //当前头像为空 显示邀请好友
                // TODO: 2017/9/5 后台未实现房间人数
                new InviteFriendPlayDialog(this, roomInfoBean.roomId).show();
            }
//            clockView.setCountDownTimeSecond(25);
//            clockView.start();

            new SnatchWolfKillIdentityDialog(this, roomInfoBean.roomId).show();

//            new WolfKillIdentityDialog(this).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        workerThread.joinChannel(engineConfig.mChannel, UserData.getInstance().getId(), engineConfig.mUid);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.quitRoom(roomInfoBean.roomId);
    }

    private void quitRoom() {
        workerThread.leaveChannel(engineConfig.mChannel);
        eventHandler.removeEventHandler(this);
    }

    @Override
    protected void onDestroy() {
        presenter.quitRoom(roomInfoBean.roomId);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        LogUtil.d(TAG, "---channel:" + channel + "----uid:" + uid + "----elapsed:" + elapsed);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                workerThread.getRtcEngine().muteLocalAudioStream(mAudioMuted);
            }
        });
    }

    @Override
    public void onUserOffline(int uid, int reason) {

        LogUtil.d(TAG, "----uid:" + uid + "----reason:" + reason);
    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED: {
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_QUALITY: {
                peerUid = (Integer) data[0];
                int quality = (int) data[1];
                short delay = (short) data[2];
                short lost = (short) data[3];
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS: {
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                StringBuilder volumeCache = new StringBuilder();
                for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                    peerUid = each.uid;
                    int peerVolume = each.volume;

                    if (peerUid == 0) {
                        continue;
                    }

                    volumeCache.append("volume: ").append(peerUid & 0xFFFFFFFFL).append(" ").append(peerVolume).append("\n");
                }

                if (volumeCache.length() > 0) {
                }
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR: {
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_NETWORK_CONNECTION) {
                }

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED: {
                mAudioRouting = (int) data[0];
                break;
            }
        }
    }

    @Override
    public void clockFinish(boolean isClockFinish) {
        this.isClockFinish = isClockFinish;
    }

    @Override
    public void showWolfKillRoomInfo(WolfKillRoomInfoBean roomInfoBean) {
        if (null != roomInfoBean) {
            this.roomInfoBean = roomInfoBean;
            reSetPlayerInfo();
        } else {

        }
    }

    @Override
    public void prepareGameResult(boolean flag, boolean isReady) {
        if (flag) {
            isReadyGame = isReady;
        } else {
            isReadyGame = !isReady;
        }
        presenter.getRoomInfoAndPlayer();
    }

    @Override
    public void currentQuitRoom(boolean result) {
        if (result) {
            quitRoom();
            finishCurrentActivity();
        } else {
            showMessageToast("退出房间失败");
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

    private void reSetPlayerInfo() {
        List<WolfRoleBean> roomUserInfoList = roomInfoBean.roomUserInfoList;
        this.gamePlayerList = roomUserInfoList;
        initPlayerView();
        if (null != roomUserInfoList && roomUserInfoList.size() > 0) {
            for (int i = 0; i < roomUserInfoList.size(); i++) {
                WolfRoleBean roleBean = roomUserInfoList.get(i);
                if (roleBean.userId.equals(UserData.getInstance().getId())) {
                    this.currentPlayer = roleBean;
                    if (currentPlayer.prepare == 1) {
                        isReadyGame = true;
                        ivWolfReadyGame.setSelected(true);
                    } else {
                        isReadyGame = false;
                        ivWolfReadyGame.setSelected(false);
                    }
                }
                switch (roleBean.order - 1) {
                    case 0:
                        tvUserName1.setText(roleBean.nickname);
                        ivUserHead1.setImageURI(roleBean.rolePicture);
                        ivUserHead1State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner1.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner1.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState1.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState1.setVisibility(View.GONE);
                        }
                        tvUserIdentify1.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify1.setText("民");
                                break;
                            case 1:
                                tvUserIdentify1.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify1.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify1.setText("预");
                                break;
                            case 4:
                                tvUserIdentify1.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify1.setText("守");
                                break;
                        }
                        break;
                    case 1:
                        tvUserName2.setText(roleBean.nickname);
                        ivUserHead2.setImageURI(roleBean.rolePicture);
                        ivUserHead2State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner2.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner2.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState2.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState2.setVisibility(View.GONE);
                        }
                        tvUserIdentify2.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify2.setText("民");
                                break;
                            case 1:
                                tvUserIdentify2.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify2.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify2.setText("预");
                                break;
                            case 4:
                                tvUserIdentify2.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify2.setText("守");
                                break;
                        }
                        break;
                    case 2:
                        tvUserName3.setText(roleBean.nickname);
                        ivUserHead3.setImageURI(roleBean.rolePicture);
                        ivUserHead3State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner3.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner3.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState3.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState3.setVisibility(View.GONE);
                        }
                        tvUserIdentify3.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify3.setText("民");
                                break;
                            case 1:
                                tvUserIdentify3.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify3.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify3.setText("预");
                                break;
                            case 4:
                                tvUserIdentify3.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify3.setText("守");
                                break;
                        }
                        break;
                    case 3:
                        tvUserName4.setText(roleBean.nickname);
                        ivUserHead4.setImageURI(roleBean.rolePicture);
                        ivUserHead4State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner4.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner4.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState4.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState4.setVisibility(View.GONE);
                        }
                        tvUserIdentify4.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify4.setText("民");
                                break;
                            case 1:
                                tvUserIdentify4.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify4.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify4.setText("预");
                                break;
                            case 4:
                                tvUserIdentify4.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify4.setText("守");
                                break;
                        }
                        break;
                    case 4:
                        tvUserName5.setText(roleBean.nickname);
                        ivUserHead5.setImageURI(roleBean.rolePicture);
                        ivUserHead5State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner5.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner5.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState5.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState5.setVisibility(View.GONE);
                        }
                        tvUserIdentify5.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify5.setText("民");
                                break;
                            case 1:
                                tvUserIdentify5.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify5.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify5.setText("预");
                                break;
                            case 4:
                                tvUserIdentify5.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify5.setText("守");
                                break;
                        }
                        break;
                    case 5:
                        tvUserName6.setText(roleBean.nickname);
                        ivUserHead6.setImageURI(roleBean.rolePicture);
                        ivUserHead6State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner6.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner6.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState6.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState6.setVisibility(View.GONE);
                        }
                        tvUserIdentify6.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify6.setText("民");
                                break;
                            case 1:
                                tvUserIdentify6.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify6.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify6.setText("预");
                                break;
                            case 4:
                                tvUserIdentify6.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify6.setText("守");
                                break;
                        }
                        break;
                    case 6:
                        tvUserName7.setText(roleBean.nickname);
                        ivUserHead7.setImageURI(roleBean.rolePicture);
                        ivUserHead7State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner7.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner7.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState7.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState7.setVisibility(View.GONE);
                        }
                        tvUserIdentify7.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify7.setText("民");
                                break;
                            case 1:
                                tvUserIdentify7.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify7.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify7.setText("预");
                                break;
                            case 4:
                                tvUserIdentify7.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify7.setText("守");
                                break;
                        }
                        break;
                    case 7:
                        tvUserName8.setText(roleBean.nickname);
                        ivUserHead8.setImageURI(roleBean.rolePicture);
                        ivUserHead8State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner8.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner8.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState8.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState8.setVisibility(View.GONE);
                        }
                        tvUserIdentify8.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify8.setText("民");
                                break;
                            case 1:
                                tvUserIdentify8.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify8.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify8.setText("预");
                                break;
                            case 4:
                                tvUserIdentify8.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify8.setText("守");
                                break;
                        }
                        break;
                    case 8:
                        tvUserName9.setText(roleBean.nickname);
                        ivUserHead9.setImageURI(roleBean.rolePicture);
                        ivUserHead9State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner9.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner9.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState9.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState9.setVisibility(View.GONE);
                        }
                        tvUserIdentify9.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify9.setText("民");
                                break;
                            case 1:
                                tvUserIdentify9.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify9.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify9.setText("预");
                                break;
                            case 4:
                                tvUserIdentify9.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify9.setText("守");
                                break;
                        }
                        break;
                    case 9:
                        tvUserName10.setText(roleBean.nickname);
                        ivUserHead10.setImageURI(roleBean.rolePicture);
                        ivUserHead10State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner10.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner10.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState10.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState10.setVisibility(View.GONE);
                        }
                        tvUserIdentify10.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify10.setText("民");
                                break;
                            case 1:
                                tvUserIdentify10.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify10.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify10.setText("预");
                                break;
                            case 4:
                                tvUserIdentify10.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify10.setText("守");
                                break;
                        }
                        break;
                    case 10:
                        tvUserName11.setText(roleBean.nickname);
                        ivUserHead11.setImageURI(roleBean.rolePicture);
                        ivUserHead11State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner11.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner11.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState11.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState11.setVisibility(View.GONE);
                        }
                        tvUserIdentify11.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify11.setText("民");
                                break;
                            case 1:
                                tvUserIdentify11.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify11.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify11.setText("预");
                                break;
                            case 4:
                                tvUserIdentify11.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify11.setText("守");
                                break;
                        }
                        break;
                    case 11:
                        tvUserName12.setText(roleBean.nickname);
                        ivUserHead12.setImageURI(roleBean.rolePicture);
                        ivUserHead12State.setImageURI(roleBean.rolePicture);
                        if (roleBean.role == 1) {
                            tvUserHomeOwner12.setVisibility(View.VISIBLE);
                        } else {
                            tvUserHomeOwner12.setVisibility(View.GONE);
                        }
                        if (roleBean.prepare == 1) {
                            tvUserGameState12.setVisibility(View.VISIBLE);
                        } else {
                            tvUserGameState12.setVisibility(View.GONE);
                        }
                        tvUserIdentify12.setVisibility(View.VISIBLE);
                        switch (roleBean.gameRoleMark) {
                            case 0:
                                tvUserIdentify12.setText("民");
                                break;
                            case 1:
                                tvUserIdentify12.setText("狼");
                                break;
                            case 2:
                                tvUserIdentify12.setText("猎");
                                break;
                            case 3:
                                tvUserIdentify12.setText("预");
                                break;
                            case 4:
                                tvUserIdentify12.setText("巫");
                                break;
                            case 5:
                                tvUserIdentify12.setText("守");
                                break;
                        }
                        break;
                }
            }
        }
    }

    private void initPlayerView() {
        tvUserName1.setText("");
        ivUserHead1.setImageURI("");
        ivUserHead1State.setImageURI("");
        tvUserHomeOwner1.setVisibility(View.GONE);
        tvUserGameState1.setVisibility(View.GONE);
        tvUserIdentify1.setVisibility(View.GONE);
        tvUserName2.setText("");
        ivUserHead2.setImageURI("");
        ivUserHead2State.setImageURI("");
        tvUserHomeOwner2.setVisibility(View.GONE);
        tvUserGameState2.setVisibility(View.GONE);
        tvUserIdentify2.setVisibility(View.GONE);
        tvUserName3.setText("");
        ivUserHead3.setImageURI("");
        ivUserHead3State.setImageURI("");
        tvUserHomeOwner3.setVisibility(View.GONE);
        tvUserGameState3.setVisibility(View.GONE);
        tvUserIdentify3.setVisibility(View.GONE);
        tvUserName4.setText("");
        ivUserHead4.setImageURI("");
        ivUserHead4State.setImageURI("");
        tvUserHomeOwner4.setVisibility(View.GONE);
        tvUserGameState4.setVisibility(View.GONE);
        tvUserIdentify4.setVisibility(View.GONE);
        tvUserName5.setText("");
        ivUserHead5.setImageURI("");
        ivUserHead5State.setImageURI("");
        tvUserHomeOwner5.setVisibility(View.GONE);
        tvUserGameState5.setVisibility(View.GONE);
        tvUserIdentify5.setVisibility(View.GONE);
        tvUserName6.setText("");
        ivUserHead6.setImageURI("");
        ivUserHead6State.setImageURI("");
        tvUserHomeOwner6.setVisibility(View.GONE);
        tvUserGameState6.setVisibility(View.GONE);
        tvUserIdentify6.setVisibility(View.GONE);
        tvUserName7.setText("");
        ivUserHead7.setImageURI("");
        ivUserHead7State.setImageURI("");
        tvUserHomeOwner7.setVisibility(View.GONE);
        tvUserGameState7.setVisibility(View.GONE);
        tvUserIdentify7.setVisibility(View.GONE);
        tvUserName8.setText("");
        ivUserHead8.setImageURI("");
        ivUserHead8State.setImageURI("");
        tvUserHomeOwner8.setVisibility(View.GONE);
        tvUserGameState8.setVisibility(View.GONE);
        tvUserIdentify8.setVisibility(View.GONE);
        tvUserName9.setText("");
        ivUserHead9.setImageURI("");
        ivUserHead9State.setImageURI("");
        tvUserHomeOwner9.setVisibility(View.GONE);
        tvUserGameState9.setVisibility(View.GONE);
        tvUserIdentify9.setVisibility(View.GONE);
        tvUserName10.setText("");
        ivUserHead10.setImageURI("");
        ivUserHead10State.setImageURI("");
        tvUserHomeOwner10.setVisibility(View.GONE);
        tvUserGameState10.setVisibility(View.GONE);
        tvUserIdentify10.setVisibility(View.GONE);
        tvUserName11.setText("");
        ivUserHead11.setImageURI("");
        ivUserHead11State.setImageURI("");
        tvUserHomeOwner11.setVisibility(View.GONE);
        tvUserGameState11.setVisibility(View.GONE);
        tvUserIdentify11.setVisibility(View.GONE);
        tvUserName12.setText("");
        ivUserHead12.setImageURI("");
        ivUserHead12State.setImageURI("");
        tvUserHomeOwner12.setVisibility(View.GONE);
        tvUserGameState12.setVisibility(View.GONE);
        tvUserIdentify12.setVisibility(View.GONE);
    }
}