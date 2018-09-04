package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/5/10 下午7:32
 * @Modified Time : 2017/5/10 下午7:32
 */
public class LoadingProgressDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.iv_loading_progress)
    ImageView ivLoadingProgress;
    @BindView(R.id.tv_loading_des)
    TextView tvLoadingDes;
    private Animation animation;
    private double exitTime;

    public LoadingProgressDialog(Context context, boolean canBack) {
        super(context);
        this.context = context;
        setCancelable(canBack);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_loading_progress;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim);
        ivLoadingProgress.startAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
        } else {
            dismiss();
        }
    }

    public void setLoadingText(String des) {
        tvLoadingDes.setText(des);
    }
}
