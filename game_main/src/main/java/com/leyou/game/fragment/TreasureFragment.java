package com.leyou.game.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.treasure.MyWorkerActivity;
import com.leyou.game.activity.treasure.TreasureActivity;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.treasure.TreasureAdapter;
import com.leyou.game.adapter.treasure.TreasureCanEmployWorkerAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.PropBean;
import com.leyou.game.bean.TreasureBean;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.WorkerBean;
import com.leyou.game.event.RefreshTreasureEvent;
import com.leyou.game.event.WorkerCanEmployEvent;
import com.leyou.game.ipresenter.treasure.ITreasureFragment;
import com.leyou.game.presenter.treasure.TreasureFragmentPresenter;
import com.leyou.game.util.DataUtil;
import com.leyou.game.util.SPUtil;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.CountDownTimeTextView;
import com.leyou.game.widget.GuideView;
import com.leyou.game.widget.dialog.ActivateTreasureDialog;
import com.leyou.game.widget.dialog.DeleteTreasureFragmentDialog;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.LogInDialog;
import com.leyou.game.widget.dialog.RefreshWorkerDialog;
import com.leyou.game.widget.dialog.WebViewDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

/**
 * Description : 宝窟游戏页面
 *
 * @author : rocky
 * @Create Time : 2017/3/29 下午4:21
 * @Modified By: rocky
 * @Modified Time : 2017/3/29 下午4:21
 */
public class TreasureFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ITreasureFragment, CustomItemClickListener {


    private static final String TAG = "TreasureFragment";
    @BindView(R.id.tv_treasure_protect_grade)
    TextView tvTreasureProtectGrade;
    @BindView(R.id.tv_treasure_play_explain)
    TextView tvTreasurePlayExplain;
    @BindView(R.id.tv_treasure_title_my_worker)
    TextView tvTreasureTitle;
    @BindView(R.id.iv_treasury_bg)
    ImageView ivTreasureBg;
    @BindView(R.id.iv_treasury_attach_animal)
    ImageView ivTreasureAttachAnimal;
    @BindView(R.id.iv_treasure_gif)
    GifImageView ivTreasureGif;
    @BindView(R.id.tv_treasure_grade)
    TextView tvTreasureGrade;
    @BindView(R.id.re_treasure_container_chips)
    RelativeLayout reTreasureContainerChips;
    @BindView(R.id.recycler_treasure_refresh_time)
    RecyclerView recyclerTreasureRefreshTime;
    @BindView(R.id.btn_treasure_refresh)
    Button btnTreasureRefresh;
    @BindView(R.id.btn_treasure_refresh_worker)
    Button btnTreasureRefreshWorker;
    @BindView(R.id.recycler_treasure_employ_worker)
    RecyclerView recyclerTreasureEmployWorker;
    @BindView(R.id.tv_treasure_employ_worker_extract_time)
    CountDownTimeTextView tvTreasureEmployWorkerExtractTime;
    @BindView(R.id.iv_treasure_user_header)
    SimpleDraweeView ivTreasureUserHeader;
    @BindView(R.id.re_treasure_user)
    RelativeLayout reTreasureUser;
    @BindView(R.id.swipeRefresh_treasure)
    SwipeRefreshLayout swipeRefresh;

