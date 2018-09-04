package com.leyou.game.widget.fightdialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WolfKillRoomInfoBean;
import com.leyou.game.bean.WolfRoleBean;
import com.leyou.game.dao.Friend;
import com.leyou.game.event.WolfKillRoomEvent;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.FriendApi;
import com.leyou.game.util.api.WolfKillApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 查看游戏玩家的资料 添加好友 房主踢出玩家
 *
 * @author : rocky
 * @Create Time : 2017/7/26 下午4:10
 * @Modified Time : 2017/7/26 下午4:10
 */
public class WolfKillPlayerDetailDialog extends BaseDialog {
    private int role;
    private Context context;
    private WolfRoleBean wolfRoleBean;
    @BindView(R.id.iv_user_head)
    SimpleDraweeView ivUserHead;
    @BindView(R.id.tv_report)
    TextView tvReport;
    @BindView(R.id.tv_player_name)
    TextView tvPlayerName;
    @BindView(R.id.tv_player_game_count)
    TextView tvPlayerGameCount;
    @BindView(R.id.tv_player_win_rate)
    TextView tvPlayerWinRate;
    @BindView(R.id.iv_kick_room)
    ImageView ivKickRoom;
    @BindView(R.id.iv_add_friend)
    ImageView ivAddFriend;

    public WolfKillPlayerDetailDialog(Context context, WolfRoleBean wolfRoleBean, int role) {
        super(context);
        this.context = context;
        this.wolfRoleBean = wolfRoleBean;
        this.role = role;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_wolf_kill_player_detail;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        ivUserHead.setImageURI(wolfRoleBean.rolePicture);
        tvPlayerName.setText(wolfRoleBean.nickname);
        tvPlayerGameCount.setText("" + wolfRoleBean.gameCount + "");
        tvPlayerWinRate.setText("" + wolfRoleBean.winRate + "");
        if (role == 1) {
            ivKickRoom.setVisibility(View.VISIBLE);
        } else {
            ivKickRoom.setVisibility(View.GONE);
        }
        Friend friend = DBUtil.getInstance(context).queryFriendByUserId(wolfRoleBean.userId);
        if (null != friend) {
            ivAddFriend.setVisibility(View.GONE);
        } else {
            ivAddFriend.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_report, R.id.iv_kick_room, R.id.iv_add_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_report:
                report(wolfRoleBean.userId);
                break;
            case R.id.iv_kick_room:
                kickRoom();
                break;
            case R.id.iv_add_friend:
                addFriend(wolfRoleBean.userId);
                break;
        }
    }

    private void kickRoom() {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).kickRoom(wolfRoleBean.userId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("踢出玩家失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    ToastUtils.showToastShort("踢出玩家成功");
                    EventBus.getDefault().post(new WolfKillRoomEvent(true));
                    dismiss();
                } else {
                    ToastUtils.showToastShort("踢出玩家失败");
                }
            }
        });
    }

    public void addFriend(String userId) {
        HttpUtil.subscribe(HttpUtil.createApi(FriendApi.class, Constants.URL).addFriendByUserId(userId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("好友添加失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    ToastUtils.showToastShort("申请好友成功");
                    dismiss();
                } else {
                    ToastUtils.showToastShort("好友添加失败");
                }
            }
        });
    }

    public void report(String userId) {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).report(userId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dismiss();
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    ToastUtils.showToastShort("举报成功");
                } else {
                }
                dismiss();
            }
        });
    }
}
