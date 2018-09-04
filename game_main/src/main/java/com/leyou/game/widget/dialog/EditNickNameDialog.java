package com.leyou.game.widget.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 修改备注 和 设置群名称
 *
 * @author : rocky
 * @Create Time : 2017/7/25 下午8:06
 * @Modified Time : 2017/7/25 下午8:06
 */
public class EditNickNameDialog extends BaseDialog {

    private final Context context;
    private final String title;
    private final int type;//1修改备注 2设置群名称 3修改我在本群的昵称
    private String nickName;
    @BindView(R.id.et_modify_name)
    EditText etModifyName;
    @BindView(R.id.tv_edit_nick_name)
    TextView tvEditNickName;
    private setNickNameListener listener;

    public EditNickNameDialog(Context context, String title, String nickName, int type) {
        super(context);
        this.context = context;
        this.title = title;
        this.nickName = nickName;
        this.type = type;
    }

    public void setOnSetNickNameListener(setNickNameListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_friend_edit_nick_name;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvEditNickName.setText(title);
        etModifyName.setText(nickName);
        etModifyName.setSelection(nickName.length());
        etModifyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentName = s.toString();
                nickName = currentName;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        KeyBoardUtils.openKeyboard(etModifyName, context);
    }

    @OnClick(R.id.btn_modify_name_confirm)
    public void onViewClicked() {
        listener.setNickName(nickName);
        KeyBoardUtils.closeKeyboard(etModifyName, context);
        dismiss();
    }

    public interface setNickNameListener {
        void setNickName(String name);
    }
}
