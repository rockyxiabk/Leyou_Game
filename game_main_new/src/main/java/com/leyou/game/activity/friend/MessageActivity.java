package com.leyou.game.activity.friend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.MessageAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.MessageBean;
import com.leyou.game.ipresenter.friend.IMessageActivity;
import com.leyou.game.presenter.friend.MessagePresenter;
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
 * Description : 消息通知页面
 *
 * @author : rocky
 * @Create Time : 2017/5/5 上午10:37
 * @Modified By: rocky
 * @Modified Time : 2017/5/5 上午10:37
 */
public class MessageActivity extends BaseActivity implements IMessageActivity, CustomItemClickListener {
    private static final String TAG = "MessageActivity";
    @BindView(R.id.recycler_message)
    RecyclerView recyclerMessage;
    @BindView(R.id.iv_message_layout_error)
    ImageView ivMessageLayoutError;
    @BindView(R.id.re_message_note_error)
    RelativeLayout reMessageNoteError;
    private boolean isLoading = false;
    private List<MessageBean> list = new ArrayList<>();
    private MessageAdapter adapter;
    private MessagePresenter presenter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        //添加适配器
        adapter = new MessageAdapter(this, list);
        adapter.setOnItemClickListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMessage.setLayoutManager(layoutManager);
        recyclerMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                presenter.loadMoreMessageList();
                                isLoading = false;
                            }
                        }, 10);
                    }
                }
            }
        });
        recyclerMessage.addItemDecoration(new SpaceItemDecoration(this, 1, R.color.white_e9));
        recyclerMessage.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {
        presenter = new MessagePresenter(this, this);
    }

    @OnClick({R.id.iv_message_back, R.id.iv_message_layout_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_message_layout_error:
                presenter.getMessageList();
                break;
            case R.id.iv_message_back:
                finishCurrentActivity();
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void showLoadMoreList(List<MessageBean> messageBeanList) {
        adapter.addAdapter(messageBeanList);
    }

    @Override
    public void setMessageLoadAll(boolean isLoadAll) {
        adapter.setLoadAllData(isLoadAll);
    }

    @Override
    public void showErrorView() {
        reMessageNoteError.setVisibility(View.VISIBLE);
        recyclerMessage.setVisibility(View.GONE);
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
    public void onItemClick(View view, int position) {
    }
}
