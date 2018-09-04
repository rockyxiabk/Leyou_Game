package com.leyou.game.widget.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 编辑昵称弹窗
 *
 * @author : rocky
 * @Create Time : 2017/4/27 下午1:49
 * @Modified Time : 2017/4/27 下午1:49
 */
public class NickNameDialog extends BaseDialog {

    @BindView(R.id.et_modify_name)
    EditText etModifyName;
    @BindView(R.id.btn_modify_name_confirm)
    Button btnModifyNameConfirm;
    private Context context;
    private String name;
    private setNickNameListener listener;

    public NickNameDialog(Context context, String name) {
        super(context);
        this.context = context;
        this.name = name;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_modify_name;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        etModifyName.setText(name);
        etModifyName.setSelection(name.length());
        etModifyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentName = s.toString();
                name = currentName;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        KeyBoardUtils.openKeyboard(etModifyName, context);
    }

    @OnClick(R.id.btn_modify_name_confirm)
    public void onViewClicked() {
        listener.setNickName(name);
        KeyBoardUtils.closeKeyboard(etModifyName, context);
        dismiss();
    }

    public void setOnSetNickNameListener(setNickNameListener listener) {
        this.listener = listener;
    }

    public interface setNickNameListener {
        void setNickName(String name);
    }
}
