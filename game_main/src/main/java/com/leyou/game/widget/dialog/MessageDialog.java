package com.leyou.game.widget.dialog;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.MessageBean;
import com.leyou.game.bean.Result;
import com.leyou.game.event.FriendFragmentRefreshEvent;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.api.UserApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/7/5 下午5:38
 * @Modified Time : 2017/7/5 下午5:38
 */
public class MessageDialog extends BaseDialog {

    private final Context context;
    private final String id;
    private final String title;
    private final int infoType;
    @BindView(R.id.tv_text_explain_title)
    TextView tvTextExplainTitle;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;
    @BindView(R.id.iv_game_explain_close)
    ImageView ivGameExplainClose;
    @BindView(R.id.tv_text_explain_content)
    TextView tvTextExplainContent;
    private MessageBean messageBean;

    public MessageDialog(Context context, String id, int infoType, String title) {
        super(context);
        this.context = context;
        this.id = id;
        this.infoType = infoType;
        this.title = title;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_message_content;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTextExplainTitle.setText(title);
        getDetail();
    }

    private void getDetail() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getMessageDetail(id, infoType), new Observer<Result<MessageBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dismiss();
            }

            @Override
            public void onNext(Result<MessageBean> messageBeanResult) {
                if (messageBeanResult.result == 1) {
                    messageBean = messageBeanResult.data;
                    tvTextExplainContent.setText(messageBean.content);
                    tvMessageTime.setText(DataUtil.getConvertResult(messageBean.time,DataUtil.M_D_HM));
                    EventBus.getDefault().post(new FriendFragmentRefreshEvent(1));
                } else {
                    dismiss();
                }
            }
        });
    }

    @OnClick(R.id.iv_game_explain_close)
    public void onViewClicked() {
        dismiss();
    }
}
