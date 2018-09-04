package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.Result;
import com.leyou.game.event.PropShopEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.TreasureApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : com.leyou.game.widget.dialog
 *
 * @author : rocky
 * @Create Time : 2017/6/30 下午1:33
 * @Modified Time : 2017/6/30 下午1:33
 */
public class BuyPropDialog extends BaseDialog {

    @BindView(R.id.tv_buy_prop_title)
    TextView tvBuyPropTitle;
    @BindView(R.id.tv_buy_prop_des)
    TextView tvBuyPropDes;
    @BindView(R.id.iv_prop_img)
    ImageView ivPropImg;
    @BindView(R.id.tv_prop_name)
    TextView tvPropName;
    @BindView(R.id.tv_chips_number)
    TextView tvChipsNumber;
    @BindView(R.id.tv_diamond_number)
    TextView tvDiamondNumber;
    @BindView(R.id.btn_diamond_buy)
    Button btnDiamondBuy;
    @BindView(R.id.btn_chips_buy)
    Button btnChipsBuy;
    @BindView(R.id.iv_buy_prop_close)
    ImageView ivBuyPropClose;
    @BindView(R.id.btn_minus)
    ImageView btnMinus;
    @BindView(R.id.tv_prop_number)
    TextView tvPropNumber;
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    private int number = 1;
    private Context context;
    private PropBean propBean;

    public BuyPropDialog(Context context, PropBean propBean) {
        super(context);
        this.context = context;
        this.propBean = propBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_buy_prop;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvBuyPropDes.setText(propBean.itemContent);
        Glide.with(context).load(propBean.pictureUrl).error(R.mipmap.icon_default).into(ivPropImg);
        tvPropName.setText(propBean.itemName);
        tvChipsNumber.setText("" + propBean.itemPrice1);
        tvDiamondNumber.setText("" + propBean.itemPrice2);
    }

    private void buyProp(int type, int number) {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).buyProp(propBean.id, number, type), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort(context.getString(R.string.order_pay_failed));
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    ToastUtils.showToastShort(context.getString(R.string.order_pay_success));
                    EventBus.getDefault().post(new PropShopEvent(1));
                    dismiss();
                } else {
                    ToastUtils.showToastShort(context.getString(R.string.order_pay_failed));
                    dismiss();
                }
            }
        });
    }

    @OnClick({R.id.iv_buy_prop_close, R.id.btn_minus, R.id.btn_add, R.id.btn_diamond_buy, R.id.btn_chips_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_minus:
                if (number == 1) {
                    ToastUtils.showToastShort("至少购买一个");
                } else {
                    number--;
                }
                tvPropNumber.setText("" + number);
                break;
            case R.id.btn_add:
                number++;
                tvPropNumber.setText("" + number);
                break;
            case R.id.iv_buy_prop_close:
                dismiss();
                break;
            case R.id.btn_diamond_buy:
                buyProp(1, number);
                break;
            case R.id.btn_chips_buy:
                buyProp(0, number);
                break;
        }
    }
}
