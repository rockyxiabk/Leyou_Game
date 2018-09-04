package com.leyou.game.widget.loading;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description : com.leyou.game.widget.loading
 *
 * @author : rocky
 * @Create Time : 2017/5/9 下午4:38
 * @Modified Time : 2017/5/9 下午4:38
 */
public class LoadingDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.loadingView)
    LoadingView loadingView;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
        this.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_loading_view;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        loadingView.setLoadingText("数据加载中...");
    }
}
