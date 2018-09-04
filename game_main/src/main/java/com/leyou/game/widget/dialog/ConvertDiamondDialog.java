package com.leyou.game.widget.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/6/19 下午3:53
 * @Modified Time : 2017/6/19 下午3:53
 */
public class ConvertDiamondDialog extends BaseDialog {

    @BindView(R.id.et_chip_number)
    EditText etChipNumber;
    private Context context;
    private String title;
    private int chipsNumber;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_convert_max)
    TextView tvConvertMax;
    private WorkerListener listener;
    private int number;

    public void setWorkerListener(WorkerListener listener) {
        this.listener = listener;
    }

    public ConvertDiamondDialog(Context context, String title, int chipsNumber) {
        super(context);
        this.context = context;
        this.title = title;
        this.chipsNumber = chipsNumber;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_convert_diamond;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTitle.setText(title);
        tvConvertMax.setText("当碎钻兑换比例为1：10000, 最多可兑换" + chipsNumber / 10000 + "个钻石");

        etChipNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentName = s.toString();
                if (!TextUtils.isEmpty(currentName)) {
                    number = Integer.valueOf(currentName);
                } else {
                    number = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        KeyBoardUtils.openKeyboard(etChipNumber, context);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                KeyBoardUtils.closeKeyboard(etChipNumber, context);
                dismiss();
                break;
            case R.id.btn_confirm:
                commit();
                break;
        }
    }

    private void commit() {
        if (0 < number && number <= chipsNumber / 10000) {
            KeyBoardUtils.closeKeyboard(etChipNumber, context);
            listener.confirm(number * 10000);
            dismiss();
        } else {
            ToastUtils.showToastShort("请输入正确的钻石数量");
        }
    }

    public interface WorkerListener {
        void confirm(int number);
    }
}
