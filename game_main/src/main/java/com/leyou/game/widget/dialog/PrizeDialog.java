package com.leyou.game.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.activity.mine.AwardDetailActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.PushBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/6/23 下午3:28
 * @Modified Time : 2017/6/23 下午3:28
 */
public class PrizeDialog extends BaseDialog {

    private String prizeId;
    private PushBean pushBean;
    private Context context;
    @BindView(R.id.iv_prize)
    ImageView ivPrize;

    public PrizeDialog(Context context, PushBean pushBean, String prizeId) {
        super(context);
        this.context = context;
        this.pushBean = pushBean;
        this.prizeId = prizeId;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_prize_yes;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_prize)
    public void onViewClicked() {
        Intent intent = new Intent(context, AwardDetailActivity.class);
        intent.putExtra("prizeId", prizeId);
        context.startActivity(intent);
        dismiss();
    }
}
