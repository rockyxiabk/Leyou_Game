package com.leyou.game.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.leyou.game.R;

/**
 * Description : 自定义弹窗样式
 *
 * @author : rocky
 * @Create Time : 2017/4/20 上午9:32
 * @Modified Time : 2017/4/20 上午9:32
 */
public abstract class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        super(context, R.style.CustomBaseDialog);
        getWindow().setWindowAnimations(R.style.iosalertdialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
    }

    public abstract int getLayout();

    protected abstract void initView();
}
