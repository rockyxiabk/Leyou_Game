package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog.mine
 *
 * @author : rocky
 * @Create Time : 2017/12/4 下午4:25
 * @Modified Time : 2017/12/4 下午4:25
 */
public class ShareApplyCashDialog extends BaseDialog {
    @BindView(R.id.ll_item_qq_zone)
    LinearLayout llItemQqZone;
    @BindView(R.id.ll_item_wx_zone)
    LinearLayout llItemWxZone;
    @BindView(R.id.iv_close)
    ImageView ivCancel;
    private OnViewClickListener onViewClickListener;

    public ShareApplyCashDialog(Context context) {
        super(context);
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_with_cash_share;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_item_qq_zone, R.id.ll_item_wx, R.id.ll_item_wx_zone, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_item_qq_zone:
                onViewClickListener.shareChannel(SHARE_MEDIA.QZONE);
                break;
            case R.id.ll_item_wx_zone:
                onViewClickListener.shareChannel(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_item_wx:
                onViewClickListener.shareChannel(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.iv_close:
                onViewClickListener.exit();
                break;
        }
    }

    public interface OnViewClickListener {

        void shareChannel(SHARE_MEDIA type);

        void exit();
    }
}
