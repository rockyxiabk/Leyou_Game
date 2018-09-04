package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.UserData;
import com.leyou.game.util.BitmapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog.mine
 *
 * @author : rocky
 * @Create Time : 2017/12/4 下午7:53
 * @Modified Time : 2017/12/4 下午7:53
 */
public class ScreenShotDialog extends BaseDialog {
    private String money;
    private Context context;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.iv_user_header)
    ImageView ivUserHeader;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    private OnViewClickListener listener;

    public ScreenShotDialog(Context context, String money) {
        super(context);
        this.context = context;
        this.money = money;
    }

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_screen_shot_view;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Glide.with(context).load(UserData.getInstance().getPictureUrl()).error(R.mipmap.ic_launcher).into(ivUserHeader);
        tvUserName.setText(UserData.getInstance().getNickname());
        tvMoney.setText("" + money + "元");
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
