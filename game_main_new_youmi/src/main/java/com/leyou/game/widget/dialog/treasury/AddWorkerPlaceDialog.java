package com.leyou.game.widget.dialog.treasury;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.event.WorkerPlaceEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.TreasureApi;
import com.leyou.game.widget.dialog.LoadingProgressDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 添加更多矿工位
 *
 * @author : rocky
 * @Create Time : 2017/6/7 下午4:38
 * @Modified Time : 2017/6/7 下午4:38
 */
public class AddWorkerPlaceDialog extends BaseDialog {

    @BindView(R.id.tv_treasure_add_worker_des)
    TextView tvTreasureAddWorkerDes;
    @BindView(R.id.btn_treasure_cancel)
    Button btnTreasureCancel;
    @BindView(R.id.btn_treasure_confirm)
    Button btnTreasureConfirm;
    private Context context;
    private int coin;
    private LoadingProgressDialog progressDialog;

    public AddWorkerPlaceDialog(Context context, int coin) {
        super(context);
        this.context = context;
        this.coin = coin;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_treasure_add_worker;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        tvTreasureAddWorkerDes.setText("花费" + coin + "颗钻石，开启一个矿工位，最多开启25个矿工位");
        progressDialog = new LoadingProgressDialog(context, false);
    }

    @OnClick({R.id.btn_treasure_cancel, R.id.btn_treasure_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_treasure_cancel:
                dismiss();
                break;
            case R.id.btn_treasure_confirm:
                progressDialog.show();
                addWorkerHolder();
                break;
        }
    }

    private void addWorkerHolder() {
        progressDialog.setLoadingText("更多矿工位开启中...");
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).addWorkerPlaceCount(), new Observer<Result<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("开启失败，请重新开启～");
                progressDialog.dismiss();
                dismiss();
            }

            @Override
            public void onNext(Result<String> stringResult) {
                if (stringResult.result == 1) {
                    EventBus.getDefault().post(new WorkerPlaceEvent(1));
                    ToastUtils.showToastShort("开启成功，去雇用更多可用矿工吧～");
                } else {
                    ToastUtils.showToastShort("开启失败，请重新开启～");
                }
                progressDialog.dismiss();
                dismiss();
            }
        });
    }
}
