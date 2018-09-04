package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 矿工刷新弹窗
 *
 * @author : rocky
 * @Create Time : 2017/5/3 上午10:15
 * @Modified Time : 2017/5/3 上午10:15
 */
public class RefreshWorkerDialog extends BaseDialog {
    @BindView(R.id.btn_worker_cancel)
    Button btnWorkerCancel;
    @BindView(R.id.btn_worker_confirm)
    Button btnWorkerConfirm;
    private RefreshWorkerListener listener;

    public RefreshWorkerDialog(Context context) {
        super(context);
    }

    public void setRefreshWorkerListener(RefreshWorkerListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_refresh_worker;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_worker_cancel, R.id.btn_worker_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_worker_cancel:
                dismiss();
                break;
            case R.id.btn_worker_confirm:
                dismiss();
                listener.confirm();
                break;
        }
    }

    public interface RefreshWorkerListener {
        void confirm();
    }
}
