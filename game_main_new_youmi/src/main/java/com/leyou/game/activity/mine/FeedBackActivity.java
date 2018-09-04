package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.Result;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.SystemApi;
import com.leyou.game.widget.dialog.mine.FeedBackCommitDialag;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Description : 意见反馈
 *
 * @author : rocky
 * @Create Time : 2017/11/1 下午8:38
 * @Modified By: rocky
 * @Modified Time : 2017/11/1 下午8:38
 */
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_feedback_title)
    EditText etFeedbackTitle;
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent;
    @BindView(R.id.et_feedback_contact_type)
    EditText etFeedbackContactType;
    @BindView(R.id.btn_feedback_commit)
    Button btnFeedbackCommit;
    private String title;
    private String content;
    private String contactType;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
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

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.iv_close, R.id.btn_feedback_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                KeyBoardUtils.closeKeyboard(etFeedbackContactType, this);
                finishCurrentActivity();
                break;
            case R.id.btn_feedback_commit:
                commit(title, content, contactType);
                KeyBoardUtils.closeKeyboard(etFeedbackContactType, this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    public void commit(String title, String content, String contact) {
        HttpUtil.createApi(SystemApi.class, Constants.URL).feedback(title, content, contact).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    int result = response.body().result;
                    if (0 != result) {
                        ToastUtils.showToastShort("提交成功");
                        showSuccessView();
                    } else {
                        ToastUtils.showToastShort("提交失败,请重新提交反馈信息！");
                    }
                } else {
                    ToastUtils.showToastShort("提交失败,请重新提交反馈信息！");
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                ToastUtils.showToastShort("提交失败,请重新提交反馈信息！");
            }
        });
    }

    private void showSuccessView() {
        new FeedBackCommitDialag(this).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishCurrentActivity();
            }
        }, 3000);
    }
}
