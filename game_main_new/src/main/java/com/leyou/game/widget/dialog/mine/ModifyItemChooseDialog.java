package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 个人信息修改 选择性别和头像
 *
 * @author : rocky
 * @Create Time : 2017/10/24 下午6:50
 * @Modified Time : 2017/10/24 下午6:50
 */
public class ModifyItemChooseDialog extends BaseDialog {

    @BindView(R.id.tv_item_1)
    TextView tvItem1;
    @BindView(R.id.ll_item_1)
    LinearLayout llItem1;
    @BindView(R.id.tv_item_2)
    TextView tvItem2;
    @BindView(R.id.ll_item_2)
    LinearLayout llItem2;
    @BindView(R.id.tv_item_3)
    TextView tvItem3;
    @BindView(R.id.ll_item_3)
    LinearLayout llItem3;
    private String text1;
    private String text2;
    private String text3;
    private OnClickItemListener listener;

    public ModifyItemChooseDialog(Context context, String text1, String text2, String text3) {
        super(context);
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void setOnItemClickListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_modify_item_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvItem1.setText(text1);
        tvItem2.setText(text2);
        tvItem3.setText(text3);
    }

    @OnClick({R.id.ll_item_1, R.id.ll_item_2, R.id.ll_item_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_item_1:
                listener.onClickItem(1);
                break;
            case R.id.ll_item_2:
                listener.onClickItem(2);
                break;
            case R.id.ll_item_3:
                listener.onClickItem(3);
                break;
        }
        dismiss();
    }

    public interface OnClickItemListener {
        void onClickItem(int position);
    }
}
