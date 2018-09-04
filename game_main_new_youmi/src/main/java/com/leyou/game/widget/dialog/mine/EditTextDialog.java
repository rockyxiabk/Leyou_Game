package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog.mine
 *
 * @author : rocky
 * @Create Time : 2017/10/24 下午7:33
 * @Modified Time : 2017/10/24 下午7:33
 */
public class EditTextDialog extends BaseDialog {
    private final String title;
    private final String text;
    private final String action;
    private final Context context;
    @BindView(R.id.tv_item_title)
    TextView tvItemTitle;
    @BindView(R.id.ed_text)
    EditText edText;
    @BindView(R.id.ll_item_edit)
    LinearLayout llItemEdit;
    @BindView(R.id.tv_item_done)
    TextView tvItemDone;
    @BindView(R.id.ll_item_done)
    LinearLayout llItemDone;
    private OnConfirmListener listener;
    private String content;

    public EditTextDialog(Context context, String title, String text, String action) {
        super(context);
        this.context = context;
        this.title = title;
        this.text = text;
        this.action = action;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    public  void setOnConfirmListener(OnConfirmListener listener){
        this.listener = listener;
    }
    @Override
    public int getLayout() {
        return R.layout.layout_dialog_modify_edit_item;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvItemTitle.setText(title);
        tvItemDone.setText(action);
        content=text;
        edText.setText(text);
        edText.setSelection(text.length());
        edText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = edText.getText().toString();
            }
        });
        KeyBoardUtils.openKeyboard(edText,context);
    }

    @OnClick({R.id.ed_text, R.id.ll_item_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ed_text:
                break;
            case R.id.ll_item_done:
                listener.onFinished(content);
                KeyBoardUtils.closeKeyboard(edText,context);
                dismiss();
                break;
        }
    }
    public interface OnConfirmListener {
        void onFinished(String content);
    }
}
