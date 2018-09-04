package com.leyou.game.fragment.treasure;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.adapter.treasure.WorkerComposeAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.UserData;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.event.WorkerPlaceEvent;
import com.leyou.game.ipresenter.treasure.IWorkerComposeFragment;
import com.leyou.game.presenter.treasure.WorkerComposePresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.treasury.WorkerModifyDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

/**
 * Description : 合成矿工页面
 *
 * @author : rocky
 * @Create Time : 2017/5/5 下午5:27
 * @Modified By: rocky
 * @Modified Time : 2017/5/5 下午5:27
 */
public class WorkerComposeFragment extends BaseFragment implements IWorkerComposeFragment, WorkerComposeAdapter.ChoseItemClickListener {
    private static final String TAG = "WorkerComposeFragment";
    private final static String TITLE = "title";
    private final static String TYPE_ID = "id";
    @BindView(R.id.iv_composed_worker_img1)
    ImageView ivComposedWorkerImg1;
    @BindView(R.id.iv_worker_star1)
    ImageView ivWorkerStar1;
    @BindView(R.id.tv_composed_worker_power1)
    TextView tvComposedWorkerPower1;
    @BindView(R.id.iv_composed_worker_img2)
    ImageView ivComposedWorkerImg2;
    @BindView(R.id.iv_worker_star2)
    ImageView ivWorkerStar2;
    @BindView(R.id.tv_composed_worker_power2)
    TextView tvComposedWorkerPower2;
    @BindView(R.id.btn_composed_worker_confirm)
    Button btnComposedWorkerConfirm;
    @BindView(R.id.recycler_composed_worker)
    RecyclerView recyclerComposedWorker;
    Unbinder unbinder;
    @BindView(R.id.round_pr_phy1)
    RoundCornerProgressBar roundPrPhy1;
    @BindView(R.id.tv_compose_worker_phy1)
    TextView tvComposeWorkerPhy1;
    @BindView(R.id.round_pr_power1)
    RoundCornerProgressBar roundPrPower1;
    @BindView(R.id.tv_compose_worker_power1)
    TextView tvComposeWorkerPower1;
    @BindView(R.id.ll_round_compose1)
    LinearLayout llRoundCompose1;
    @BindView(R.id.re_composed_worker1)
    RelativeLayout reComposedWorker1;
    @BindView(R.id.round_pr_phy2)
    RoundCornerProgressBar roundPrPhy2;
    @BindView(R.id.tv_compose_worker_phy2)
    TextView tvComposeWorkerPhy2;
    @BindView(R.id.round_pr_power2)
    RoundCornerProgressBar roundPrPower2;
    @BindView(R.id.tv_compose_worker_power2)
    TextView tvComposeWorkerPower2;
    @BindView(R.id.ll_round_compose2)
    LinearLayout llRoundCompose2;
    @BindView(R.id.gif_compose_result)
    GifImageView gifState;
    private List<WorkerBean> list = new ArrayList<>();
    private Map<Integer, Boolean> map = new HashMap<>();
    private WorkerBean worker1 = new WorkerBean();
    private WorkerBean worker2 = new WorkerBean();
    private WorkerComposeAdapter adapter;
    private WorkerComposePresenter presenter;
    private LoadingProgressDialog loadingProgressDialog;
    private Handler handler = new Handler();
    private int currentType;
    private boolean canClick = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public static WorkerComposeFragment newInstance(String title, int typeId) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putInt(TYPE_ID, typeId);
        WorkerComposeFragment fragment = new WorkerComposeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_worker_compose;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

