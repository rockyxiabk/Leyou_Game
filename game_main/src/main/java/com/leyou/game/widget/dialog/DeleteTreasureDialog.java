package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.widget.treasury.TreasureTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 废除自己的宝窟
 *
 * @author : rocky
 * @Create Time : 2017/6/12 下午5:17
 * @Modified Time : 2017/6/12 下午5:17
 */
public class DeleteTreasureDialog extends BaseDialog {

    private Context context;
    @BindView(R.id.tv_treasure_cancel)
    TreasureTextView btnTreasureCancel;
    @BindView(R.id.tv_treasure_confirm)
    TreasureTextView btnTreasureConfirm;
    private DeleteTreasureListener listener;

    public void setDeleteTreasureListener(DeleteTreasureListener listener) {
        this.listener = listener;
    }

    public DeleteTreasureDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_delete_treasure;
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
                listener.confirm();
                dismiss();
                break;
        }
    }

    public interface DeleteTreasureListener {
        void confirm();
    }
}
