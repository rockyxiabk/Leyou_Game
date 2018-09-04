package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.ConsumeNoteAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.ipresenter.mine.IConsumeActivity;
import com.leyou.game.presenter.mine.ConsumeActivityPresenter;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.SpaceItemDecoration;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.fluid.FluidLayout;
import com.leyou.game.widget.popup.ConsumeTabPopUp;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 消费记录
 *
 * @author : rocky
 * @Create Time : 2017/8/10 下午3:51
 * @Modified By: rocky
 * @Modified Time : 2017/8/10 下午3:51
 */
public class ConsumeNoteActivity extends BaseActivity implements IConsumeActivity {

    private static final String TAG = "ConsumeNoteActivity";
    @BindView(R.id.re_toolbar)
    RelativeLayout reToolbar;
    @BindView(R.id.re_content)
    RelativeLayout reContent;
    @BindView(R.id.iv_consume_back)
    ImageView ivConsumeBack;
    @BindView(R.id.iv_sift)
    ImageView ivSift;
    @BindView(R.id.recycler_consume)
    RecyclerView recyclerConsume;
    @BindView(R.id.iv_consume_error)
    ImageView ivConsumeError;
    @BindView(R.id.re_consume_note_error)
    RelativeLayout reConsumeNoteError;
    @BindView(R.id.fluid_tab)
    FluidLayout fluidTab;
    private ConsumeActivityPresenter presenter;
    private boolean isLoading = false;
    private List<DiamondBean> list = new ArrayList<>();
    private ConsumeNoteAdapter adapter;
    private int currentType = 0;
    private List<DiamondBean> dataTab = new ArrayList<>();
    private LoadingProgressDialog progressDialog;
    private ConsumeTabPopUp consumeTabPopUp;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_consume_note;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        //添加适配器
        adapter = new ConsumeNoteAdapter(this, list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerConsume.setLayoutManager(layoutManager);
        recyclerConsume.addItemDecoration(new SpaceItemDecoration(this, 1, R.color.white_e9));
        recyclerConsume.setAdapter(adapter);
        recyclerConsume.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    if (!isLoading) {
                        isLoading = true;
                        presenter.loadMoreConsumeList();
                    }
                }
            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new ConsumeActivityPresenter(this, this, currentType);
    }

    @OnClick({R.id.iv_consume_back, R.id.iv_sift, R.id.re_consume_note_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_consume_back:
                finishCurrentActivity();
                break;
            case R.id.iv_sift:
                initPopTab();
                consumeTabPopUp.showAtLocation(reContent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.re_consume_note_error:
                presenter.getConsumeList(currentType);
                break;
        }
    }

    private void initPopTab() {
        consumeTabPopUp = new ConsumeTabPopUp(this);
    }

    @Override
    public void showConsumeTab(final List<DiamondBean> data) {
        dataTab = data;
        setTabView();
    }

    @Override
    public void showDataList(List<DiamondBean> consumeBeanList) {
        isLoading = false;
        reConsumeNoteError.setVisibility(View.GONE);
        adapter.addAdapter(consumeBeanList);
        recyclerConsume.scrollToPosition(0);
        dismissedLoading();
    }

    @Override
    public void showLoadMoreList(List<DiamondBean> consumeBeanList) {
        isLoading = false;
        adapter.loadMoreData(consumeBeanList);
        dismissedLoading();
    }

    private void setTabView() {
        if (null != dataTab && dataTab.size() > 0) {
            for (int i = 0; i < dataTab.size(); i++) {
                final TextView tv = new TextView(this);
                tv.setPadding(ScreenUtil.dp2px(this, 6), ScreenUtil.dp2px(this, 3), ScreenUtil.dp2px(this, 6), ScreenUtil.dp2px(this, 3));
                tv.setText(dataTab.get(i).title);
                tv.setBackgroundResource(R.drawable.checkbox_btn_bg_selector);
                tv.setTextSize(12);
                tv.setTextColor(getResources().getColor(R.color.text_color_selector));
                final int finalI = i;
                if (dataTab.get(i).type == currentType) {
                    tv.setSelected(true);
                } else {
                    tv.setSelected(false);
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.getConsumeList(dataTab.get(finalI).type);
                    }
                });
                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(12, 12, 12, 12);
                fluidTab.addView(tv, params);
            }
        }
    }

    @Override
    public void setConsumeLoadAll(boolean isLoadAll) {
        adapter.setLoadAllData(isLoadAll);
    }

    @Override
    public void showNullView() {
        ivConsumeError.setImageResource(R.mipmap.icon_no_consume);
        reConsumeNoteError.setVisibility(View.VISIBLE);
        dismissedLoading();
    }

    @Override
    public void showErrorView() {
        ivConsumeError.setImageResource(R.mipmap.icon_error);
        reConsumeNoteError.setVisibility(View.VISIBLE);
        dismissedLoading();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

}
