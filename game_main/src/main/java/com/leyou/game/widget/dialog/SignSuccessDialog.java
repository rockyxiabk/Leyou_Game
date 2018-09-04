package com.leyou.game.widget.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午4:25
 * @Modified Time : 2017/5/10 下午4:25
 */
public class SignSuccessDialog extends BaseDialog {
    private Context context;
    private int coinNumber;
    @BindView(R.id.iv_box_change)
    ImageView ivBoxChange;
    @BindView(R.id.iv_sign_diamond)
    ImageView ivDiamond;
    @BindView(R.id.tv_diamond_number)
    TextView tvDiamondNumber;
    @BindView(R.id.ll_number)
    LinearLayout llNumber;
    private Handler handler = new Handler();

    public SignSuccessDialog(Context context, int coinNumber) {
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
        tvDiamondNumber.setText("+" + coinNumber);
        ivDiamond.setVisibility(View.GONE);
        tvDiamondNumber.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeInUp).duration(1000).playOn(ivBoxChange);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeImage();
            }
        }, 1000);
    }

    private void changeImage() {
        ivDiamond.setVisibility(View.VISIBLE);
        tvDiamondNumber.setVisibility(View.VISIBLE);
        ivBoxChange.setImageResource(R.mipmap.icon_sign_box_on);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 2000);
    }
}
