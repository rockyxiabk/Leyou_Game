package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/7/3 上午9:58
 * @Modified Time : 2017/7/3 上午9:58
 */
public class WorkerModifyDialog extends BaseDialog {

    private final Context context;
    private final String string;
    private final String des;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private WorkerListener listener;

    public void setWorkerListener(WorkerListener listener) {
        this.listener = listener;
    }

    public WorkerModifyDialog(Context context, String string, String des) {
        super(context);
        this.context = context;
        this.string = string;
        this.des = des;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_worker;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTitle.setText(string);
        tvDes.setText(des);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                listener.confirm();
                dismiss();
                break;
        }
    }


    public interface WorkerListener {
        void confirm();
    }
}
