package com.leyou.game.widget.dialog.treasury;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.leyou.game.R;
import com.leyou.game.adapter.treasure.ChooseWorkerAdapter;
import com.leyou.game.adapter.treasure.ChooseWorkerCanUseAdapter;
import com.leyou.game.base.BaseDialog;
import com.leyou.game.bean.treasure.TreasureBean;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.event.RefreshTreasureEvent;
import com.leyou.game.ipresenter.mine.IModifyWorkerDialog;
import com.leyou.game.presenter.mine.ModifyWorkerDialogPresenter;
import com.leyou.game.util.ToastUtils;
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
 * Description : 刷新宝窟 攻占宝窟
 *
 * @author : rocky
 * @Create Time : 2017/6/20 上午10:06
 * @Modified Time : 2017/6/20 上午10:06
 */
public class ChooseWorkerModifyTreasureDialog extends BaseDialog implements IModifyWorkerDialog, ChooseWorkerAdapter.ChoseItemClickListener, ChooseWorkerCanUseAdapter.ChoseUseItemClickListener {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_attack_title_treasure_grade)
    TreasureTextView tvAttackTitleTreasureGrade;
    @BindView(R.id.tv_attack_title_treasure_attribute)
    TreasureTextView tvAttackTitleTreasureAttribute;
    @BindView(R.id.recycler_worker_mine)
    RecyclerView recyclerWorkerMine;
    @BindView(R.id.recycler_worker_mine_can_use)
    RecyclerView recyclerWorkerMineCanUse;
    @BindView(R.id.btn_modify_worker)
    TreasureTextView btnModifyWorker;
    @BindView(R.id.btn_abandon_worker)
    TreasureTextView btnAbandonWorker;
    private Context context;
    private TreasureBean treasureBean;

    private List<WorkerBean> chooseUseList = new ArrayList<>();
    private Map<Integer, Boolean> choseUseMap = new HashMap<>();
    private ChooseWorkerCanUseAdapter chooseCanUseAdapter;

    private List<WorkerBean> chooseWorkerList = new ArrayList<>();
    private Map<Integer, Boolean> choseMap = new HashMap<>();
    private ChooseWorkerAdapter chooseWorkerAdapter;
    private ModifyWorkerDialogPresenter presenter;

    private int selectCount = 0;
    private List<WorkerBean> selectWorker = new ArrayList<>();

    public ChooseWorkerModifyTreasureDialog(Context context, TreasureBean treasureBean) {
        super(context);
        this.context = context;
        this.treasureBean = treasureBean;
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dialog_treasure_modify_my_worker;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        presenter = new ModifyWorkerDialogPresenter(context, this, treasureBean.id);

        presenter.getMineCanUseWorker(treasureBean.type, treasureBean.level);

        switch (treasureBean.level) {
            case 1:
                tvAttackTitleTreasureGrade.setText("一级宝窟");
                break;
            case 2:
                tvAttackTitleTreasureGrade.setText("二级宝窟");
                break;
            case 3:
                tvAttackTitleTreasureGrade.setText("三级宝窟");
                break;
            case 4:
                tvAttackTitleTreasureGrade.setText("四级宝窟");
                break;
            case 5:
                tvAttackTitleTreasureGrade.setText("五级宝窟");
                break;
        }
        tvAttackTitleTreasureAttribute.setText(treasureBean.attribute + "");

        //我的矿工
        chooseWorkerAdapter = new ChooseWorkerAdapter(context, chooseWorkerList, true);
        chooseWorkerAdapter.setChoseItemClickListener(this);
        recyclerWorkerMine.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerWorkerMine.setAdapter(chooseWorkerAdapter);

        //我的可雇佣矿工
        chooseCanUseAdapter = new ChooseWorkerCanUseAdapter(context, chooseUseList);
        chooseCanUseAdapter.setChoseItemClickListener(this);
        recyclerWorkerMineCanUse.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerWorkerMineCanUse.setAdapter(chooseCanUseAdapter);

    }

    @OnClick({R.id.iv_close, R.id.btn_modify_worker, R.id.btn_abandon_worker})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_modify_worker:
                if (null != selectWorker && selectWorker.size() > 0) {
                    presenter.modifyWorker(treasureBean.id, selectWorker);
                } else {
                    showMessageToast("选择要更改的矿工");
                }
                break;
            case R.id.btn_abandon_worker:
                presenter.deleteTreasure();
                break;
        }
    }

    @Override
    public void showMessageToast(String msg) {
        ToastUtils.showToastShort(msg);
    }

    @Override
    public void showMineWorker(List<WorkerBean> data) {
        chooseWorkerList.clear();
        choseMap.clear();

        chooseWorkerList.addAll(data);
        chooseWorkerAdapter.setWorkerAdapter(data);
        if (null != chooseWorkerList && chooseWorkerList.size() > 0) {
            for (int i = 0; i < chooseWorkerList.size(); i++) {
                choseMap.put(i, true);
                selectCount++;
            }
        }
        chooseWorkerAdapter.setChoseCount(selectCount);
        chooseCanUseAdapter.setSelectNum(5 - selectCount);
    }

    @Override
    public void showMineCanUseWorker(List<WorkerBean> data) {
        chooseUseList.clear();
        choseUseMap.clear();

        chooseUseList.addAll(data);
        chooseCanUseAdapter.setWorkerAdapter(data);
    }

    @Override
    public void deleteTreasure(boolean flag) {
        if (flag) {
            EventBus.getDefault().post(new RefreshTreasureEvent(1));
            dismiss();
        }
    }

    @Override
    public void modifyTreasure(boolean flag) {
        if (flag) {
            EventBus.getDefault().post(new RefreshTreasureEvent(1));
            dismiss();
        }
    }

    @Override
    public void choseMap(Map<Integer, Boolean> map) {
        choseMap.clear();
        choseMap.putAll(map);
        calculateWorker();
    }

    @Override
    public void choseUseMap(Map<Integer, Boolean> map) {
        choseUseMap.clear();
        choseUseMap.putAll(map);
        calculateWorker();
    }

    private void calculateWorker() {
        selectCount = 0;
        selectWorker.clear();
        if (null != choseMap && choseMap.size() > 0) {
            for (int i = 0; i < choseMap.size(); i++) {
                if (choseMap.get(i)) {
                    selectCount++;
                    selectWorker.add(chooseWorkerList.get(i));
                }
            }
        }
        int currentNum = selectCount;
        if (null != choseUseMap && choseUseMap.size() > 0) {
            for (int i = 0; i < choseUseMap.size(); i++) {
                if (choseUseMap.get(i)) {
                    selectCount++;
                    selectWorker.add(chooseUseList.get(i));
                }
            }
        }
        chooseWorkerAdapter.setSelectNum(5 - selectCount + chooseWorkerAdapter.getChoseCount());
        chooseCanUseAdapter.setSelectNum(5 - currentNum);
        calculateAttribute();
    }

    private void calculateAttribute() {
        int attribute = 0;
        if (null != selectWorker && selectWorker.size() > 0) {
            for (int i = 0; i < selectWorker.size(); i++) {
                attribute += (selectWorker.get(i).starLevel * 0.2 * selectWorker.get(i).maxAttribute) + selectWorker.get(i).attribute;
            }
        }
        tvAttackTitleTreasureAttribute.setText(String.valueOf(attribute));
    }
}
