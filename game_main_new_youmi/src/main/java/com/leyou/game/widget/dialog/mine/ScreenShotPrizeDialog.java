package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.game.GameWinPriseBean;
import com.leyou.game.util.BitmapUtil;
import com.leyou.game.widget.TextViewRotate45;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 填写收获地址 截屏页面
 *
 * @author : rocky
 * @Create Time : 2017/12/4 下午7:53
 * @Modified Time : 2017/12/4 下午7:53
 */
public class ScreenShotPrizeDialog extends BaseDialog {
    private final GameWinPriseBean awardInfo;
    @BindView(R.id.tv_prize_name)
    TextView tvPrizeName;
    @BindView(R.id.iv_win_prize_img)
    SimpleDraweeView ivWinPrizeImg;
    @BindView(R.id.tv_nick_name)
    TextViewRotate45 tvNickName;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_share)
    TextView tvShare;
    private Context context;
    private OnViewClickListener listener;

    public ScreenShotPrizeDialog(Context context, GameWinPriseBean awardInfo) {
        super(context);
        this.context = context;
        this.awardInfo = awardInfo;
    }

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_screen_shot_prize_view;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ivWinPrizeImg.setImageURI(awardInfo.getPrizeImgUrl());
        tvNickName.setText(UserData.getInstance().getNickname());
        tvPrizeName.setText(awardInfo.getPrizeName());
    }

    @OnClick({R.id.tv_cancel, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_share:
                String path = BitmapUtil.savePhotoToSDCard(BitmapUtil.getViewBitmap(llRoot),
                        Constants.IMAGE_CACHE_DIR, "" + UserData.getInstance().getBirthday() + "" + System.currentTimeMillis());
                listener.share(path);
                dismiss();
                break;
        }
    }

    public interface OnViewClickListener {
        void share(String path);
    }
}
