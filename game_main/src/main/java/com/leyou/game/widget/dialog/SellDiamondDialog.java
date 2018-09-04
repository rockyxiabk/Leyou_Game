package com.leyou.game.widget.dialog;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.ExChangeBuyBean;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.TradeApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 钻石收购，交易钻石
 *
 * @author : rocky
 * @Create Time : 2017/6/21 下午4:12
 * @Modified Time : 2017/6/21 下午4:12
 */
public class SellDiamondDialog extends BaseDialog {
    private ExChangeBuyBean exChangeBean;
    private Context context;
    @BindView(R.id.iv_purchase_diamond_close)
    ImageView ivPurchaseDiamondClose;
    @BindView(R.id.et_sell_diamond_num_input)
    EditText etSellDiamondNumInput;
    @BindView(R.id.tv_sell_total_diamond)
    TextView tvSellTotalDiamond;
    @BindView(R.id.tv_sell_money)
    TextView tvSellMoney;
    @BindView(R.id.btn_sell_diamond)
    Button btnSellDiamond;
    private int sellDiamondNumber;
    private Handler handler = new Handler();

    public SellDiamondDialog(Context context, ExChangeBuyBean exChangeBean) {
        super(context);
        this.context = context;
        this.exChangeBean = exChangeBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_sell_diamond;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);


        tvSellTotalDiamond.setText("当前最多可出售" + exChangeBean.currVirtualCoin + "个钻石");
        etSellDiamondNumInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (!TextUtils.isEmpty(string)) {
                    Integer number = Integer.valueOf(string);
                    if (0 == number % 10) {
                        sellDiamondNumber = number;
                        calculateMoney();
                    } else {
                        sellDiamondNumber = 0;
                        ToastUtils.showToastLong("请输入10的整数倍");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void calculateMoney() {
        if (sellDiamondNumber > UserData.getInstance().getDiamonds()) {
            ToastUtils.showToastShort("账户钻石数量不足！");
        } else {
            double money = exChangeBean.price * sellDiamondNumber;
            tvSellMoney.setText("" + money);
        }
    }

    @OnClick({R.id.iv_purchase_diamond_close, R.id.et_sell_diamond_num_input, R.id.btn_sell_diamond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_purchase_diamond_close:
                KeyBoardUtils.closeKeyboard(etSellDiamondNumInput, context);
                dismiss();
                break;
            case R.id.et_sell_diamond_num_input:
                KeyBoardUtils.openKeyboard(etSellDiamondNumInput, context);
                break;
            case R.id.btn_sell_diamond:
                KeyBoardUtils.closeKeyboard(etSellDiamondNumInput, context);
                sell();
                break;
        }
    }

    private void sell() {
        HttpUtil.subscribe(HttpUtil.createApi(TradeApi.class, Constants.URL).purcharsSell(sellDiamondNumber, exChangeBean.id), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showMessageToast("交易失败");
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    showMessageToast("交易成功");
                    EventBus.getDefault().post(new RefreshEvent(RefreshEvent.REFRESH, RefreshEvent.MINE));
                    dismiss();
                } else {
                    showMessageToast("交易失败");
                }
            }
        });
    }

    private void showMessageToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }
}
