package com.leyou.game.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.WebViewActivity;
import com.leyou.game.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/9/5 下午1:59
 * @Modified Time : 2017/9/5 下午1:59
 */
public class DiamondConvertFlowDialog extends BaseDialog {

    private Context context;
    @BindView(R.id.iv_mb)
    ImageView ivMb;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_btn)
    ImageView ivBtn;

    public DiamondConvertFlowDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_diamond_convert;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_close, R.id.iv_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_btn:
                Intent rule = new Intent(context, WebViewActivity.class);
                rule.putExtra("title", context.getResources().getString(R.string.diamond_convert));
                rule.putExtra("url", Constants.DIAMOND_CONVERT_URL);
                rule.putExtra("type", 1);
                context.startActivity(rule);
                dismiss();
                break;
        }
    }
}
