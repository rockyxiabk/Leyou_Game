package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 激活宝库页面（新用户）
 *
 * @author : rocky
 * @Create Time : 2017/5/15 下午7:21
 * @Modified Time : 2017/5/15 下午7:21
 */
public class ActivateTreasureDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.btn_activate_confirm)
    Button btnActivateConfirm;

    public ActivateTreasureDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_activate_treasure;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_activate_confirm)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_activate_confirm:
                SPUtil.setInt(context, SPUtil.TREASURE, "activateTimes", 2);
                dismiss();
                break;
        }
    }
}
