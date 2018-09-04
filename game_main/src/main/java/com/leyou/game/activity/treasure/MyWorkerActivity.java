package com.leyou.game.activity.treasure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.treasure.PropUseToWorkerAdapter;
import com.leyou.game.adapter.treasure.TreasureMineWorkerAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.event.WorkerPlaceEvent;
import com.leyou.game.ipresenter.treasure.IMyWorkerActivity;
import com.leyou.game.presenter.treasure.MyWorkerPresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.ConvertDiamondDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

/**
 * Description : 我的矿工页面
 *
 * @author : rocky
 * @Create Time : 2017/8/25 下午6:11
 * @Modified By: rocky
 * @Modified Time : 2017/8/25 下午6:11
 */
public class MyWorkerActivity extends BaseActivity implements IMyWorkerActivity, SwipeRefreshLayout.OnRefreshListener, CustomItemClickListener, PropUseToWorkerAdapter.onSelectorProp {

    @BindView(R.id.iv_worker_back)
    ImageView ivWorkerBack;
    @BindView(R.id.iv_treasure_worker_header)
    SimpleDraweeView ivTreasureWorkerHeader;
    @BindView(R.id.tv_worker_chips_tips)
    TextView tvWorkerChipsTips;
    @BindView(R.id.tv_worker_chips_count)
    TextView tvWorkerChipsCount;
    @BindView(R.id.btn_chips_convert)
    Button btnChipsConvert;
    @BindView(R.id.btn_dismissed_worker)
    Button btnDismissedWorker;
    @BindView(R.id.btn_composed_worker)
    Button btnComposedWorker;
    @BindView(R.id.btn_upgrade_worker)
    Button btnUpgradeWorker;
    @BindView(R.id.tv_treasure_user_worker_number)
    TextView tvTreasureUserWorkerNumber;
    @BindView(R.id.recycler_mine_worker)
    RecyclerView recyclerMineWorker;
    @BindView(R.id.swipeRefresh_mine_worker)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.re_popup_bg)
    RelativeLayout rePopupBg;
    @BindView(R.id.gif_diamond_change)
    GifImageView gifDiamondChange;
    private List<WorkerBean> mineWorkerList = new ArrayList<>();
    private TreasureMineWorkerAdapter mineWorkerAdapter;
    private MyWorkerPresenter presenter;
    private int chipsNumber;

    private View rootWorkerPopup;
    private PopupWindow popupWindowWorker;
    private LinearLayout llMyPropClose;
    private ImageView ivWorker, ivWorkerStar;
    private RoundCornerProgressBar progressBarPhy, progressBarPower;
    private TextView tvPower, tvPhy;
    private GifImageView gifStar;
    private Button btnUseProp;
    private RecyclerView recyclerViewProp;
    private List<PropBean> propList = new ArrayList<>();
    private PropUseToWorkerAdapter propAdapter;
    private WorkerBean workerProp;
    private PropBean propBean;
    private int starLevel;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWorker(WorkerPlaceEvent event) {
        if (event.getType() == 1) {
            onRefresh();
        }
    }

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_my_worker;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);

        //我的矿工
        int SCREEN_WIDTH = ScreenUtil.getInstance(this).getScreenWidth();
        int number;
        if (SCREEN_WIDTH > 540) {
            number = Constants.TREASURE_FIVE_720;
        } else {
            number = Constants.TREASURE_FIVE_480;
        }
        mineWorkerAdapter = new TreasureMineWorkerAdapter(this, mineWorkerList, this);
        recyclerMineWorker.setLayoutManager(new GridLayoutManager(this, number));
        recyclerMineWorker.setAdapter(mineWorkerAdapter);

        //init sex change popUpWindow
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_popup_worker_prop_selector, null);
        rootWorkerPopup = inflate.findViewById(R.id.ll_worker_root_popup);

        llMyPropClose = ButterKnife.findById(inflate, R.id.ll_worker_back_popup);
        llMyPropClose.setOnClickListener(this);

        ivWorker = ButterKnife.findById(inflate, R.id.iv_worker_img);
        ivWorkerStar = ButterKnife.findById(inflate, R.id.iv_worker_star);
        tvPhy = ButterKnife.findById(inflate, R.id.tv_worker_phy);
        progressBarPhy = ButterKnife.findById(inflate, R.id.round_pr_phy);
        tvPower = ButterKnife.findById(inflate, R.id.tv_worker_power);
        progressBarPower = ButterKnife.findById(inflate, R.id.round_pr_power);
        gifStar = ButterKnife.findById(inflate, R.id.gif_worker_upgrade_star);

        btnUseProp = ButterKnife.findById(inflate, R.id.btn_use_prop_to_worker);
        btnUseProp.setOnClickListener(this);
        recyclerViewProp = ButterKnife.findById(inflate, R.id.recycler_my_prop_choose);
        propAdapter = new PropUseToWorkerAdapter(this, propList, this);
        recyclerViewProp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProp.setAdapter(propAdapter);

        popupWindowWorker = new PopupWindow(inflate, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rootWorkerPopup.setFocusable(true);
        rootWorkerPopup.setFocusableInTouchMode(true);
        popupWindowWorker.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//防止被底部虚拟键挡住
        popupWindowWorker.setAnimationStyle(R.style.popWindow_anim_style);
    }

    @Override
    public void initPresenter() {
        presenter = new MyWorkerPresenter(this, this);
    }

    @OnClick({R.id.iv_worker_back, R.id.tv_worker_shop, R.id.btn_chips_convert, R.id.btn_dismissed_worker, R.id.btn_composed_worker, R.id.btn_upgrade_worker, R.id.re_popup_bg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_worker_back:
                finishCurrentActivity();
                break;
            case R.id.tv_worker_shop:
                startActivity(new Intent(this, PropActivity.class));
                break;
            case R.id.btn_chips_convert:
                ConvertDiamondDialog workerDialog = new ConvertDiamondDialog(this, getString(R.string.treasure_diamond_convert), chipsNumber);
                workerDialog.setWorkerListener(new ConvertDiamondDialog.WorkerListener() {
                    @Override
                    public void confirm(int number) {
                        presenter.convertChips(number);
                    }
                });
                workerDialog.show();
                break;
            case R.id.btn_dismissed_worker:
                startActivity(new Intent(this, WorkerDismissActivity.class));
                break;
            case R.id.btn_composed_worker:
                startActivity(new Intent(this, WorkerComposeActivity.class));
                break;
            case R.id.btn_upgrade_worker:
                startActivity(new Intent(this, WorkerUpgradeActivity.class));
                break;
            case R.id.re_popup_bg:
                hiddenWorkerPopUpWindow();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_worker_back_popup:
                hiddenWorkerPopUpWindow();
                break;
            case R.id.btn_use_prop_to_worker:
                if (null != workerProp && null != propBean) {
                    if (propBean.itemName.equalsIgnoreCase("升星符")) {
                        if (workerProp.starLevel < 5) {
                            starLevel = workerProp.starLevel;
                            presenter.useProp(workerProp.id, propBean.id, propBean.type);
                        } else {
                            ToastUtils.showToastShort("已经是满级了，试试其他道具吧！");
                        }
                    } else if (propBean.itemName.contains("面包")) {
                        float phyProgress = (workerProp.phyPower / (100.0f + workerProp.starLevel * 10)) * 100;
                        if (phyProgress < 100) {
                            presenter.useProp(workerProp.id, propBean.id, propBean.type);
                        } else {
                            ToastUtils.showToastShort("体力充沛，精神满满！");
                        }
                    }
                } else {
                    showMessageToast("选择要使用的道具");
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        showWorkerInfo(mineWorkerList.get(position));
        presenter.getMyProp();
    }

    @Override
    public void selectorProp(PropBean propBean) {
        this.propBean = propBean;
    }

    @Override
    public void setMineWorkerData(List<WorkerBean> workerBeanList) {
        swipeRefresh.setRefreshing(false);
        mineWorkerList.clear();
        if (null != workerBeanList && workerBeanList.size() > 0) {
            mineWorkerList.addAll(workerBeanList);
        }
        mineWorkerAdapter.setWorkerAdapter(workerBeanList);
    }

    @Override
    public void showConvertDiamondResult(boolean result, final int diamondCount) {
        if (result) {
            UserData.getInstance().setDiamonds(UserData.getInstance().getDiamonds() + diamondCount);
            gifDiamondChange.setVisibility(View.VISIBLE);
            gifDiamondChange.setImageResource(R.mipmap.icon_treasure_diamond_change);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gifDiamondChange.setVisibility(View.GONE);
                    showMessageToast("成功兑换" + diamondCount + "个钻石，请移至我的页面查看钻石数量");
                }
            }, 2500);
        } else {
            showMessageToast("碎钻兑换失败，请重新提交申请");
        }
    }

    @Override
    public void showWorkerUpStar(boolean result, String msg) {
        gifStar.setVisibility(View.VISIBLE);
        presenter.getMyProp();
        if (result) {
            ivWorkerStar.setVisibility(View.GONE);
            switch (starLevel) {
                case 0:
                    gifStar.setImageResource(R.mipmap.icon_worker_up_star_one);
                    break;
                case 1:
                    gifStar.setImageResource(R.mipmap.icon_worker_up_star_two);
                    break;
                case 2:
                    gifStar.setImageResource(R.mipmap.icon_worker_up_star_three);
                    break;
                case 3:
                    gifStar.setImageResource(R.mipmap.icon_worker_up_star_four);
                    break;
                case 4:
                    gifStar.setImageResource(R.mipmap.icon_worker_up_star_five);
                    break;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gifStar.setVisibility(View.GONE);
                    hiddenWorkerPopUpWindow();
                    presenter.getMineWorker();
                }
            }, 2000);
        } else {
            gifStar.setImageResource(R.mipmap.icon_worker_up_star_failed);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gifStar.setVisibility(View.GONE);
                }
            }, 2000);
            showMessageToast(msg);
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
    public void showWorkerPopUpWindow() {
        propBean = null;
        rePopupBg.setVisibility(View.VISIBLE);
        propAdapter.resetSelectorPosition();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindowWorker.showAtLocation(rePopupBg, Gravity.BOTTOM, 0, 0);
            }
        }, 50);
    }

    @Override
    public void showWorkerInfo(WorkerBean workerBean) {
        workerProp = workerBean;
        Glide.with(this).load(workerBean.pictureUrl).error(R.mipmap.icon_default).into(ivWorker);
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
        tvPower.setText(workerBean.attribute + "");
        float progress = (workerBean.attribute * 1.0f / (workerBean.maxAttribute * 1.0f)) * 100;
        progressBarPower.setProgress(progress);
        tvPhy.setText(workerBean.phyPower + "");
        float phyProgress = (workerBean.phyPower / (100.0f + workerBean.starLevel * 10)) * 100;
        progressBarPhy.setProgress(phyProgress);
    }

    @Override
    public void showPropData(List<PropBean> data) {
        propList.clear();
        if (null != data) {
            propList.addAll(data);
        }
        propAdapter.setListAdapter(data);
    }

    @Override
    public void hiddenWorkerPopUpWindow() {
        popupWindowWorker.dismiss();
        rePopupBg.setVisibility(View.GONE);
    }

    @Override
    public void setTreasureChips(int chipsNumber) {
        this.chipsNumber = chipsNumber;
        tvWorkerChipsCount.setText(chipsNumber + "");
    }

    @Override
    public void setUserWorkerPlaceNumber(int holder, int holderMax) {
        tvTreasureUserWorkerNumber.setText("(" + holder + "/" + holderMax + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
        ivTreasureWorkerHeader.setImageURI(UserData.getInstance().getPictureUrl());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (popupWindowWorker.isShowing()) {
                hiddenWorkerPopUpWindow();
            } else {
                finishCurrentActivity();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        presenter.queryChips();
        presenter.getUserWorkerPlace();
        presenter.getMineWorker();
    }
}
