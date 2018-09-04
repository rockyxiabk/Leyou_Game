package com.leyou.game.activity.treasure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.treasure.TreasureBigAdapter;
import com.leyou.game.adapter.treasure.TreasureWorkerAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.event.RefreshTreasureEvent;
import com.leyou.game.ipresenter.treasure.ITreasureActivity;
import com.leyou.game.presenter.treasure.TreasureActivityPresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.GuideView;
import com.leyou.game.widget.dialog.ChooseWorkerAttackTreasureDialog;
import com.leyou.game.widget.dialog.ChooseWorkerModifyTreasureDialog;
import com.leyou.game.widget.dialog.DeleteTreasureDialog;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.RefreshTreasureDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

/**
 * Description : 单个宝库页面显示
 *
 * @author : rocky
 * @Create Time : 2017/6/23 上午11:32
 * @Modified By: rocky
 * @Modified Time : 2017/6/23 上午11:32
 */
public class TreasureActivity extends BaseActivity implements ITreasureActivity, CustomItemClickListener {

    @BindView(R.id.iv_treasure_bg)
    ImageView ivTreasureBg;
    @BindView(R.id.iv_treasure_gif)
    GifImageView ivTreasureGif;
    @BindView(R.id.iv_treasure_no_attach_animal)
    ImageView ivTreasurreAttachAnimal;
    @BindView(R.id.recycler_worker)
    RecyclerView recyclerWorker;
    @BindView(R.id.tv_renew_worker)
    TextView tvRenewWorker;
    @BindView(R.id.re_treasure_container_chips)
    RelativeLayout reTreasureContainerChips;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.recycler_treasure)
    RecyclerView recyclerTreasureRefreshTime;
    @BindView(R.id.btn_find_new_treasure)
    Button btnFindNewTreasure;
    @BindView(R.id.btn_abandon_treasure)
    Button btnAbandonTreasure;
    @BindView(R.id.iv_find_treasure_gif)
    GifImageView gifFindTreasure;
    private List<TreasureBean> treasureBeanList = new ArrayList<>();
    private LoadingProgressDialog loadingDialog;
    private TreasureBigAdapter treasureAdapter;
    private TreasureBean currentTreasureBean;
    private TreasureActivityPresenter presenter;

    private List<WorkerBean> workerList = new ArrayList<>();
    private TreasureWorkerAdapter workerAdapter;
    private TreasureBean findTreasure;
    private int findLevel;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshTreasure(RefreshTreasureEvent event) {
        if (event.getType() == 1) {
            currentTreasureBean = null;
            presenter.queryTreasures();
        }
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_treasure;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        btnAbandonTreasure.setSelected(true);
        btnFindNewTreasure.setSelected(true);

        Intent intent = getIntent();
        findLevel = intent.getIntExtra("level", 0);
        if (findLevel >= 4) {
            findTreasure = intent.getParcelableExtra("treasure");
        }

        //我的宝库
        treasureAdapter = new TreasureBigAdapter(this, treasureBeanList);
        treasureAdapter.setOnItemClickListener(this);
        recyclerTreasureRefreshTime.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerTreasureRefreshTime.setAdapter(treasureAdapter);

        //当前所选宝库矿工
        workerAdapter = new TreasureWorkerAdapter(this, workerList);
        recyclerWorker.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerWorker.setAdapter(workerAdapter);

    }

    @Override
    public void initPresenter() {
        presenter = new TreasureActivityPresenter(this, this);
        if (null == findTreasure) {
            presenter.queryTreasures();
        }
    }

    @OnClick({R.id.tv_renew_worker, R.id.tv_back, R.id.btn_find_new_treasure, R.id.btn_abandon_treasure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_renew_worker:
                if (null != currentTreasureBean) {
                    ChooseWorkerModifyTreasureDialog modifyTreasureDialog = new ChooseWorkerModifyTreasureDialog(this, currentTreasureBean);
                    modifyTreasureDialog.show();
                }
                break;
            case R.id.tv_back:
                finishCurrentActivity();
                break;
            case R.id.btn_find_new_treasure:
                RefreshTreasureDialog refreshTreasureDialog = new RefreshTreasureDialog(this);
                refreshTreasureDialog.setRefreshTreasureListener(new RefreshTreasureDialog.RefreshTreasureListener() {
                    @Override
                    public void confirm() {
                        presenter.refreshTreasure();
                    }
                });
                refreshTreasureDialog.show();
                break;
            case R.id.btn_abandon_treasure:
                if (null != currentTreasureBean) {
                    DeleteTreasureDialog deleteTreasureDialog = new DeleteTreasureDialog(this);
                    deleteTreasureDialog.setDeleteTreasureListener(new DeleteTreasureDialog.DeleteTreasureListener() {
                        @Override
                        public void confirm() {
                            presenter.deleteTreasure(currentTreasureBean.id);
                            removeChipView();
                        }
                    });
                    deleteTreasureDialog.show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (null != treasureBeanList && treasureBeanList.size() > 0 && position < treasureBeanList.size()) {
            currentTreasureBean = treasureBeanList.get(position);
            showCurrentTreasureInfo(currentTreasureBean);
            treasureAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setTreasureData(List<TreasureBean> treasureList) {
        treasureBeanList.clear();
        if (null != treasureList && treasureList.size() > 0) {
            treasureBeanList.addAll(treasureList);
            treasureAdapter.setTreasureBigAdapter(treasureList);
            showCurrentTreasureInfo(treasureList.get(0));
            tvRenewWorker.setVisibility(View.VISIBLE);
        } else {
            treasureAdapter.setTreasureBigAdapter(treasureList);
            showCurrentTreasureInfo(null);
            setTreasureWorkerData(null);
            tvRenewWorker.setVisibility(View.GONE);
        }
        dismissedLoading();
    }

    @Override
    public void setTreasureWorkerData(List<WorkerBean> data) {
        workerList.clear();
        if (null != data && data.size() > 0) {
            workerList.addAll(data);
            workerAdapter.setTreasureWorkerAdapter(data);
            ivTreasurreAttachAnimal.setVisibility(View.GONE);
        } else {
            workerAdapter.setTreasureWorkerAdapter(null);
            ivTreasurreAttachAnimal.setVisibility(View.VISIBLE);
            ivTreasurreAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_one_animal);
        }
        dismissedLoading();
    }

    @Override
    public void showCurrentTreasureInfo(TreasureBean treasureBean) {
        if (null != treasureBean) {
            currentTreasureBean = treasureBean;
            switch (treasureBean.workerNum) {
                case 1:
                    ivTreasureGif.setImageResource(R.mipmap.icon_worker_one);
                    break;
                case 2:
                    ivTreasureGif.setImageResource(R.mipmap.icon_worker_two);
                    break;
                case 3:
                    ivTreasureGif.setImageResource(R.mipmap.icon_worker_three);
                    break;
                case 4:
                    ivTreasureGif.setImageResource(R.mipmap.icon_worker_four);
                    break;
                case 5:
                    ivTreasureGif.setImageResource(R.mipmap.icon_worker_five);
                    break;
                default:
                    ivTreasureGif.setImageResource(R.mipmap.icon_treasury_bg_one);
                    break;
            }
            Integer int_type;
            try {
                int_type = Integer.valueOf(treasureBean.level);
            } catch (Exception e) {
                int_type = 0;
            }
            switch (int_type) {
                case 1:
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_one);
                    break;
                case 2:
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_two);
                    break;
                case 3:
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_three);
                    break;
                case 4:
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_four);
                    break;
                case 5:
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_five);
                    break;
                default:
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_one);
                    break;
            }
            presenter.getTreasureWorker(currentTreasureBean.id);
            presenter.queryCanHarvestChips(currentTreasureBean.id);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.queryCanHarvestProp(currentTreasureBean.id);
                }
            }, 500);

        } else {
            currentTreasureBean = null;
            ivTreasureGif.setImageResource(R.mipmap.icon_treasury_bg_one);
        }
    }

    @Override
    public void showChipsView(final int chipsNumber) {
        if (chipsNumber > 0) {
            reTreasureContainerChips.removeAllViews();
            TextView textView = new TextView(this);
            textView.setBackgroundResource(R.mipmap.icon_debris);
            textView.setText("" + chipsNumber + "");
            textView.setTextSize(9);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.white));
            if (null != currentTreasureBean) {
                long time = currentTreasureBean.residueTime - System.currentTimeMillis();
                if (time <= 0) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            presenter.harvestChips(currentTreasureBean, chipsNumber);
                        }
                    });
                }
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(this, 37), ScreenUtil.dp2px(this, 43));
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            textView.setLayoutParams(layoutParams);
            reTreasureContainerChips.addView(textView);
        } else {
            reTreasureContainerChips.removeAllViews();
        }
    }

    @Override
    public void showPropView(List<PropBean> propList) {
        for (int i = 0; i < propList.size(); i++) {
            if (propList.get(i).itemNum > 0) {
                TextView textView = new TextView(this);
                textView.setText(propList.get(i).itemName + "\nx" + propList.get(i).itemNum);
                textView.setBackgroundResource(R.mipmap.icon_debris);
                textView.setTextSize(9);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.white));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(this, 37), ScreenUtil.dp2px(this, 43));
                layoutParams.setMargins(new Random().nextInt(reTreasureContainerChips.getWidth() - 100), new Random().nextInt(reTreasureContainerChips.getHeight() - 100), 0, 0);
                textView.setLayoutParams(layoutParams);
                reTreasureContainerChips.addView(textView);
            }
        }
    }

    @Override
    public void raceTreasure(TreasureBean treasureBean) {
        if (null != treasureBean) {
            ChooseWorkerAttackTreasureDialog attackTreasureDialog = new ChooseWorkerAttackTreasureDialog(this, treasureBean, 0);
            attackTreasureDialog.show();
        }
    }

    @Override
    public void removeChipView() {
        reTreasureContainerChips.removeAllViews();
    }

    @Override
    public void deleteTreasure(TreasureBean treasureBean) {
        DeleteTreasureDialog deleteTreasureDialog = new DeleteTreasureDialog(this);
        deleteTreasureDialog.setDeleteTreasureListener(new DeleteTreasureDialog.DeleteTreasureListener() {
            @Override
            public void confirm() {
                presenter.deleteTreasure(currentTreasureBean.id);
            }
        });
        deleteTreasureDialog.show();
    }

    @Override
    public void showLoading() {
        loadingDialog = new LoadingProgressDialog(this, false);
        loadingDialog.show();
    }

    @Override
    public void changeLoadingDes(String text) {
        if (null != loadingDialog) {
            loadingDialog.setLoadingText(text);
        }
    }

    @Override
    public void dismissedLoading() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessageToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        if (findLevel == 4) {
            gifFindTreasure.setVisibility(View.VISIBLE);
            final ChooseWorkerAttackTreasureDialog attackTreasureDialog = new ChooseWorkerAttackTreasureDialog(this, findTreasure, 1);
            gifFindTreasure.setImageResource(R.mipmap.icon_treasure_find_four);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gifFindTreasure.setVisibility(View.GONE);
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_four);
                    attackTreasureDialog.show();
                }
            }, 1500);

        } else if (findLevel == 5) {
            gifFindTreasure.setVisibility(View.VISIBLE);
            final ChooseWorkerAttackTreasureDialog attackTreasureDialog = new ChooseWorkerAttackTreasureDialog(this, findTreasure, 1);
            gifFindTreasure.setImageResource(R.mipmap.icon_treasure_find_five);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gifFindTreasure.setVisibility(View.GONE);
                    ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_five);
                    attackTreasureDialog.show();
                }
            }, 1500);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    private void showGuideView() {
//        // 使用图片
//        final ImageView iv = new ImageView(this);
//        iv.setImageResource(R.mipmap.icon_arrow_find_treasure);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        guideViewTreasure = GuideView.Builder
//                .newInstance(this)
//                .setTargetView(btnFindNewTreasure)//设置目标
//                .setCustomGuideView(iv)
//                .setOnclickExit(true)
//                .setDirction(GuideView.Direction.LEFT_BOTTOM)
//                .setShape(GuideView.MyShape.RECTANGULAR)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewTreasure.hide();
//                        presenter.refreshTreasure();
//                    }
//                })
//                .build();
//        boolean isDiamond = SPUtil.getBoolean(this, SPUtil.INDUCE, "find_treasure");
//        boolean isNewPerson = SPUtil.getBoolean(this, SPUtil.INDUCE, "isNewPerson");
//        if (!isDiamond && isNewPerson) {
//            SPUtil.setBoolean(this, SPUtil.INDUCE, "find_treasure", true);
//            guideViewTreasure.show();
//        }
//    }
}
