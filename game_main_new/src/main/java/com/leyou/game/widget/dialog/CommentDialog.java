package com.leyou.game.widget.dialog;

import android.content.Context;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;

/**
 * Description : 应用市场好评返钻石
 *
 * @author : rocky
 * @Create Time : 2017/12/4 下午2:50
 * @Modified Time : 2017/12/4 下午2:50
 */
public class CommentDialog extends BaseDialog {
    public CommentDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_comment;
    }

    @Override
    protected void initView() {

    }
}
