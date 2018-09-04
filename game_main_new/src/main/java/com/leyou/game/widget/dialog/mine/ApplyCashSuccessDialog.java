package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.widget.dialog.game.GameManagerDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog.mine
 *
 * @author : rocky
 * @Create Time : 2017/12/4 下午5:11
 * @Modified Time : 2017/12/4 下午5:11
 */
public class ApplyCashSuccessDialog extends BaseDialog {
    @BindView(R.id.iv_close)
    ImageView ivClose;
    private OnViewClickListener listener;

    public ApplyCashSuccessDialog(Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setOnClickListener(OnViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_with_cash_share_success;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        listener.exit();
        dismiss();
    }

    public interface OnViewClickListener {

        void exit();
    }
}
