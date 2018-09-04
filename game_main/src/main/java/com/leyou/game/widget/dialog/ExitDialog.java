package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 退出登陆弹窗提示
 *
 * @author : rocky
 * @Create Time : 2017/4/27 上午11:47
 * @Modified Time : 2017/4/27 上午11:47
 */
public class ExitDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.btn_exit_cancel)
    Button btnExitCancel;
    @BindView(R.id.btn_exit_confirm)
    Button btnExitConfirm;

    public ExitDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_exit;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_exit_cancel, R.id.btn_exit_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_exit_confirm:
                dismiss();
                break;
            case R.id.btn_exit_cancel:
                boolean exit = UserData.getInstance().clearUserInfo();
                if (exit) {
                    ToastUtils.showToastShort("退出登陆");
                    EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH,1));
                } else {
                    ToastUtils.showToastShort("退出登陆失败");
                }
                dismiss();
                break;
        }
    }
}
