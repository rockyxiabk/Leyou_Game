package com.leyou.game.fragment.treasure;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leyou.game.Constants;
import com.leyou.game.R;
import com.leyou.game.activity.treasure.TreasureActivity;
import com.leyou.game.adapter.treasure.ChooseWorkerUsePropAdapter;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.treasure.MyPropAdapter;
import com.leyou.game.base.BaseFragment;
import com.leyou.game.bean.treasure.PropBean;
import com.leyou.game.bean.treasure.TreasureBean;
import com.leyou.game.bean.treasure.WorkerBean;
import com.leyou.game.event.PropShopEvent;
import com.leyou.game.ipresenter.treasure.IPropFragment;
import com.leyou.game.presenter.treasure.MyPropFragmentPresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.dialog.treasury.TextViewDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description : 我的道具页面
 *
 * @author : rocky
 * @Create Time : 2017/6/30 上午10:50
 * @Modified By: rocky
 * @Modified Time : 2017/6/30 上午10:50
 */
public class MyPropFragment extends BaseFragment implements IPropFragment, SwipeRefreshLayout.OnRefreshListener, CustomItemClickListener, ChooseWorkerUsePropAdapter.ChoseItemClickListener {

    private static final String TAG = "MyPropFragment";
    @BindView(R.id.recycler_my_prop)
    RecyclerView recycler;
    @BindView(R.id.swipeRefresh_my_prop)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.re_load_try)
    RelativeLayout reLoadNoData;
    @BindView(R.id.re_popup_bg)
    RelativeLayout rePopupBg;
    Unbinder unbinder;
    private MyPropFragmentPresenter presenter;
    private List<PropBean> list = new ArrayList<>();
    private MyPropAdapter adapter;
    private Handler handler = new Handler();

    private View rootWorkerPopup;
    private PopupWindow popupWindowWorker;
    private LinearLayout llMyPropClose;
    private ImageView ivPropImg;
    private TextView tvPropName;
    private Button btnUseProp;
    private RecyclerView recyclerViewProp;
    private List<WorkerBean> workerList = new ArrayList<>();
    private ChooseWorkerUsePropAdapter usePropAdapter;
    private WorkerBean workerProp;
    private PropBean propBean;
    private LoadingProgressDialog progressDialog;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (null != presenter)
                presenter.getMyProp();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMyProp(PropShopEvent tabEvent) {
        if (tabEvent.event == 1) {
            presenter.getMyProp();
        }
    }

    public MyPropFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_prop;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        //设置下拉刷新背景颜色
        swipeRefresh.setProgressBackgroundColorSchemeColor(getContext().getResources().getColor(R.color.white_1));
        //设置刷新进度颜色
        swipeRefresh.setColorSchemeResources(R.color.blue_44, R.color.blue_42, R.color.purple_62, R.color.purple_74);
        swipeRefresh.setOnRefreshListener(this);

        adapter = new MyPropAdapter(context, list, this);
        int SCREEN_WIDTH = ScreenUtil.getInstance(context).getScreenWidth();
        int number;
        if (SCREEN_WIDTH > 540) {
            number = Constants.TREASURE_THREE_720;
        } else {
            number = Constants.TREASURE_THREE_480;
        }
        recycler.setLayoutManager(new GridLayoutManager(context, number));
        recycler.setAdapter(adapter);

        //init sex change popUpWindow
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_prop_worker_selector, null);
        rootWorkerPopup = inflate.findViewById(R.id.ll_worker_root_popup);

        llMyPropClose = ButterKnife.findById(inflate, R.id.ll_worker_back_popup);
        llMyPropClose.setOnClickListener(this);

        ivPropImg = ButterKnife.findById(inflate, R.id.iv_prop_img);
        tvPropName = ButterKnife.findById(inflate, R.id.tv_prop_name);

        btnUseProp = ButterKnife.findById(inflate, R.id.btn_use_prop_to_worker);
        btnUseProp.setOnClickListener(this);
        recyclerViewProp = ButterKnife.findById(inflate, R.id.recycler_my_prop_choose);
        usePropAdapter = new ChooseWorkerUsePropAdapter(getContext(), workerList);
        usePropAdapter.setChoseItemClickListener(this);
        recyclerViewProp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProp.setAdapter(usePropAdapter);

        popupWindowWorker = new PopupWindow(inflate, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rootWorkerPopup.setFocusable(true);
        rootWorkerPopup.setFocusableInTouchMode(true);
        popupWindowWorker.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//防止被底部虚拟键挡住
        popupWindowWorker.setAnimationStyle(R.style.popWindow_anim_style);
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
                    if (propBean.type == 2) {
                        if (workerProp.starLevel < 5) {
                            presenter.useProp(propBean.id, propBean.type, workerProp.id);
                        } else {
                            ToastUtils.showToastShort("已经是满级了，给其他兄弟吧！");
                        }
                    } else if (propBean.type == 0) {
                        float phyProgress = (workerProp.phyPower / (100.0f + workerProp.starLevel * 10)) * 100;
                        if (phyProgress < 100) {
                            presenter.useProp(propBean.id, propBean.type, workerProp.id);
                        } else {
                            ToastUtils.showToastShort("体力充沛，精神满满！");
                        }
                    } else {
                        presenter.useProp(propBean.id, propBean.type, workerProp.id);
                    }
                }
                break;
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new MyPropFragmentPresenter(context, this);
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        presenter.getMyProp();
    }

    @Override
    public void showWorkerPopUpWindow() {
        rePopupBg.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindowWorker.showAtLocation(rePopupBg, Gravity.BOTTOM, 0, 0);
            }
        }, 50);
    }

    @Override
    public void showPropInfo(PropBean propBean) {
        Glide.with(context).load(propBean.pictureUrl).error(R.mipmap.icon_default).into(ivPropImg);
        tvPropName.setText(propBean.itemName);
        presenter.getMineWorker();
    }

    @Override
    public void showWorkerData(List<WorkerBean> data) {
        workerList.clear();
        if (null != data && data.size() > 0) {
            workerList.addAll(data);
            usePropAdapter.setWorkerAdapter(data);
            showWorkerPopUpWindow();
        } else {
            showMessageToast(context.getString(R.string.data_load_failed));
        }
    }

    @Override
    public void hiddenWorkerPopUpWindow() {
        popupWindowWorker.dismiss();
        rePopupBg.setVisibility(View.GONE);

    }

    @Override
    public void showPropListData(List<PropBean> data) {
        swipeRefresh.setRefreshing(false);
        list.clear();
        if (null != data && data.size() > 0) {
            swipeRefresh.setVisibility(View.VISIBLE);
            reLoadNoData.setVisibility(View.GONE);
            list.addAll(data);
            adapter.setListAdapter(data);
        } else {
            reLoadNoData.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRefreshTreasure(int type, boolean flag) {
        presenter.getMyProp();
        if (flag) {
            switch (type) {
                case 0:
                    showMessageToast("体力恢复成功");
                    break;
                case 1:
                    showLoading();
                    changeLoadingDes("寻找宝库中...");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.refreshTreasure();
                        }
                    }, 500);
                    break;
                case 2:
                    showMessageToast("矿工升星成功");
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    showMessageToast("体力恢复失败");
                    break;
                case 1:
                    break;
                case 2:
                    showMessageToast("矿工升星失败");
                    break;
            }
        }
    }

    @Override
    public void showLoading() {
        progressDialog = new LoadingProgressDialog(context, false);
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToastShort(msg);
            }
        });
    }

    @Override
    public void raceTreasure(TreasureBean treasureBean) {
        if (null != treasureBean) {
            Intent intent = new Intent(context, TreasureActivity.class);
            intent.putExtra("treasure", treasureBean);
            intent.putExtra("level", treasureBean.level);
            context.startActivity(intent);

        }
    }

    @Override
    public void choseWorker(WorkerBean workerBean) {
        this.workerProp = workerBean;
    }

    @Override
    public void onItemClick(View view, int position) {
        propBean = list.get(position);
        switch (propBean.type) {
            case 0:
                showPropInfo(propBean);
                break;
            case 1:
                TextViewDialog dialog = new TextViewDialog(context, propBean.itemName, propBean.itemContent);
                dialog.setMyOnClickListener(new TextViewDialog.MyOnClickListener() {
                    @Override
                    public void confirm() {
                        presenter.useProp(propBean.id, 1, "");
                    }

                    @Override
                    public void cancel() {

                    }
                });
                dialog.show();
                break;
            case 2:
                showPropInfo(propBean);
                break;
        }
    }
}
