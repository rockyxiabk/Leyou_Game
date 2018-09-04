package com.leyou.game.widget.fightdialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WolfPropBean;
import com.leyou.game.event.PropShopEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.WolfKillApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 狼人杀游戏角色道具购买
 *
 * @author : rocky
 * @Create Time : 2017/7/31 上午11:41
 * @Modified Time : 2017/7/31 上午11:41
 */
public class RolePropBuyDialog extends BaseDialog {

    private Context context;
    private WolfPropBean wolfPropBean;
    private int number = 1;
    @BindView(R.id.tv_buy_prop_title)
    TextView tvBuyPropTitle;
    @BindView(R.id.iv_buy_prop_close)
    ImageView ivBuyPropClose;
    @BindView(R.id.tv_buy_prop_des)
    TextView tvBuyPropDes;
    @BindView(R.id.iv_prop_img)
    ImageView ivPropImg;
    @BindView(R.id.tv_prop_name)
    TextView tvPropName;
    @BindView(R.id.btn_minus)
    ImageView btnMinus;
    @BindView(R.id.tv_prop_number)
    TextView tvPropNumber;
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.tv_chips_count)
    TextView tvChipsCount;
    @BindView(R.id.ll_prop_buy)
    LinearLayout llPropBuy;

    public RolePropBuyDialog(Context context, WolfPropBean wolfPropBean) {
        super(context);
        this.context = context;
        this.wolfPropBean = wolfPropBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_role_buy_prop;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvBuyPropDes.setText(wolfPropBean.roleDes);
        Glide.with(context).load(wolfPropBean.pictureUrl).error(R.mipmap.icon_default).into(ivPropImg);
        tvPropName.setText(wolfPropBean.name);
        tvChipsCount.setText(String.valueOf(wolfPropBean.price));
    }

    @OnClick({R.id.iv_buy_prop_close, R.id.btn_minus, R.id.btn_add, R.id.ll_prop_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_buy_prop_close:
                dismiss();
                break;
            case R.id.btn_minus:
                if (number == 1) {
                    ToastUtils.showToastShort("至少购买一个");
                } else {
                    number--;
                }
                break;
            case R.id.btn_add:
                number++;
                tvPropNumber.setText("" + number);
                break;
            case R.id.ll_prop_buy:
                if (number > 0) {
                    buyProp();
                }
                break;
        }
    }

    private void buyProp() {
        HttpUtil.subscribe(HttpUtil.createApi(WolfKillApi.class, Constants.WOLF_KILL_URL).buyProp(wolfPropBean.mark, number), new Observer<Result>() {
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
}