        //添加适配器
        adapter = new WorkerComposeAdapter(getContext(), list);
        adapter.setChoseItemClickListener(this);
        int SCREEN_WIDTH = ScreenUtil.getInstance(context).getScreenWidth();
        int number;
        if (SCREEN_WIDTH > 540) {
            number = Constants.TREASURE_FIVE_720;
        } else {
            number = Constants.TREASURE_FIVE_480;
        }
        recyclerComposedWorker.setLayoutManager(new GridLayoutManager(getContext(), number));
        recyclerComposedWorker.setAdapter(adapter);
    }

    @Override
    protected void initPresenter() {
        presenter = new WorkerComposePresenter(context, this, getArguments().getInt(TYPE_ID));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }

    @OnClick({R.id.btn_composed_worker_confirm, R.id.gif_compose_result})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_composed_worker_confirm:
                String des = "矿工合成需要消耗一定数量的钻石，如果合成失败会死掉一个矿工，确定要合成吗？";
                if (null != worker1 && null != worker2) {
                    switch (worker1.typeId) {
                        case 0:
                            currentType = 0;
                            des = "合成橙色矿工需要消耗10钻石，如果合成失败会死掉一个矿工，确定要合成吗？";
                            break;
                        case 1:
                            currentType = 1;
                            des = "合成红色矿工需要消耗20钻石，如果合成失败会死掉一个矿工，确定要合成吗？";
                            break;
                        case 2:
                            currentType = 2;
                            des = "合成蓝色矿工需要消耗50钻石，如果合成失败会死掉一个矿工，确定要合成吗？";
                            break;
                        case 3:
                            currentType = 3;
                            des = "合成紫色矿工需要消耗200钻石，如果合成失败会死掉一个矿工，确定要合成吗？";
                            break;
                    }
                    WorkerModifyDialog workerDialog = new WorkerModifyDialog(getContext(), getString(R.string.treasure_composed_worker), des);
                    workerDialog.setWorkerListener(new WorkerModifyDialog.WorkerListener() {
                        @Override
                        public void confirm() {
                            showLoading();
                            canClick = false;
                            adapter.setCanClick(canClick);
                            presenter.composeWorker(worker1, worker2);
                        }
                    });
                    int diamond = currentType == 0 ? 10 : currentType == 1 ? 20 : currentType == 2 ? 50 : currentType == 3 ? 200 : 0;
                    if (diamond <= UserData.getInstance().getDiamonds()) {
                        workerDialog.show();
                    } else {
                        showMessageToast("当前钻石余额不足！");
                    }

                } else {
                    showMessageToast("请选择两个要合成的矿工");
                }
                break;
            case R.id.gif_compose_result:
                canClick = true;
                adapter.setCanClick(canClick);
                gifState.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void showMineWorkerData(List<WorkerBean> beanList) {
        list.clear();
        map.clear();
        if (null != beanList && beanList.size() > 0) {
            list.addAll(beanList);
        }
        adapter.setWorkerAdapter(beanList);
        worker1 = null;
        worker2 = null;
        showComposeWorkerInfo();
    }

    @Override
    public void showComposeWorkerInfo() {
        if (null != worker1) {
            llRoundCompose1.setVisibility(View.VISIBLE);
            tvComposedWorkerPower1.setVisibility(View.GONE);

            Glide.with(this).load(worker1.pictureUrl).error(R.mipmap.icon_default).into(ivComposedWorkerImg1);

            tvComposeWorkerPower1.setText("" + worker1.attribute);
            float progress = (worker1.attribute * 1.0f / (worker1.maxAttribute * 1.0f)) * 100;
            roundPrPower1.setProgress(progress);

            tvComposeWorkerPhy1.setText(worker1.phyPower + "");
            float phyProgress = (worker1.phyPower / (100.0f + worker1.starLevel * 10)) * 100;
            roundPrPhy1.setProgress(phyProgress);
            if (worker1.starLevel > 0) {
                ivWorkerStar1.setVisibility(View.VISIBLE);
                switch (worker1.starLevel) {
                    case 1:
                        ivWorkerStar1.setImageResource(R.mipmap.icon_star_one);
                        break;
                    case 2:
                        ivWorkerStar1.setImageResource(R.mipmap.icon_star_two);
                        break;
                    case 3:
                        ivWorkerStar1.setImageResource(R.mipmap.icon_star_three);
                        break;
                    case 4:
                        ivWorkerStar1.setImageResource(R.mipmap.icon_star_four);
                        break;
                    case 5:
                        ivWorkerStar1.setImageResource(R.mipmap.icon_star_five);
                        break;
                }
            } else {
                ivWorkerStar1.setVisibility(View.GONE);
            }
        } else {
            llRoundCompose1.setVisibility(View.GONE);
            tvComposedWorkerPower1.setVisibility(View.VISIBLE);
            Glide.with(this).load("").error(R.mipmap.icon_worker_default).into(ivComposedWorkerImg1);
            ivWorkerStar1.setVisibility(View.GONE);
        }
        if (null != worker2) {
            tvComposedWorkerPower2.setVisibility(View.GONE);
            llRoundCompose2.setVisibility(View.VISIBLE);

            Glide.with(this).load(worker2.pictureUrl).error(R.mipmap.icon_default).into(ivComposedWorkerImg2);

            tvComposeWorkerPower2.setText("" + worker2.attribute);
            float progress = (worker2.attribute * 1.0f / (worker2.maxAttribute * 1.0f)) * 100;
            roundPrPower2.setProgress(progress);

            tvComposeWorkerPhy2.setText(worker2.phyPower + "");
            float phyProgress = (worker2.phyPower / (100.0f + worker2.starLevel * 10)) * 100;
            roundPrPhy2.setProgress(phyProgress);
            if (worker2.starLevel > 0) {
                ivWorkerStar2.setVisibility(View.VISIBLE);
                switch (worker2.starLevel) {
                    case 1:
                        ivWorkerStar2.setImageResource(R.mipmap.icon_star_one);
                        break;
                    case 2:
                        ivWorkerStar2.setImageResource(R.mipmap.icon_star_two);
                        break;
                    case 3:
                        ivWorkerStar2.setImageResource(R.mipmap.icon_star_three);
                        break;
                    case 4:
                        ivWorkerStar2.setImageResource(R.mipmap.icon_star_four);
                        break;
                    case 5:
                        ivWorkerStar2.setImageResource(R.mipmap.icon_star_five);
                        break;
                }
            } else {
                ivWorkerStar2.setVisibility(View.GONE);
            }
        } else {
            tvComposedWorkerPower2.setVisibility(View.VISIBLE);
            llRoundCompose2.setVisibility(View.GONE);
            Glide.with(this).load("").error(R.mipmap.icon_worker_default).into(ivComposedWorkerImg2);
            ivWorkerStar2.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        loadingProgressDialog = new LoadingProgressDialog(getContext(), false);
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
    public void showComposeResult(final boolean state, final String des) {
        gifState.setVisibility(View.VISIBLE);
        switch (currentType) {
            case 0:
                if (state) {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_yellow_success);
                } else {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_failed);
                }
                break;
            case 1:
                if (state) {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_red_success);
                } else {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_failed);
                }
                break;
            case 2:
                if (state) {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_blue_success);
                } else {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_failed);
                }
                break;
            case 3:
                if (state) {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_purple_success);
                } else {
                    gifState.setImageResource(R.mipmap.icon_worker_compose_failed);
                }
                break;
        }
        if (state) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != gifState) {
                        gifState.setVisibility(View.GONE);
                        showMessageToast("合成成功,刷新我的矿工去查看吧！");
                    }
                    canClick = true;
                    if (null != adapter) {
                        adapter.setCanClick(canClick);
                    }
                }
            }, 3000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != gifState) {
                        gifState.setVisibility(View.GONE);
                    }
                    canClick = true;
                    if (null != adapter) {
                        adapter.setCanClick(canClick);
                    }
                }
            }, 2200);
        }

    }

    @Override
    public void showMessageToast(String msg) {
        ToastUtils.showToastShort(msg);
    }

    @Override
    public void sendEventComposedSuccess() {
        EventBus.getDefault().post(new WorkerPlaceEvent(1));
    }

    @Override
    public void choseMap(Map<Integer, Boolean> map1) {
        map.clear();
        map.putAll(map1);
        worker1 = null;
        worker2 = null;
        int j = 0;
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i)) {
                if (j == 0) {
                    worker1 = list.get(i);
                    j++;
                } else if (j == 1) {
                    worker2 = list.get(i);
                }
            }
        }
        showComposeWorkerInfo();
    }
}
