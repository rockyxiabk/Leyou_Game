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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.treasure.EditWorkerAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.event.WorkerPlaceEvent;
import com.leyou.game.ipresenter.treasure.IWorkerUpgradeActivity;
import com.leyou.game.presenter.treasure.WorkerUpgradePresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.WebViewDialog;
import com.leyou.game.widget.dialog.WorkerModifyDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

/**
 * Description : 升级矿工页面
 *
 * @author : rocky
 * @Create Time : 2017/5/3 上午10:49
 * @Modified By: rocky
 * @Modified Time : 2017/5/3 上午10:49
 */
public class WorkerUpgradeActivity extends BaseActivity implements IWorkerUpgradeActivity, CustomItemClickListener {

    @BindView(R.id.iv_upgrade_back)
    ImageView ivUpgradeBack;
    @BindView(R.id.tv_upgrade_explain)
    TextView tvUpgradeExplain;
    @BindView(R.id.iv_upgrade_worker_img)
    ImageView ivUpgradeWorkerImg;
    @BindView(R.id.iv_worker_star)
    ImageView ivWorkerStar;
    @BindView(R.id.tv_upgrade_des)
    TextView tvUpgradeDes;
    @BindView(R.id.gif_worker_upgrade)
    GifImageView gifImageView;
    @BindView(R.id.tv_upgrade_worker_power)
    TextView tvUpgradeWorkerPower;
    @BindView(R.id.btn_upgrade_worker_confirm)
    Button btnUpgradeWorkerConfirm;
    @BindView(R.id.recycler_upgrade_worker)
    RecyclerView recyclerUpgradeWorker;
    @BindView(R.id.tv_upgrade_worker_choose)
    TextView tvUpgradeWorkerChoose;
    @BindView(R.id.round_pr_phy)
    RoundCornerProgressBar roundPrPhy;
    @BindView(R.id.tv_upgrade_worker_phy)
    TextView tvUpgradeWorkerPhy;
    @BindView(R.id.round_pr_power)
    RoundCornerProgressBar roundPrPower;
    @BindView(R.id.ll_round_upgrade)
    LinearLayout llRoundUpgrade;
    @BindView(R.id.tv_diamond_number)
    TextView tvDiamondNumber;
    private WorkerUpgradePresenter presenter;
    private List<WorkerBean> list = new ArrayList<>();
    private EditWorkerAdapter adapter;
    private WorkerBean lastWorkerBean;
    private WorkerBean upgradeWorkerBean;
    private LoadingProgressDialog progressDialog;
    private boolean isUpgrading = false;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_worker_upgrade;
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
        recyclerUpgradeWorker.setLayoutManager(new GridLayoutManager(this, number));
        recyclerUpgradeWorker.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {
        presenter = new WorkerUpgradePresenter(this, this);
    }

    @OnClick({R.id.iv_upgrade_back, R.id.tv_upgrade_explain, R.id.btn_upgrade_worker_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_upgrade_back:
                finishCurrentActivity();
                break;
            case R.id.tv_upgrade_explain:
                WebViewDialog webViewDialog = new WebViewDialog(this, getString(R.string.treasure_upgrade_explain), Constants.TREASURE_WORKER_UPGRADE);
                webViewDialog.show();
                break;
            case R.id.btn_upgrade_worker_confirm:
                String des = "升级矿工需要消耗一定数量的钻石，确定要升级当前选择的矿工吗？";
                switch (upgradeWorkerBean.typeId) {
                    case 0:
                        des = "白色矿工每次升级消耗" + upgradeWorkerBean.levelUpConsume + "颗钻石（武力值随机增加1~3）,确定要升级当前选择的矿工吗？";
                        break;
                    case 1:
                        des = "橙色矿工每次升级消耗" + upgradeWorkerBean.levelUpConsume + "颗钻石（武力值随机增加2~5），确定要升级当前选择的矿工吗？";
                        break;
                    case 2:
                        des = "红色矿工每次升级消耗" + upgradeWorkerBean.levelUpConsume + "颗钻石（武力值随机增加3~8），确定要升级当前选择的矿工吗？";
                        break;
                    case 3:
                        des = "金色矿工每次升级消耗" + upgradeWorkerBean.levelUpConsume + "颗钻石（武力值随机增加5~10），确定要升级当前选择的矿工吗？";
                        break;
                    case 4:
                        des = "紫色矿工每次升级消耗" + upgradeWorkerBean.levelUpConsume + "颗钻石（武力值随机增加10~20），确定要升级当前选择的矿工吗？";
                        break;
                }
                if (upgradeWorkerBean.attribute >= upgradeWorkerBean.maxAttribute) {
                    showMessageToast("当前矿工已经满级");
                } else {
                    WorkerModifyDialog workerDialog = new WorkerModifyDialog(this, getString(R.string.treasure_upgrade_worker), des);
                    workerDialog.setWorkerListener(new WorkerModifyDialog.WorkerListener() {
                        @Override
                        public void confirm() {
                            showLoading();
                            isUpgrading = true;
                            presenter.upgradeWorker(upgradeWorkerBean);
                            btnUpgradeWorkerConfirm.setEnabled(false);
                            btnUpgradeWorkerConfirm.setText("升级中...");
                        }
                    });
                    if (!isUpgrading) {
                        workerDialog.show();
                    }
                }
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
    public void showMineWorkerData(List<WorkerBean> data) {
        list.clear();
        list.addAll(data);
        adapter.setWorkerAdapter(data);
        if (null != upgradeWorkerBean && null != list && list.size() > 0) {
            lastWorkerBean = upgradeWorkerBean;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equalsIgnoreCase(upgradeWorkerBean.id)) {
                    upgradeWorkerBean = list.get(i);
                }
            }
            showUpgradeNumber();
        } else {
            if (null != list && list.size() > 0) {
                upgradeWorkerBean = list.get(0);
            }
        }
        showUpgradeWorkerInfo();
    }

