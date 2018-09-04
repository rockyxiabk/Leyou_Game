package com.leyou.game.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.event.MainTabEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 提示用户登陆
 *
 * @author : rocky
 * @Create Time : 2017/7/21 下午4:32
 * @Modified Time : 2017/7/21 下午4:32
 */
public class LogInDialog extends BaseDialog {
    private final Context context;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    public LogInDialog(Context context, boolean b) {
        super(context);
        this.context = context;
        setCancelable(b);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_log_in;
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

    @OnClick({ R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                EventBus.getDefault().post(new MainTabEvent(2));
                context.startActivity(new Intent(context, RegisterActivity.class));
                dismiss();
                break;
        }
    }
}
