package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午4:25
 * @Modified Time : 2017/5/10 下午4:25
 */
public class SignSuccessDialog extends BaseDialog {
    private Context context;
    private String coinNumber;
    @BindView(R.id.iv_box_change)
    ImageView ivBoxChange;
    @BindView(R.id.iv_sign_diamond)
    ImageView ivDiamond;
    @BindView(R.id.tv_diamond_number)
    TextView tvDiamondNumber;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    public SignSuccessDialog(Context context, String coinNumber) {
        super(context);
        this.context = context;
        this.coinNumber = coinNumber;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_sign_success;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        LogUtil.d("sign", "---signDiamond:" + coinNumber);
        double coin = Double.valueOf(coinNumber);
        int coin1 = (int) coin;
        tvDiamondNumber.setText("+" + String.valueOf(coin1) + "");
    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
