package com.leyou.game.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/9/1 上午11:56
 * @Modified Time : 2017/9/1 上午11:56
 */
public class CustomDialog extends BaseDialog {

    @BindView(R.id.tv_title_dialog)
    TextView tvTitleDialog;
    @BindView(R.id.tv_des_dialog)
    TextView tvDesDialog;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private String title;
    private String des;
    private String cancel;
    private String confirm;
    private Context context;
    private CustomListener listener;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomDialog(Context context, String title, String des) {
        super(context);
        this.context = context;
        this.title = title;
        this.des = des;
    }

    public CustomDialog(Context context, String title, String des, String cancel, String confirm) {
        super(context);
        this.context = context;
        this.title = title;
        this.des = des;
        this.cancel = cancel;
        this.confirm = confirm;
    }

    public void setCustomListener(CustomListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_custom_dialog;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTitleDialog.setText(title);
        tvDesDialog.setText(des);
        if (!TextUtils.isEmpty(cancel)) {
            btnCancel.setText(cancel);
            btnConfirm.setText(confirm);
        }
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                listener.cancel();
                dismiss();
                break;
            case R.id.btn_confirm:
                listener.confirm();
                dismiss();
                break;
        }
    }

    public interface CustomListener {
        void cancel();

        void confirm();
    }
}
