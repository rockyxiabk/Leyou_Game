package com.leyou.game.widget.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.event.FriendFragmentRefreshEvent;
import com.leyou.game.util.DataUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/7/5 下午5:38
 * @Modified Time : 2017/7/5 下午5:38
 */
public class MessageDialog extends BaseDialog {

    private final Context context;
    private final int id;
    private final String title;
    private final String content;
    private final long createTime;
    @BindView(R.id.tv_text_explain_title)
    TextView tvTextExplainTitle;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;
    @BindView(R.id.iv_game_explain_close)
    ImageView ivGameExplainClose;
    @BindView(R.id.tv_text_explain_content)
    TextView tvTextExplainContent;

    public MessageDialog(Context context, int id, String title, String content, long createTime) {
        super(context);
        this.context = context;
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_message_content;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTextExplainTitle.setText(title);
        tvTextExplainContent.setText(content);
        tvMessageTime.setText(DataUtil.getConvertResult(createTime, DataUtil.Y_M_D_HM));
        EventBus.getDefault().post(new FriendFragmentRefreshEvent(1));
    }

    @OnClick(R.id.iv_game_explain_close)
    public void onViewClicked() {
        dismiss();
    }
}
