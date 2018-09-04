package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.ConsumeNoteAdapter;
import com.leyou.game.adapter.mine.WithCashNoteAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.diamond.DiamondBean;
import com.leyou.game.bean.user.WithCashNoteBean;
import com.leyou.game.ipresenter.mine.IWithCashApply;
import com.leyou.game.ipresenter.mine.IWithCashNote;
import com.leyou.game.presenter.mine.WithCashNotePresenter;
import com.leyou.game.util.SpaceItemDecoration;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.list;

/**
 * Description : 提现记录
 *
 * @author : rocky
 * @Create Time : 2017/11/29 下午1:58
 * @Modified By: rocky
 * @Modified Time : 2017/11/29 下午1:58
 */
public class WithCashNoteActivity extends BaseActivity implements IWithCashNote {

    private static final String TAG = "WithCashNoteActivity";
    @BindView(R.id.iv_with_cash_back)
    ImageView ivWithCashBack;
    @BindView(R.id.re_toolbar)
    RelativeLayout reToolbar;
    @BindView(R.id.recycler_with_cash_note)
    RecyclerView recyclerWithCashNote;
    @BindView(R.id.iv_consume_error)
    ImageView ivConsumeError;
    @BindView(R.id.re_consume_note_error)
    RelativeLayout reConsumeNoteError;
    @BindView(R.id.re_content)
    RelativeLayout reContent;
    private List<WithCashNoteBean> list = new ArrayList<>();
    private WithCashNoteAdapter adapter;
    private boolean isLoading = false;
    private LoadingProgressDialog progressDialog;
    private WithCashNotePresenter presenter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_with_cash_note;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        //添加适配器
        adapter = new WithCashNoteAdapter(this, list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerWithCashNote.setLayoutManager(layoutManager);
        recyclerWithCashNote.addItemDecoration(new SpaceItemDecoration(this, 1, R.color.white_e9));
        recyclerWithCashNote.setAdapter(adapter);
        recyclerWithCashNote.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        presenter = new WithCashNotePresenter(this, this);
    }

    @OnClick({R.id.iv_with_cash_back, R.id.iv_consume_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_with_cash_back:
                finishCurrentActivity();
                break;
            case R.id.iv_consume_error:
                presenter.getConsumeList();
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
    public void showDataList(List<WithCashNoteBean> messageBeanList) {
        isLoading = false;
        reConsumeNoteError.setVisibility(View.GONE);
        adapter.addAdapter(messageBeanList);
        recyclerWithCashNote.scrollToPosition(0);
        dismissedLoading();
    }

    @Override
    public void showLoadMoreList(List<WithCashNoteBean> messageBeanList) {
        isLoading = false;
        adapter.loadMoreData(messageBeanList);
        dismissedLoading();
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
}
