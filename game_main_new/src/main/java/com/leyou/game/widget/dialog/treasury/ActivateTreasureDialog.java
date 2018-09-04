package com.leyou.game.widget.dialog.treasury;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.event.WorkerCanEmployEvent;
import com.leyou.game.util.HttpUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.util.api.TreasureApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Description : 激活宝库页面（新用户）
 *
 * @author : rocky
 * @Create Time : 2017/5/15 下午7:21
 * @Modified Time : 2017/5/15 下午7:21
 */
public class ActivateTreasureDialog extends BaseDialog {
    private Context context;
    @BindView(R.id.btn_activate_confirm)
    Button btnActivateConfirm;

    public ActivateTreasureDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_activate_treasure;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_activate_confirm)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_activate_confirm:
                activeTreasure();
                break;
        }
    }

    private void activeTreasure() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).activate(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("激活失败，再试一下");
                UserData.getInstance().setHasBaoku(false);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    queryStatus();
                } else {
                    ToastUtils.showToastShort("激活失败，再试一下");
                    UserData.getInstance().setHasBaoku(false);
                }
            }
        });
    }

    private void queryStatus() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).getTreasureActivateState(), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort("激活失败，再试一下");
                UserData.getInstance().setHasBaoku(false);
            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    UserData.getInstance().setHasBaoku(true);
                    EventBus.getDefault().post(new WorkerCanEmployEvent(1));
                    ToastUtils.showToastShort("激活成功");
                    dismiss();
                } else {
                    ToastUtils.showToastShort("激活失败，再试一下");
                    UserData.getInstance().setHasBaoku(false);
                }
            }
        });
    }
}
