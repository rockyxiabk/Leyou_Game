package com.leyou.game.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.event.MainTabEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 没有网络环境或者网络慢
 *
 * @author : rocky
 * @Create Time : 2017/7/21 下午4:32
 * @Modified Time : 2017/7/21 下午4:32
 */
public class NoNetWorkDialog extends BaseDialog {
    private final Context context;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    public NoNetWorkDialog(Context context, boolean b) {
        super(context);
        this.context = context;
        setCancelable(b);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_no_net_work;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new MainTabEvent(2));
        dismiss();
    }

    @OnClick({R.id.btn_confirm, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                dismiss();
                break;
            case R.id.btn_cancel:
                EventBus.getDefault().post(new MainTabEvent(2));
                dismiss();
                break;
        }
    }
}
