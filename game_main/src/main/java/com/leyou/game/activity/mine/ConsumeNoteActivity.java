package com.leyou.game.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.mine.ConsumeNoteAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.ConsumeBean;
import com.leyou.game.ipresenter.mine.IConsumeActivity;
import com.leyou.game.presenter.friend.ConsumeActivityPresenter;
import com.leyou.game.util.LogUtil;
import com.leyou.game.util.SpaceItemDecoration;
import com.leyou.game.util.ToastUtils;
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
    @BindView(R.id.iv_consume_back)
    ImageView ivConsumeBack;
    @BindView(R.id.recycler_consume)
    RecyclerView recyclerConsume;
    @BindView(R.id.iv_consume_error)
    ImageView ivConsumeError;
    @BindView(R.id.tv_consume)
    TextView tvConsume;
    @BindView(R.id.re_consume_note_error)
    RelativeLayout reConsumeNoteError;
    private ConsumeActivityPresenter presenter;
    private boolean isLoading = false;
    private List<ConsumeBean> list = new ArrayList<>();
    private ConsumeNoteAdapter adapter;

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
        recyclerConsume.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtil.d(TAG, "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("test", "onScrolled");
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                presenter.loadMoreConsumeList();
                                isLoading = false;
                            }
                        }, 10);
                    }
                }
            }
        });
        recyclerConsume.addItemDecoration(new SpaceItemDecoration(this, 1, R.color.white_e9));
        recyclerConsume.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {
        presenter = new ConsumeActivityPresenter(this, this);
    }

    @OnClick({R.id.iv_consume_back, R.id.re_consume_note_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_consume_back:
                finishCurrentActivity();
                break;
            case R.id.re_consume_note_error:
                presenter.getConsumeList();
                break;
        }
    }

    @Override
    public void showLoadMoreList(List<ConsumeBean> consumeBeanList) {
        reConsumeNoteError.setVisibility(View.GONE);
        adapter.addAdapter(consumeBeanList);
    }

    @Override
    public void setConsumeLoadAll(boolean isLoadAll) {
        adapter.setLoadAllData(isLoadAll);
    }

    @Override
    public void showErrorView() {
        reConsumeNoteError.setVisibility(View.VISIBLE);
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

}
