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
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/9/9 上午11:32
 * @Modified Time : 2017/9/9 上午11:32
 */
public class DeleteTreasureFragmentDialog extends BaseDialog {

    private Context context;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private DeleteTreasureFragmentListener listener;

    public void setDeleteTreasureFragmentListener(DeleteTreasureFragmentListener listener) {
        this.listener = listener;
    }

    public DeleteTreasureFragmentDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_delete_treasure_fragment;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                listener.confirm();
                dismiss();
                break;
        }
    }

    public interface DeleteTreasureFragmentListener {
        void confirm();
    }
}
