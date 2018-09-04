package com.leyou.game.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.bean.WorkerExtBean;
import com.leyou.game.event.RefreshEvent;
import com.leyou.game.event.WorkerCanEmployEvent;
import com.leyou.game.event.WorkerPlaceEvent;
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
 * @Create Time : 2017/6/3 下午4:09
 * @Modified Time : 2017/6/3 下午4:09
 */
public class EmployWorkerDialog extends BaseDialog {
    private final Context context;
    private final WorkerBean workerBean;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    public EmployWorkerDialog(Context context, WorkerBean workerBean) {
        super(context);
        this.context = context;
        this.workerBean = workerBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_employ_worker;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                employ();
                break;
        }
    }

    private void employ() {
        HttpUtil.subscribe(HttpUtil.createApi(TreasureApi.class, Constants.TREASURE_URL).employWorker(workerBean.typeId, workerBean.typeName, workerBean.attribute), new Observer<Result<WorkerExtBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToastShort(context.getString(R.string.treasure_worker_employ_failed));
                dismiss();
            }

            @Override
            public void onNext(Result<WorkerExtBean> stringResult) {
                int result = stringResult.result;
                WorkerExtBean data = stringResult.data;
                if (result == 1) {
                    EventBus.getDefault().post(new WorkerCanEmployEvent(1));
                    ToastUtils.showToastShort(data.message);
                } else {
                    ToastUtils.showToastShort(data.message);
                }
                dismiss();
            }
        });
    }
}
