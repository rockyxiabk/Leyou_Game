package com.leyou.game.widget.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.ipresenter.mine.IFeedBackDialog;
import com.leyou.game.presenter.mine.FeedBackDialogPresenter;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 意见反馈弹窗
 *
 * @author : rocky
 * @Create Time : 2017/4/24 下午3:27
 * @Modified Time : 2017/4/24 下午3:27
 */
public class FeedBackDialog extends BaseDialog implements IFeedBackDialog {
    private Context context;
    @BindView(R.id.iv_feedback_close)
    ImageView ivFeedbackClose;
    @BindView(R.id.et_feedback_title)
    EditText etFeedbackTitle;
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent;
    @BindView(R.id.et_feedback_contact_type)
    EditText etFeedbackContactType;
    @BindView(R.id.btn_feedback_commit)
    Button btnFeedbackCommit;
    private FeedBackDialogPresenter presenter;
    private String title;
    private String content;
    private String contactType;

    public FeedBackDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_feed_back;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        presenter = new FeedBackDialogPresenter(context, this);
        etFeedbackTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etFeedbackContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etFeedbackContactType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactType = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_feedback_close, R.id.btn_feedback_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_feedback_close:
                KeyBoardUtils.closeKeyboard(etFeedbackContactType, context);
                dismiss();
                break;
            case R.id.btn_feedback_commit:
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(contactType) && !TextUtils.isEmpty(content)){
                    presenter.commit(title, content, contactType);
                }else {
                    showError("请确认反馈信息是否完整！");
                }
                break;
        }
    }

    @Override
    public void showSuccess(String info) {
        KeyBoardUtils.closeKeyboard(etFeedbackContactType, context);
        dismiss();
        new FeedBackCommitDialag(context).show();
    }

    @Override
    public void showError(String error) {
        ToastUtils.showToastShort(error);
    }
}
