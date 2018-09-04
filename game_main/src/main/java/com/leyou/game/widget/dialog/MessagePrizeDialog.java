package com.leyou.game.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.mine.AwardDetailActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.AwardInfoBean;
import com.leyou.game.bean.Result;
import com.leyou.game.event.FriendFragmentRefreshEvent;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.util.NumberUtil;
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
 * @Create Time : 2017/8/28 上午11:37
 * @Modified Time : 2017/8/28 上午11:37
 */
public class MessagePrizeDialog extends BaseDialog {
    private Context context;
    private String id;
    private String title;
    @BindView(R.id.tv_message_prize_title)
    TextView tvMessagePrizeTitle;
    @BindView(R.id.tv_message_prize_time)
    TextView tvMessagePrizeTime;
    @BindView(R.id.iv_message_prize_close)
    ImageView ivMessagePrizeClose;
    @BindView(R.id.tv_prize_game_name)
    TextView tvPrizeGameName;
    @BindView(R.id.tv_prize_rank)
    TextView tvPrizeRank;
    @BindView(R.id.tv_prize_name)
    TextView tvPrizeName;
    @BindView(R.id.tv_prize_value)
    TextView tvPrizeValue;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private AwardInfoBean awardInfoBean;

    public MessagePrizeDialog(Context context, String id, String title) {
        super(context);
        this.context = context;
        this.id = id;
        this.title = title;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_message_prize;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        getDetail();
    }

    private void getDetail() {
        HttpUtil.subscribe(HttpUtil.createApi(UserApi.class, Constants.URL).getMessagePrizeDetail(id), new Observer<Result<AwardInfoBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                dismiss();
            }

            @Override
            public void onNext(Result<AwardInfoBean> messageBeanResult) {
                if (messageBeanResult.result == 1) {
                    AwardInfoBean data = messageBeanResult.data;
                    if (null != data) {
                        setAwardInfo(data);
                        EventBus.getDefault().post(new FriendFragmentRefreshEvent(1));
                    } else {
                        dismiss();
                    }
                } else {
                    dismiss();
                }
            }
        });
    }

    private void setAwardInfo(AwardInfoBean awardInfoBean) {
        this.awardInfoBean = awardInfoBean;
        tvMessagePrizeTitle.setText("中奖信息");
        tvMessagePrizeTime.setText(DataUtil.getConvertResult(awardInfoBean.addTime, DataUtil.M_D_HM));
        tvPrizeGameName.setText("游戏名称：" + awardInfoBean.game + "");
        tvPrizeRank.setText("排        名：第" + NumberFormatUtil.formatInteger(awardInfoBean.rank) + "名");
        tvPrizeName.setText("奖品名称：" + awardInfoBean.prizeName + "");
        tvPrizeValue.setText("奖品价值：" + awardInfoBean.price + "元");
        switch (awardInfoBean.status) {
            case AwardInfoBean.STATUS_NULL:
                if (awardInfoBean.type == 1) {
                    btnConfirm.setText("填写收货地址");
                } else if (awardInfoBean.type == 2) {
                    btnConfirm.setText("去充值");
                }
                break;
            case AwardInfoBean.STATUS_WAITING:
            case AwardInfoBean.STATUS_SEND:
                btnConfirm.setText("查看中奖详情");
                break;
        }
    }


    @OnClick({R.id.iv_message_prize_close, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_message_prize_close:
                dismiss();
                break;
            case R.id.btn_confirm:
                Intent intent = new Intent(context, AwardDetailActivity.class);
                intent.putExtra("awardInfo", awardInfoBean);
                intent.putExtra("prizeId", awardInfoBean.id);
                context.startActivity(intent);
                dismiss();
                break;
        }
    }
}