    private List<TreasureBean> treasureBeanList = new ArrayList<>();
    private List<WorkerBean> canEmployList = new ArrayList<>();
    Unbinder unbinder;
    private Handler handler = new Handler();
    private TreasureCanEmployWorkerAdapter canEmployWorkerAdapter;
    private TreasureFragmentPresenter presenter;
    private LoadingProgressDialog loadingDialog;
    private TreasureAdapter treasureAdapter;
    private TreasureBean currentTreasureBean;
    private GuideView guideView;
    private GuideView guideViewWorker;
    private GuideView guideViewTreasure;
    private LogInDialog logInDialog;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshTreasure(RefreshTreasureEvent event) {
        if (event.getType() == 1) {
            currentTreasureBean = null;
            presenter.queryTreasures();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWorkerCanEmply(WorkerCanEmployEvent event) {
        if (event.getType() == 1) {
            presenter.getCanEmployWorker();
            presenter.getCanEmployRefreshTime();

            showActivateView();
        }
    }

    public TreasureFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && UserData.getInstance().isLogIn()) {
            UserLogIned();
            presenter.getCanEmployWorker();
            presenter.getCanEmployRefreshTime();
            ivTreasureUserHeader.setImageURI(UserData.getInstance().getPictureUrl());
        } else if (getUserVisibleHint() && !UserData.getInstance().isLogIn()) {
            userUnLogIn();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_treasure;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(getContext().getResources().getColor(R.color.theme_color));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.white, R.color.yellow, R.color.red_f2, R.color.purple);
        swipeRefresh.setOnRefreshListener(this);

        //可雇用矿工
        canEmployWorkerAdapter = new TreasureCanEmployWorkerAdapter(getContext(), canEmployList);
        recyclerTreasureEmployWorker.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerTreasureEmployWorker.setAdapter(canEmployWorkerAdapter);

        //我的宝库
        treasureAdapter = new TreasureAdapter(getContext(), treasureBeanList);
        treasureAdapter.setOnItemClickListener(this);
        recyclerTreasureRefreshTime.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerTreasureRefreshTime.setAdapter(treasureAdapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new TreasureFragmentPresenter(context, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        presenter.getCanEmployWorker();
        presenter.getCanEmployRefreshTime();
        presenter.queryTreasures();
        ivTreasureUserHeader.setImageURI(UserData.getInstance().getPictureUrl());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.tv_treasure_play_explain, R.id.re_treasure_user, R.id.btn_treasure_refresh, R.id.btn_treasure_refresh_worker})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_treasure_play_explain:
                WebViewDialog webViewDialog = new WebViewDialog(context, getString(R.string.treasure_play_explain), Constants.TREASURE_PLAY_EXPLAIN);
                webViewDialog.show();
                break;
            case R.id.re_treasure_user:
                context.startActivity(new Intent(getContext(), MyWorkerActivity.class));
                break;
            case R.id.btn_treasure_refresh:
                context.startActivity(new Intent(getContext(), TreasureActivity.class));
                break;
            case R.id.btn_treasure_refresh_worker:
                RefreshWorkerDialog refreshWorkerDialog = new RefreshWorkerDialog(getContext());
                refreshWorkerDialog.setRefreshWorkerListener(new RefreshWorkerDialog.RefreshWorkerListener() {
                    @Override
                    public void confirm() {
                        presenter.refreshWorker(10);
                    }
                });
                refreshWorkerDialog.show();
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
            treasureAdapter.setTreasureAdapter(treasureList);
            showCurrentTreasureInfo(treasureList.get(0));
        } else {
            treasureAdapter.setTreasureAdapter(treasureList);
            showCurrentTreasureInfo(null);
            ivTreasureAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_one_animal);
            ivTreasureAttachAnimal.setVisibility(View.VISIBLE);
        }
        dismissedLoadingView();
    }

    @Override
    public void showCurrentTreasureInfo(TreasureBean treasureBean) {
        if (null != treasureBean) {
            currentTreasureBean = treasureBean;
            ivTreasureAttachAnimal.setVisibility(View.GONE);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    presenter.queryCanHarvestChips(currentTreasureBean.id);
                }
            });
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.queryCanHarvestProp(currentTreasureBean.id);
                }
            }, 500);
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
            tvTreasureProtectGrade.setText(treasureBean.level + "");
        } else {
            currentTreasureBean = null;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    presenter.queryCanHarvestChips("");
                    presenter.queryCanHarvestProp("");
                }
            });
            ivTreasureBg.setImageResource(R.mipmap.icon_treasury_bg_one);
            ivTreasureAttachAnimal.setVisibility(View.VISIBLE);
            ivTreasureAttachAnimal.setImageResource(R.mipmap.icon_treasury_bg_one_animal);
            ivTreasureGif.setImageResource(R.mipmap.icon_treasury_bg_one);
            tvTreasureProtectGrade.setText("0");
        }
    }

    @Override
    public void deleteTreasure(TreasureBean treasureBean) {
        DeleteTreasureFragmentDialog deleteTreasureDialog = new DeleteTreasureFragmentDialog(getContext());
        deleteTreasureDialog.setDeleteTreasureFragmentListener(new DeleteTreasureFragmentDialog.DeleteTreasureFragmentListener() {
            @Override
            public void confirm() {
                presenter.deleteTreasure(currentTreasureBean.id);
            }
        });
        deleteTreasureDialog.show();
    }

    @Override
    public void showChipsView(final int chipsNumber) {
        if (chipsNumber > 0) {
            reTreasureContainerChips.removeAllViews();
            TextView textView = new TextView(getContext());
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
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(getContext(), 45), ScreenUtil.dp2px(getContext(), 45));
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
                TextView textView = new TextView(context);
                textView.setText(propList.get(i).itemName + "\nx" + propList.get(i).itemNum);
                textView.setBackgroundResource(R.mipmap.icon_debris);
                textView.setTextSize(9);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.white));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(context, 45), ScreenUtil.dp2px(context, 45));
                layoutParams.setMargins(ScreenUtil.dp2px(context, 45 * i), 0, 0, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                textView.setLayoutParams(layoutParams);
                reTreasureContainerChips.addView(textView);
            }
        }
    }

    @Override
    public void removeChipView() {
        reTreasureContainerChips.removeAllViews();
    }

    @Override
    public void setCanEmployWorkerData(List<WorkerBean> canEmployWorkerData) {
        presenter.getUserWorkerPlace();
        canEmployWorkerAdapter.setWorkerAdapter(canEmployWorkerData);
        dismissedLoadingView();
    }

    @Override
    public void setCanEmployCountDownTime(long countDownTime) {
        tvTreasureEmployWorkerExtractTime.cancel();
        long currentTimeMillis = countDownTime - System.currentTimeMillis();
        tvTreasureEmployWorkerExtractTime.setMillisInFuture(currentTimeMillis);
        tvTreasureEmployWorkerExtractTime.setCountdownInterval(1000);
        tvTreasureEmployWorkerExtractTime.setSimpleDataFormat(DataUtil.MS);
        tvTreasureEmployWorkerExtractTime.start();
        dismissedLoadingView();
    }

    @Override
    public void showLoadingView() {
        loadingDialog = new LoadingProgressDialog(context, false);
        loadingDialog.show();
    }

    @Override
    public void showLoadingViewText(String text) {
        if (null != loadingDialog) {
            loadingDialog.setLoadingText(text);
        }
    }

    @Override
    public void dismissedLoadingView() {
        swipeRefresh.setRefreshing(false);
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
    public void userUnLogIn() {
        logInDialog = new LogInDialog(context, false);
        logInDialog.show();
    }

    @Override
    public void UserLogIned() {
        if (null != logInDialog) {
            logInDialog.dismiss();
        }
    }

    @Override
    public void setUserWorkerPlaceNumber(int has, int total) {
        tvTreasureTitle.setText("我的矿工（" + has + "/" + total + ")");
    }

    @Override
    public void onRefresh() {
        presenter.getCanEmployWorker();
        presenter.getCanEmployRefreshTime();
        presenter.queryTreasures();
    }

    private void showActivateView() {
        if (SPUtil.getInt(context, SPUtil.TREASURE, "activateTimes") == 1) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivateTreasureDialog activateTreasureDialog = new ActivateTreasureDialog(context);
                    activateTreasureDialog.show();
                }
            }, 1500);
        }
    }

