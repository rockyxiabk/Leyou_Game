package com.leyou.game.activity.treasure;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.treasure.EditWorkerAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.event.WorkerPlaceEvent;
import com.leyou.game.ipresenter.treasure.IWorkerDismissActivity;
import com.leyou.game.presenter.treasure.WorkerDismissPresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.TextExplainDialog;
import com.leyou.game.widget.dialog.WorkerModifyDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 解雇矿工页面
 *
 * @author : rocky
 * @Create Time : 2017/5/3 上午10:46
 * @Modified By: rocky
 * @Modified Time : 2017/5/3 上午10:46
 */
public class WorkerDismissActivity extends BaseActivity implements IWorkerDismissActivity, CustomItemClickListener {

    @BindView(R.id.iv_dismiss_back)
    ImageView ivDismissBack;
    @BindView(R.id.tv_dismiss_explain)
    TextView tvDismissExplain;
    @BindView(R.id.iv_dismiss_worker_img)
    ImageView ivDismissWorkerImg;
    @BindView(R.id.iv_worker_star)
    ImageView ivWorkerStar;
    @BindView(R.id.tv_dismiss_worker_power)
    TextView tvDismissWorkerPower;
    @BindView(R.id.btn_dismiss_worker_confirm)
    Button btnDismissWorkerConfirm;
    @BindView(R.id.recycler_dismiss_worker)
    RecyclerView recyclerDismissWorker;
    @BindView(R.id.tv_dismiss_worker_state)
    TextView tvDismissWorkerState;
    @BindView(R.id.round_pr_phy)
    RoundCornerProgressBar roundPrPhy;
    @BindView(R.id.tv_dismiss_worker_phy)
    TextView tvDismissWorkerPhy;
    @BindView(R.id.round_pr_power)
    RoundCornerProgressBar roundPrPower;
    @BindView(R.id.ll_round_dismiss)
    LinearLayout llRoundDismiss;
    private List<WorkerBean> list = new ArrayList<>();
    private WorkerDismissPresenter presenter;
    private EditWorkerAdapter adapter;
    private LoadingProgressDialog loadingProgressDialog;
    private WorkerBean workerDismiss;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_worker_dismiss;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        //添加适配器
        adapter = new EditWorkerAdapter(this, list);
        adapter.setOnItemClickListener(this);
        int SCREEN_WIDTH = ScreenUtil.getInstance(this).getScreenWidth();
        int number;
        if (SCREEN_WIDTH > 540) {
            number = Constants.TREASURE_FIVE_720;
        } else {
            number = Constants.TREASURE_FIVE_480;
        }
        recyclerDismissWorker.setLayoutManager(new GridLayoutManager(this, number));
        recyclerDismissWorker.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {
        presenter = new WorkerDismissPresenter(this, this);
    }

    @OnClick({R.id.iv_dismiss_back, R.id.tv_dismiss_explain, R.id.btn_dismiss_worker_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_dismiss_back:
                finishCurrentActivity();
                break;
            case R.id.tv_dismiss_explain:
                TextExplainDialog textExplainDialog = new TextExplainDialog(this, getString(R.string.treasure_dismissed_explain), getString(R.string.treasure_dismissed_explain_content));
                textExplainDialog.show();
                break;
            case R.id.btn_dismiss_worker_confirm:
                WorkerModifyDialog workerDialog = new WorkerModifyDialog(this, getString(R.string.treasure_dismissed_worker), "确定要解雇当前选择的矿工吗？");
                workerDialog.setWorkerListener(new WorkerModifyDialog.WorkerListener() {
                    @Override
                    public void confirm() {
                        if (null != workerDismiss) {
                            showLoading();
                            presenter.dismissWorker(workerDismiss.id);
                        }
                    }
                });
                workerDialog.show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void showMineWorkerData(List<WorkerBean> beanList) {
        workerDismiss = null;
        list.clear();
        if (null != beanList && beanList.size() > 0) {
            list.addAll(beanList);
            adapter.setWorkerAdapter(beanList);
            showDismissWorkerInfo(list.get(0));
        } else {
            showDismissWorkerInfo(null);
            adapter.setWorkerAdapter(beanList);
        }
    }

    @Override
    public void showDismissWorkerInfo(WorkerBean workerBean) {
        if (null != workerBean) {
            workerDismiss = workerBean;
            tvDismissWorkerState.setVisibility(View.GONE);
            llRoundDismiss.setVisibility(View.VISIBLE);
            Glide.with(this).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(ivDismissWorkerImg);

            tvDismissWorkerPower.setText("" + workerDismiss.attribute);
            float progress = (workerDismiss.attribute * 1.0f / (workerDismiss.maxAttribute * 1.0f)) * 100;
            roundPrPower.setProgress(progress);

            tvDismissWorkerPhy.setText(workerDismiss.phyPower + "");
            float phyProgress = (workerDismiss.phyPower / (100.0f + workerDismiss.starLevel * 10)) * 100;
            roundPrPhy.setProgress(phyProgress);
            if (workerBean.starLevel > 0) {
                ivWorkerStar.setVisibility(View.VISIBLE);
                switch (workerBean.starLevel) {
                    case 1:
                        ivWorkerStar.setImageResource(R.mipmap.icon_star_one);
                        break;
                    case 2:
                        ivWorkerStar.setImageResource(R.mipmap.icon_star_two);
                        break;
                    case 3:
                        ivWorkerStar.setImageResource(R.mipmap.icon_star_three);
                        break;
                    case 4:
                        ivWorkerStar.setImageResource(R.mipmap.icon_star_four);
                        break;
                    case 5:
                        ivWorkerStar.setImageResource(R.mipmap.icon_star_five);
                        break;
                }
            } else {
                ivWorkerStar.setVisibility(View.GONE);
            }
        } else {
            Glide.with(this).load("").error(R.mipmap.icon_add_worker).into(ivDismissWorkerImg);
            llRoundDismiss.setVisibility(View.GONE);
            tvDismissWorkerState.setVisibility(View.VISIBLE);
            ivWorkerStar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(this, false);
        loadingProgressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != loadingProgressDialog) {
            loadingProgressDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != loadingProgressDialog) {
            loadingProgressDialog.dismiss();
        }
    }

    @Override
    public void showMessageToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void sendEventDismissSuccess() {
        EventBus.getDefault().post(new WorkerPlaceEvent(1));
    }

    @Override
    public void onItemClick(View view, int position) {
        showDismissWorkerInfo(list.get(position));
    }
}
