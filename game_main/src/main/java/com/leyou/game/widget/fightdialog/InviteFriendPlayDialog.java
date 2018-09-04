package com.leyou.game.widget.fightdialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.wolfkill.InviteFriendAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.ContactBean;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.fight.IInviteFriendDialog;
import com.leyou.game.util.DBUtil;
import com.leyou.game.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 邀请好友 进入游戏房间(查询本地数据库好友)
 *
 * @author : rocky
 * @Create Time : 2017/7/26 下午4:48
 * @Modified Time : 2017/7/26 下午4:48
 */
public class InviteFriendPlayDialog extends BaseDialog implements IInviteFriendDialog, InviteFriendAdapter.ClickResult {

    private long roomId;
    @BindView(R.id.iv_load_error)
    ImageView ivLoadError;
    @BindView(R.id.re_error_try)
    RelativeLayout reErrorTry;
    @BindView(R.id.iv_loading_progress)
    ImageView ivLoadingProgress;
    @BindView(R.id.re_loading)
    RelativeLayout reLoading;
    private Context context;
    @BindView(R.id.iv_game_rank_close)
    ImageView ivGameRankClose;
    @BindView(R.id.recycler_game_rank)
    RecyclerView recyclerGameRank;
    private List<Friend> list = new ArrayList<>();
    private InviteFriendAdapter adapter;
    private Animation loadAnimation;

    public InviteFriendPlayDialog(Context context, long roomId) {
        super(context);
        this.context = context;
        this.roomId = roomId;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_wolf_kill_invite_friend_play_game;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        loadAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim);

        adapter = new InviteFriendAdapter(context, list, roomId);
        adapter.setClickResult(this);
        recyclerGameRank.setLayoutManager(new LinearLayoutManager(context));
        recyclerGameRank.setAdapter(adapter);

        showData(DBUtil.getInstance(context).queryMyFriendList());
    }

    @Override
    public void showLoadingView() {
        ivLoadingProgress.startAnimation(loadAnimation);
        reLoading.setVisibility(View.VISIBLE);
        recyclerGameRank.setVisibility(View.GONE);
        reErrorTry.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView() {
        reErrorTry.setVisibility(View.VISIBLE);
        recyclerGameRank.setVisibility(View.GONE);
        reLoading.setVisibility(View.GONE);
    }

    @Override
    public void showListView() {
        recyclerGameRank.setVisibility(View.VISIBLE);
        reErrorTry.setVisibility(View.GONE);
        reLoading.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<Friend> list) {
        if (recyclerGameRank.getVisibility() != View.VISIBLE) {
            showListView();
        }
        adapter.loadMoreData(list);
    }

    @Override
    public void showError(String error) {
        ToastUtils.showToastShort(error);
    }

    @OnClick({R.id.iv_game_rank_close, R.id.iv_load_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_game_rank_close:
                dismiss();
                break;
            case R.id.iv_load_error:
                showLoadingView();
                break;
        }
    }

    @Override
    public void inviteResult(boolean result) {
        if (result) {
            dismiss();
        }
    }
}
