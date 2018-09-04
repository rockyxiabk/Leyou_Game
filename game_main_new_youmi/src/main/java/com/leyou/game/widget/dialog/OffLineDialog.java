package com.leyou.game.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leyou.game.GameApplication;
import com.leyou.game.R;
import com.leyou.game.activity.RegisterActivity;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.PushBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 下线弹窗提示
 *
 * @author : rocky
 * @Create Time : 2017/4/27 上午11:47
 * @Modified Time : 2017/4/27 上午11:47
 */
public class OffLineDialog extends BaseDialog {
    private Context context;
    private PushBean pushBean;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.btn_re_login)
    Button btnReLogIn;

    public OffLineDialog(Context context, PushBean pushBean) {
        super(context);
        this.context = context;
        this.pushBean = pushBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_off_line;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvDes.setText(pushBean.des);
    }

    @OnClick({R.id.btn_exit, R.id.btn_re_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_exit:
                GameApplication.exit();
                dismiss();
                break;
            case R.id.btn_re_login:
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dismiss();
                break;
        }
    }
}
