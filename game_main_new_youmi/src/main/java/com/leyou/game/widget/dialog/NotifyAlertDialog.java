package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.activity.MainActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.PushBean;
import com.leyou.game.util.NotificationsUtil;
import com.leyou.game.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description :  提示打开系统通知弹窗提示
 *
 * @author : rocky
 * @Create Time : 2017/4/27 上午11:47
 * @Modified Time : 2017/4/27 上午11:47
 */
public class NotifyAlertDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_go_setting)
    Button btnGoSetting;

    public NotifyAlertDialog(Context context) {
        super(context);
        this.context = context;
        setCancelable(false);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_notify_alert;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        SPUtil.setLong(context, SPUtil.SETTING, "notify_time", System.currentTimeMillis());
    }

    @OnClick({R.id.btn_cancel, R.id.btn_go_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_go_setting:
                NotificationsUtil.requestNotificationPermission(context);
                dismiss();
                break;
        }
    }
}
