package com.leyou.game.widget.dialog.treasury;

import android.content.Context;
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
 * @Create Time : 2017/7/6 下午6:11
 * @Modified Time : 2017/7/6 下午6:11
 */
public class TextViewDialog extends BaseDialog {
    private final String content;
    private final String title;
    private final Context context;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private MyOnClickListener listener;

    public TextViewDialog(Context context, String title, String content) {
        super(context);
        this.context = context;
        this.content = content;
        this.title = title;
    }

    public void setMyOnClickListener(MyOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_text_view;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTitle.setText(title);
        tvDes.setText(content);

    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                listener.cancel();
                break;
            case R.id.btn_confirm:
                listener.confirm();
                break;
        }
        dismiss();
    }

    public interface MyOnClickListener {
        void confirm();

        void cancel();
    }
}
