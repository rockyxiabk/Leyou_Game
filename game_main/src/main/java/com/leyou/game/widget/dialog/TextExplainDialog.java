package com.leyou.game.widget.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 宝窟页面-玩法介绍  宝窟页面-合成页面-合成说明  宝窟页面-解雇页面-解雇说明  宝窟页面-升级页面-升级说明
 * 赢大奖页面-游戏说明-奖励说明
 * 我的页面-签到说明
 *
 * @author : rocky
 * @Create Time : 2017/4/25 下午3:23
 * @Modified Time : 2017/4/25 下午3:23
 */
public class TextExplainDialog extends BaseDialog {

    private final String content;
    private final String title;
    @BindView(R.id.iv_game_explain_close)
    ImageView ivGameExplainClose;
    @BindView(R.id.tv_text_explain_title)
    TextView tvTextExplainTitle;
    @BindView(R.id.tv_text_explain_content)
    TextView tvTextExplainContent;

    public TextExplainDialog(Context context, String title, String content) {
        super(context);
        this.title = title;
        this.content = content;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_text_explain;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTextExplainContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvTextExplainTitle.setText(title);
        tvTextExplainContent.setText(content);
    }

    @OnClick(R.id.iv_game_explain_close)
    public void onViewClicked() {
        dismiss();
    }
}