//    private void showGuideViewWorker() {
//        // 使用图片
//        final ImageView iv = new ImageView(context);
//        iv.setImageResource(R.mipmap.icon_arrow_worker_refresh);
//        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        iv.setLayoutParams(params);
//
//
//        guideView = GuideView.Builder
//                .newInstance(context)
//                .setTargetView(btnTreasureRefreshWorker)//设置目标
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.LEFT_TOP)
//                .setShape(GuideView.MyShape.RECTANGULAR)
//                .setOnclickExit(true)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideView.hide();
//                        presenter.refreshWorker(10);
//                        iv.setImageResource(R.mipmap.icon_arrow_worker_choose);
//                        guideViewWorker.show();
//                    }
//                })
//                .build();
//        boolean isDiamond = SPUtil.getBoolean(context, SPUtil.INDUCE, "treasure_employ_worker");
//        boolean isNewPerson = SPUtil.getBoolean(context, SPUtil.INDUCE, "isNewPerson");
//        if (!isDiamond && isNewPerson) {
//            SPUtil.setBoolean(context, SPUtil.INDUCE, "treasure_employ_worker", true);
//            guideView.show();
//        }
//        guideViewWorker = GuideView.Builder
//                .newInstance(context)
//                .setTargetView(recyclerTreasureEmployWorker)
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.TOP)
//                .setShape(GuideView.MyShape.SQUARE)
//                .setRadius(0)
//                .setOnclickExit(true)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewWorker.hide();
//                        EmployWorkerDialog employWorkerDialog = new EmployWorkerDialog(context, canEmployList.get(0));
//                        employWorkerDialog.show();
//                    }
//                })
//                .build();
//    }

//    private void showGuideViewTreasure() {
//        // 使用图片
//        final ImageView iv = new ImageView(context);
//        iv.setImageResource(R.mipmap.icon_arrow_treasure_into);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ScreenUtil.px2dp(context, 333), ScreenUtil.px2dp(context, 293));
//        iv.setLayoutParams(params);
//
//
//        guideViewTreasure = GuideView.Builder
//                .newInstance(context)
//                .setTargetView(btnTreasureRefresh)//设置目标
//                .setCustomGuideView(iv)
//                .setDirction(GuideView.Direction.LEFT_BOTTOM)
//                .setShape(GuideView.MyShape.RECTANGULAR)
//                .setOnclickExit(true)
//                .setBgColor(getResources().getColor(R.color.transparent))
//                .setOnclickListener(new GuideView.OnClickCallback() {
//                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                    @Override
//                    public void onClickedGuideView() {
//                        guideViewTreasure.hide();
//                        startActivity(new Intent(context, TreasureActivity.class));
//                    }
//                })
//                .build();
//        boolean isDiamond = SPUtil.getBoolean(context, SPUtil.INDUCE, "treasure_page");
//        if (!isDiamond) {
//            SPUtil.setBoolean(context, SPUtil.INDUCE, "treasure_page", true);
//            guideViewTreasure.show();
//        }
//    }
}
