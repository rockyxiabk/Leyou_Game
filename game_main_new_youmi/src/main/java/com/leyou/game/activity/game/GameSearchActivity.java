package com.leyou.game.activity.game;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.fight.GameHistorySearchAdapter;
import com.leyou.game.adapter.game.GameAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.bean.game.GameBean;
import com.leyou.game.dao.SearchHistory;
import com.leyou.game.ipresenter.fight.IGameSearch;
import com.leyou.game.presenter.game.GameSearchPresenter;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ScreenUtil;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;
import com.leyou.game.widget.fluid.FluidLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameSearchActivity extends BaseActivity implements IGameSearch, CustomItemClickListener {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.fluid_hot_search)
    FluidLayout fluidHotSearch;
    @BindView(R.id.recycler_history)
    RecyclerView recyclerHistory;
    @BindView(R.id.tv_history_search)
    TextView tvHistorySearch;
    @BindView(R.id.et_input_key_word)
    EditText etInputKeyWord;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;//搜索页面
    @BindView(R.id.ll_search_result_data)
    LinearLayout llSearchResultData;//搜索结果页面，有数据
    @BindView(R.id.re_search_result_null)
    RelativeLayout reSearchResultNull;//搜索结果页面，没有数据
    @BindView(R.id.ll_search_result)
    LinearLayout llSearchResult;//搜索结果页面
    @BindView(R.id.recycler_result)
    RecyclerView recyclerResule;//搜索结果数据
    private String keyWord;
    private GameSearchPresenter presenter;
    List<String> hotHistoryList = new ArrayList<>();
    List<SearchHistory> searchHistoryList = new ArrayList<>();
    List<GameBean> gameData = new ArrayList<>();
    private GameHistorySearchAdapter adapter;
    private LoadingProgressDialog progressDialog;
    private GameAdapter gameAdapter;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_game_search;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        etInputKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWord = etInputKeyWord.getText().toString().trim();
                presenter.queryListByKey(keyWord);

            }
        });
        etInputKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.getSearchByKeyWord(keyWord);
                }
                return false;
            }
        });
        gameAdapter = new GameAdapter(this, gameData);
        recyclerResule.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerResule.setAdapter(gameAdapter);

        adapter = new GameHistorySearchAdapter(this, searchHistoryList);
        adapter.setListener(this);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerHistory.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {
        presenter = new GameSearchPresenter(this, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        KeyBoardUtils.closeKeyboard(etInputKeyWord, this);
        return super.onKeyDown(keyCode, event);
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

    @OnClick({R.id.iv_close, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                KeyBoardUtils.closeKeyboard(etInputKeyWord, this);
                finishCurrentActivity();
                break;
            case R.id.et_input_key_word:
                break;
            case R.id.tv_search:
                if (!TextUtils.isEmpty(keyWord)) {
                    KeyBoardUtils.closeKeyboard(etInputKeyWord, this);
                    llSearch.setVisibility(View.GONE);
                    llSearchResult.setVisibility(View.VISIBLE);
                    presenter.search(keyWord);
                }
                break;
        }
    }

    @Override
    public void setHotSearchList(List<String> data) {
        if (null != data) {
            fluidHotSearch.removeAllViews();
            fluidHotSearch.setGravity(Gravity.TOP);
            for (int i = 0; i < data.size(); i++) {
                final TextView tv = new TextView(this);
                tv.setPadding(ScreenUtil.dp2px(this, 6), ScreenUtil.dp2px(this, 6), ScreenUtil.dp2px(this, 6), ScreenUtil.dp2px(this, 6));
                tv.setText(data.get(i));
                tv.setBackgroundResource(R.drawable.text_bg_selector);
                tv.setTextSize(12);
                tv.setTextColor(getResources().getColor(R.color.black_a46));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etInputKeyWord.setText(tv.getText());
                        presenter.getSearchByKeyWord(tv.getText().toString());
                    }
                });
                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(12, 12, 12, 12);
                fluidHotSearch.addView(tv, params);
            }
        }
    }

    @Override
    public void setSearchHistoryData(List<SearchHistory> searchHistoryData) {
        if (null != searchHistoryData) {
            tvHistorySearch.setText("历史搜索");
            searchHistoryList.clear();
            searchHistoryList.addAll(searchHistoryData);
            adapter.refreshAdapterList(searchHistoryData);
        } else {
            tvHistorySearch.setText("还没有相关搜索");
        }
    }

    @Override
    public void setSearchByKeyData(List<GameBean> data) {
        dismissedLoading();
        if (null != data) {
            llSearch.setVisibility(View.GONE);
            llSearchResult.setVisibility(View.VISIBLE);
            reSearchResultNull.setVisibility(View.GONE);
            llSearchResultData.setVisibility(View.VISIBLE);
            gameAdapter.setAdapterData(data);
        } else {
            llSearch.setVisibility(View.GONE);
            llSearchResult.setVisibility(View.VISIBLE);
            llSearchResultData.setVisibility(View.GONE);
            reSearchResultNull.setVisibility(View.VISIBLE);
            showMessageToast("未查询到相关游戏");
        }
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
    public void onItemClick(View view, int position) {
        showLoading();
        changeLoadingDes("搜索关键词中...");
        if (null != searchHistoryList && searchHistoryList.size() > 0) {
            String keyWord = searchHistoryList.get(position).getKeyWord();
            etInputKeyWord.setText(keyWord);
            presenter.getSearchByKeyWord(keyWord);
        } else {
            dismissedLoading();
            showMessageToast("未查询到相关游戏");
        }
    }
}
