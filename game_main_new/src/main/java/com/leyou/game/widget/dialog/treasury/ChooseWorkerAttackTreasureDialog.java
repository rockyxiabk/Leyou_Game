package com.leyou.game.widget.dialog.treasury;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.treasure.ChooseWorkerAdapter;
import com.leyou.game.adapter.treasure.EnemyWorkerAdapter;
import com.leyou.game.adapter.treasure.TreasureAnimalAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.treasure.TreasureBean;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.event.RefreshTreasureEvent;
import com.leyou.game.ipresenter.treasure.IChooseWorkerDialog;
import com.leyou.game.presenter.treasure.ChooseWorkerDialogPresenter;
import com.leyou.game.util.NumberFormatUtil;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.treasury.TreasureTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 刷新宝窟 有人占领的宝窟 攻占宝窟
 *
 * @author : rocky
 * @Create Time : 2017/6/21 上午10:01
 * @Modified Time : 2017/6/21 上午10:01
 */
public class ChooseWorkerAttackTreasureDialog extends BaseDialog implements IChooseWorkerDialog, ChooseWorkerAdapter.ChoseItemClickListener, FightDialog.FightListener {
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_treasure_grade)
    TextView tvTreasureGrade;
    @BindView(R.id.re_treasure_container_chips)
    RelativeLayout reTreasureContainerChips;
    @BindView(R.id.tv_enemy_worker)
    TextView tvEnemyWorker;
    @BindView(R.id.re_protect_place)
    RelativeLayout reProtectPlace;
    @BindView(R.id.recycler_worker_animal)
    RecyclerView recyclerViewAnimal;
    @BindView(R.id.recycler_worker_enemy)
    RecyclerView recyclerWorkerEnemy;
    @BindView(R.id.tv_attack_total)
    TextView tvAttackTotal;
    @BindView(R.id.iv_treasure_logo)
    ImageView ivTreasureLogo;
    @BindView(R.id.iv_treasure_no_attach_animal)
    ImageView ivWorkerNoAttachAnimal;
    @BindView(R.id.recycler_worker_mine)
    RecyclerView recyclerWorkerMine;
    @BindView(R.id.btn_harvest_treasure)
    TreasureTextView btnHarvestTreasure;
    @BindView(R.id.btn_refresh_treasure)
    TreasureTextView btnRefreshTreasure;
    private TreasureBean treasureBean;
    private Context context;
    private int source;
    private Handler handler = new Handler();
    private ChooseWorkerDialogPresenter presenter;
    private List<WorkerBean> chooseWorkerList = new ArrayList<>();
    private ChooseWorkerAdapter chooseWorkerAdapter;
    private Map<Integer, Boolean> choseMap = new HashMap<>();
    private EnemyWorkerAdapter enemyAdapter;
    private List<WorkerBean> enemyList = new ArrayList<>();
    private FightDialog fightDialog;
    private LoadingProgressDialog loadingProgressDialog;
    private TreasureAnimalAdapter animalAdapter;

    private CountDownTimer timer = new CountDownTimer(2000, 100) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnRefreshTreasure.setEnabled(false);
            btnRefreshTreasure.setText(context.getString(R.string.refresh_treasure) + "(" + (millisUntilFinished / 1000) + "s)");
        }

        @Override
        public void onFinish() {
            btnRefreshTreasure.setEnabled(true);
            btnRefreshTreasure.setText(context.getString(R.string.refresh_treasure));
        }
    };

    public ChooseWorkerAttackTreasureDialog(Context context, TreasureBean treasureBean, int source) {
        super(context);
        this.context = context;
        this.treasureBean = treasureBean;
        this.source = source;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_treasure_choose_attack_worker;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        //对方的
        enemyAdapter = new EnemyWorkerAdapter(context, enemyList);
        recyclerWorkerEnemy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerWorkerEnemy.setAdapter(enemyAdapter);

        //无人宝库野兽
        animalAdapter = new TreasureAnimalAdapter(context, 1);
        recyclerViewAnimal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAnimal.setAdapter(animalAdapter);

        //我的矿工
        chooseWorkerAdapter = new ChooseWorkerAdapter(context, chooseWorkerList);
        chooseWorkerAdapter.setChoseItemClickListener(this);
        recyclerWorkerMine.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerWorkerMine.setAdapter(chooseWorkerAdapter);

        presenter = new ChooseWorkerDialogPresenter(context, this);

        setTreasure();
    }

    private void setTreasure() {
        btnHarvestTreasure.setText(context.getString(R.string.treasure_race_treasure));
        btnRefreshTreasure.setVisibility(View.VISIBLE);
        btnRefreshTreasure.setEnabled(true);
        reProtectPlace.setVisibility(View.VISIBLE);

        choseMap.clear();
        presenter.getMineWorker(treasureBean.type, treasureBean.level);
        if (source == 1) {
            btnRefreshTreasure.setVisibility(View.GONE);
        } else {
            btnRefreshTreasure.setVisibility(View.VISIBLE);
        }

        tvTreasureGrade.setText(NumberFormatUtil.formatInteger(treasureBean.level) + "级宝窟");
        if (treasureBean.type == 1) {//已经被占领
            tvEnemyWorker.setVisibility(View.VISIBLE);
            recyclerWorkerEnemy.setVisibility(View.VISIBLE);
            recyclerViewAnimal.setVisibility(View.GONE);
            presenter.getEnemyWorker(treasureBean.id);
            presenter.queryCanHarvestChips(treasureBean.id);
        } else {//没有被占领
            reTreasureContainerChips.removeAllViews();
            tvEnemyWorker.setVisibility(View.VISIBLE);
            tvEnemyWorker.setText("" + treasureBean.attribute);
            recyclerWorkerEnemy.setVisibility(View.INVISIBLE);
            recyclerViewAnimal.setVisibility(View.VISIBLE);
            animalAdapter.setTypeAnimal(treasureBean.level);
        }
        timer.cancel();
        ivWorkerNoAttachAnimal.setVisibility(View.VISIBLE);
        switch (treasureBean.level) {
            case 1:
                ivTreasureLogo.setImageResource(R.mipmap.icon_treasury_bg_one);
                if (treasureBean.type == 1) {
                    ivWorkerNoAttachAnimal.setVisibility(View.GONE);
                } else {
                    ivWorkerNoAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_one_animal);
                }
                break;
            case 2:
                ivTreasureLogo.setImageResource(R.mipmap.icon_treasury_bg_two);
                if (treasureBean.type == 1) {
                    ivWorkerNoAttachAnimal.setVisibility(View.GONE);
                } else {
                    ivWorkerNoAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_two_animal);
                }
                break;
            case 3:
                ivTreasureLogo.setImageResource(R.mipmap.icon_treasury_bg_three);
                if (treasureBean.type == 1) {
                    ivWorkerNoAttachAnimal.setVisibility(View.GONE);
                } else {
                    ivWorkerNoAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_three_animal);
                }
                break;
            case 4:
                ivTreasureLogo.setImageResource(R.mipmap.icon_treasury_bg_four);
                if (treasureBean.type == 1) {
                    ivWorkerNoAttachAnimal.setVisibility(View.GONE);
                } else {
                    ivWorkerNoAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_four_animal);
                }
                break;
            case 5:
                ivTreasureLogo.setImageResource(R.mipmap.icon_treasury_bg_five);
                if (treasureBean.type == 1) {
                    ivWorkerNoAttachAnimal.setVisibility(View.GONE);
                } else {
                    ivWorkerNoAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_five_animal);
                }
                break;
            default:
                ivTreasureLogo.setImageResource(R.mipmap.icon_treasury_bg_one);
                break;
        }
        btnRefreshTreasure.setText(context.getString(R.string.refresh_treasure) + "(3s)");
        timer.start();

        tvAttackTotal.setText("矿工攻击力:0");
        tvAttackTotal.setVisibility(View.VISIBLE);

        System.gc();
    }

    private void setMyTreasure() {
        choseMap.clear();
        presenter.getMineWorker(treasureBean.type, treasureBean.level);

        tvTreasureGrade.setText(context.getString(R.string.treasure_send_worker));
        btnHarvestTreasure.setText(context.getString(R.string.treasure_extract_treasure));
        tvEnemyWorker.setText("" + treasureBean.attribute);

        reTreasureContainerChips.removeAllViews();
        tvEnemyWorker.setVisibility(View.VISIBLE);
        recyclerWorkerEnemy.setVisibility(View.INVISIBLE);
//        ivWorkerAnimal.setVisibility(View.VISIBLE);
        ivWorkerNoAttachAnimal.setVisibility(View.GONE);
        recyclerViewAnimal.setVisibility(View.VISIBLE);

        reProtectPlace.setVisibility(View.GONE);
        btnRefreshTreasure.setText("放弃开采");

        tvAttackTotal.setVisibility(View.VISIBLE);
        tvAttackTotal.setText("宝窟防御值:0");

        System.gc();
    }

    @OnClick({R.id.iv_close, R.id.btn_harvest_treasure, R.id.btn_refresh_treasure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                System.gc();
                dismiss();
                break;
            case R.id.btn_harvest_treasure:
                showLoading();
                if (btnHarvestTreasure.getText().toString().equalsIgnoreCase(context.getString(R.string.treasure_extract_treasure))) {
                    changeLoadingDes("开采宝库中...");
                    presenter.extractTreasure(treasureBean.id, chooseWorkerList, choseMap);
                } else if (btnHarvestTreasure.getText().toString().equalsIgnoreCase(context.getString(R.string.treasure_race_treasure))) {
                    changeLoadingDes("占领宝库中...");
                    presenter.harvestTreasure(chooseWorkerList, choseMap, treasureBean);
                }
                break;
            case R.id.btn_refresh_treasure:
                showLoading();
                if (btnHarvestTreasure.getText().toString().equalsIgnoreCase(context.getString(R.string.treasure_extract_treasure))) {
                    if (btnRefreshTreasure.getText().toString().equalsIgnoreCase("放弃开采")) {
                        presenter.deleteTreasure(treasureBean.id);
                        dismissedLoading();
                        dismiss();
                    } else {
                        presenter.deleteTreasure(treasureBean.id);
                        presenter.refreshTreasure();
                    }
                } else if (btnHarvestTreasure.getText().toString().equalsIgnoreCase(context.getString(R.string.treasure_race_treasure))) {
                    presenter.refreshTreasure();
                }
                break;
        }
    }

    @Override
    public void showMineWorker(List<WorkerBean> workerBeanList) {
        chooseWorkerList.clear();

        if (null != workerBeanList && workerBeanList.size() > 0) {
            chooseWorkerList.addAll(workerBeanList);
            chooseWorkerAdapter.setWorkerAdapter(workerBeanList);
        } else {
            chooseWorkerAdapter.setWorkerAdapter(null);
        }
        chooseWorkerAdapter.setChoseCount(0);
    }

    @Override
    public void showEnemyWorker(List<WorkerBean> data) {
        enemyList.clear();

        enemyList.addAll(data);
        enemyAdapter.setWorkerAdapter(data);
        int countAttribute = 0;
        if (null != enemyList && enemyList.size() > 0) {
            for (int i = 0; i < enemyList.size(); i++) {
                WorkerBean workerBean = enemyList.get(i);
                countAttribute += (1 + workerBean.starLevel * 0.2) * workerBean.attribute;
            }
        }
        tvEnemyWorker.setText("" + countAttribute);

    }

    @Override
    public void showChips(int chipsNumber) {
        if (chipsNumber > 0) {
            reTreasureContainerChips.removeAllViews();
            TextView textView = new TextView(getContext());
            textView.setBackgroundResource(R.mipmap.icon_debris);
            textView.setText("" + chipsNumber + "");
            textView.setTextSize(9);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(context.getResources().getColor(R.color.white));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(getContext(), 37), ScreenUtil.dp2px(getContext(), 43));
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            textView.setLayoutParams(layoutParams);
            reTreasureContainerChips.addView(textView);
        } else {
            reTreasureContainerChips.removeAllViews();
        }
    }

    @Override
    public void showMessage(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void raceState(boolean flag, TreasureBean treasure, int harvest) {
        dismissedLoading();
        if (flag) {
            this.treasureBean = treasure;
        }
        fightDialog = new FightDialog(context, flag, treasure, harvest);
        fightDialog.setFightListener(this);
        fightDialog.show();
    }

    @Override
    public void dismissView() {
        System.gc();
        EventBus.getDefault().post(new RefreshTreasureEvent(1));
        dismiss();
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(context, false);
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
    public void showMessageToast(String string) {
        showMessage(string);
    }

    @Override
    public void refreshTreasure(TreasureBean treasure) {
        dismissedLoading();
        treasureBean = treasure;
        setTreasure();
        showMessage("刷新成功");
    }

    @Override
    public void choseMap(Map<Integer, Boolean> map) {
        int countAttack = 0;
        choseMap.clear();
        choseMap.putAll(map);
        if (map.size() > 0) {
            for (int i = 0; i < map.size(); i++) {
                if (map.get(i) && chooseWorkerList.size() > i) {
                    countAttack += (chooseWorkerList.get(i).starLevel * 0.2 * chooseWorkerList.get(i).maxAttribute) + chooseWorkerList.get(i).attribute;
                }
            }
        }
        if (btnHarvestTreasure.getText().toString().equalsIgnoreCase(context.getString(R.string.treasure_extract_treasure))) {
            tvAttackTotal.setText("宝窟防御值:" + countAttack);
        } else if (btnHarvestTreasure.getText().toString().equalsIgnoreCase(context.getString(R.string.treasure_race_treasure))) {
            tvAttackTotal.setText("矿工攻击力:" + countAttack);
        }
    }

    @Override
    public void sendState(boolean flag) {
        if (flag) {
            setMyTreasure();
            showMessage(treasureBean.message);
        } else {
            showMessage("攻占失败");
            btnRefreshTreasure.setEnabled(false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.refreshTreasure();
                }
            }, 1000);
        }
    }
}