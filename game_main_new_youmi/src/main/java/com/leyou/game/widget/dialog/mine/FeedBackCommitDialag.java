package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.os.Handler;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;

import butterknife.ButterKnife;

/**
 * Description : 意见反馈成功
 *
 * @author : rocky
 * @Create Time : 2017/8/7 上午10:14
 * @Modified Time : 2017/8/7 上午10:14
 */
public class FeedBackCommitDialag extends BaseDialog {

    private Context context;
    private Handler handler = new Handler();

    public FeedBackCommitDialag(Context context) {
        super(context);
        this.context = context;
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_feed_back_commit;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 2000);
    }
}