    @Override
    public void showUpgradeWorkerInfo() {
        if (null != upgradeWorkerBean) {
            Glide.with(this).load(upgradeWorkerBean.pictureUrl).error(R.mipmap.icon_default).into(ivUpgradeWorkerImg);

            llRoundUpgrade.setVisibility(View.VISIBLE);
            tvUpgradeWorkerChoose.setVisibility(View.GONE);
            tvDiamondNumber.setText(upgradeWorkerBean.levelUpConsume + "钻石");
            tvUpgradeWorkerPower.setText("" + upgradeWorkerBean.attribute);
            float progress = (upgradeWorkerBean.attribute * 1.0f / (upgradeWorkerBean.maxAttribute * 1.0f)) * 100;
            roundPrPower.setProgress(progress);

            tvUpgradeWorkerPhy.setText(upgradeWorkerBean.phyPower + "");
            float phyProgress = (upgradeWorkerBean.phyPower / (100.0f + upgradeWorkerBean.starLevel * 10)) * 100;
            roundPrPhy.setProgress(phyProgress);
            if (upgradeWorkerBean.starLevel > 0) {
                ivWorkerStar.setVisibility(View.VISIBLE);
                switch (upgradeWorkerBean.starLevel) {
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
            llRoundUpgrade.setVisibility(View.GONE);
            tvUpgradeWorkerChoose.setVisibility(View.VISIBLE);
            tvDiamondNumber.setText("0钻石");
            Glide.with(this).load("").error(R.mipmap.icon_add_worker).into(ivUpgradeWorkerImg);
            ivWorkerStar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showUpgradeNumber() {
        if (null != upgradeWorkerBean && null != lastWorkerBean) {
            final int count = upgradeWorkerBean.attribute - lastWorkerBean.attribute;
            gifImageView.setVisibility(View.VISIBLE);
            gifImageView.setImageResource(R.mipmap.icon_worker_upgrade);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (count > 0) {
                        tvUpgradeDes.setText("武力值 + " + count);
                        tvUpgradeDes.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.FadeInUp).duration(500).playOn(tvUpgradeDes);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gifImageView.setVisibility(View.GONE);
                                tvUpgradeDes.setVisibility(View.GONE);
                                isUpgrading = false;
                                btnUpgradeWorkerConfirm.setEnabled(true);
                                btnUpgradeWorkerConfirm.setText("升级");
                            }
                        }, 1000);
                    }
                }
            }, 10);
        }
    }

    @Override
    public void showUpgradeFailed() {
        isUpgrading = false;
        btnUpgradeWorkerConfirm.setEnabled(true);
        btnUpgradeWorkerConfirm.setText("升级");
    }

    @Override
    public void showLoading() {
        progressDialog = new LoadingProgressDialog(this, false);
        progressDialog.show();
    }

    @Override
    public void changeLoadingDes(String des) {
        if (null != progressDialog) {
            progressDialog.setLoadingText(des);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != progressDialog) {
            progressDialog.dismiss();
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
    public void sendEventUpgradeSuccess() {
        EventBus.getDefault().post(new WorkerPlaceEvent(1));
    }

    @Override
    public void onItemClick(View view, int position) {
        if (!isUpgrading) {
            upgradeWorkerBean = list.get(position);
            showUpgradeWorkerInfo();
        }
    }
}
