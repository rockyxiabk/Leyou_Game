package com.leyou.game.widget.dialog.mine;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.diamond.DiamondExchangeBean;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.NumberUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.newapi.DiamondApi;

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

    private DiamondExchangeBean diamondBean;
    private Context context;
    @BindView(R.id.et_sell_diamond_num_input)
    EditText etSellDiamondNumInput;
    @BindView(R.id.tv_sell_total_diamond)
    TextView tvSellTotalDiamond;
    @BindView(R.id.tv_sell_money)
    TextView tvSellMoney;
    @BindView(R.id.tv_sell_diamond)
    TextView textView;
    @BindView(R.id.tv_sale_tips)
    TextView tvSaleTips;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    private int sellDiamondNumber;
    private Handler handler = new Handler();

    public SellDiamondDialog(Context context, DiamondExchangeBean exChangeBean) {
        super(context);
        this.context = context;
        this.diamondBean = exChangeBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_sell_diamond;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        etSellDiamondNumInput.setHint("最多可出售" + diamondBean.diamondsNum + "钻石");
        tvSellTotalDiamond.setText("钻石余额:" + UserData.getInstance().getDiamonds() + "");
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
                } else {
                    sellDiamondNumber = 0;
                    calculateMoney();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getFee();
    }

    private void getFee() {
        HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).getExChangeFee(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    String data = result.data.toString();
                    if (!TextUtils.isEmpty(data)) {
                        tvSaleTips.setText(data);
                    }
                }
            }
        });
    }

    private void calculateMoney() {
        if (sellDiamondNumber > UserData.getInstance().getDiamonds()) {
            ToastUtils.showToastShort("账户钻石数量不足！");
        } else {
            double money = diamondBean.price * sellDiamondNumber;
            tvSellMoney.setText("预计收入:" + NumberUtil.getTwoPointNumber(money) + "元");
        }
    }

    @OnClick({R.id.et_sell_diamond_num_input, R.id.tv_sell_diamond, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_sell_diamond_num_input:
                KeyBoardUtils.openKeyboard(etSellDiamondNumInput, context);
                break;
            case R.id.tv_sell_diamond:
                KeyBoardUtils.closeKeyboard(etSellDiamondNumInput, context);
                if (sellDiamondNumber >= 10) {
                    sell();
                } else {
                    showMessageToast("至少出售10颗钻石");
                }
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    private void sell() {
        HttpUtil.subscribe(HttpUtil.createApi(DiamondApi.class, Constants.URL).purchase(sellDiamondNumber, diamondBean.id), new Observer<Result>() {
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
                    showMessageToast(result.msg);
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
