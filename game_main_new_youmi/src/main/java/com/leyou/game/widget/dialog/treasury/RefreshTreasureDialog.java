package com.leyou.game.widget.dialog.treasury;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.treasury.TreasureTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 宝窟刷新弹窗
 *
 * @author : rocky
 * @Create Time : 2017/5/3 上午10:00
 * @Modified Time : 2017/5/3 上午10:00
 */
public class RefreshTreasureDialog extends BaseDialog {

    @BindView(R.id.tv_treasure_cancel)
    TreasureTextView btnTreasureCancel;
    @BindView(R.id.tv_treasure_confirm)
    TreasureTextView btnTreasureConfirm;
    private RefreshTreasureListener listener;

    public RefreshTreasureDialog(Context context) {
        super(context);
    }

    public void setRefreshTreasureListener(RefreshTreasureListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_refresh_treasure;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tv_treasure_cancel, R.id.tv_treasure_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_treasure_cancel:
                dismiss();
                break;
            case R.id.tv_treasure_confirm:
                dismiss();
                listener.confirm();
                break;
        }
    }

    public interface RefreshTreasureListener {
        void confirm();
    }
}
