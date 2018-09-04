package com.leyou.game.widget.fightdialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 狼人杀 确定身份页面
 *
 * @author : rocky
 * @Create Time : 2017/8/1 下午7:02
 * @Modified Time : 2017/8/1 下午7:02
 */
public class WolfKillIdentityDialog extends BaseDialog {
    @BindView(R.id.tv_player_identity)
    TextView tvPlayerIdentity;
    @BindView(R.id.iv_identity)
    ImageView ivIdentity;
    @BindView(R.id.iv_known)
    ImageView ivKnown;

    public WolfKillIdentityDialog(Context context) {
        super(context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_wolf_kill_identity;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @OnClick(R.id.iv_known)
    public void onViewClicked() {
        dismiss();
    }
}
