package com.leyou.game.activity.friend;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leyou.game.R;
import com.leyou.game.adapter.CustomItemClickListener;
import com.leyou.game.adapter.friend.CrowdAdapter;
import com.leyou.game.adapter.friend.FriendAdapter;
import com.leyou.game.base.BaseActivity;
import com.leyou.game.dao.Crowd;
import com.leyou.game.dao.Friend;
import com.leyou.game.ipresenter.friend.ISearchFriend;
import com.leyou.game.presenter.friend.SearchFriendPresenter;
import com.leyou.game.util.KeyBoardUtils;
import com.leyou.game.util.ToastUtils;
import com.leyou.game.widget.dialog.LoadingProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description : 根据用户ID或者群组ID查询
 *
 * @author : rocky
 * @Create Time : 2017/12/5 下午8:10
 * @Modified By: rocky
 * @Modified Time : 2017/12/5 下午8:10
 */
public class SearchFriendActivity extends BaseActivity implements ISearchFriend, CustomItemClickListener {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_input_key_word)
    EditText etInputKeyWord;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.ll_toolbar)
    LinearLayout llToolbar;
    @BindView(R.id.recycler_friend)
    RecyclerView recyclerFriend;
    @BindView(R.id.recycler_crowd)
    RecyclerView recyclerCrowd;
    @BindView(R.id.re_null_view)
    RelativeLayout reNullView;
    private List<Friend> friendList = new ArrayList<>();
    private List<Crowd> crowdList = new ArrayList<>();
    private SearchFriendPresenter presenter;
    private int type;
    private String keyWord = "";
    private FriendAdapter friendAdapter;
    private CrowdAdapter crowdAdapter;
    private LoadingProgressDialog progressDialog;

    @Override
    public void initWindows() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_search_friend;
    }

    @Override
    public void initWeight(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            etInputKeyWord.setHint(getString(R.string.friend_search_find_friend));
        } else {
            etInputKeyWord.setHint(getString(R.string.friend_search_find_crowd));
        }
        etInputKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyWord = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputKeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.closeKeyboard(etInputKeyWord, SearchFriendActivity.this);
                    if (type == 1) {
                        presenter.searchFriendByIdNo(keyWord);
                    } else if (type == 2) {
                        presenter.searchCrowdByIdNo(keyWord);
                    }
                }
                return false;
            }
        });

        friendAdapter = new FriendAdapter(this, friendList);
        friendAdapter.setCustomItemClickListener(this);
        recyclerFriend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerFriend.setAdapter(friendAdapter);

        crowdAdapter = new CrowdAdapter(this, crowdList);
        recyclerCrowd.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerCrowd.setAdapter(crowdAdapter);
    }

    @Override
    public void initPresenter() {
        presenter = new SearchFriendPresenter(this, this);
    }

    @OnClick({R.id.iv_close, R.id.et_input_key_word, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finishCurrentActivity();
                break;
            case R.id.et_input_key_word:
                break;
            case R.id.tv_search:
                KeyBoardUtils.closeKeyboard(etInputKeyWord, this);
                if (type == 1) {
                    presenter.searchFriendByIdNo(keyWord);
                } else if (type == 2) {
                    presenter.searchCrowdByIdNo(keyWord);
                }
                break;
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
    public void showSearchFriend(List<Friend> friends) {
        dismissedLoading();
        recyclerFriend.setVisibility(View.VISIBLE);
        recyclerCrowd.setVisibility(View.GONE);
        reNullView.setVisibility(View.GONE);
        if (null != friends && friends.size() > 0) {
            friendList.clear();
            friendList.addAll(friends);
            friendAdapter.setAdapterData(friends);
        } else {
            showNullView();
        }
    }

    @Override
    public void showSearchCrowd(List<Crowd> crowds) {
        dismissedLoading();
        reNullView.setVisibility(View.GONE);
        recyclerCrowd.setVisibility(View.VISIBLE);
        recyclerFriend.setVisibility(View.GONE);
        if (null != crowds && crowds.size() > 0) {
            crowdAdapter.setAdapterData(crowds);
        } else {
            showNullView();
        }
    }

    @Override
    public void showNullView() {
        dismissedLoading();
        recyclerCrowd.setVisibility(View.GONE);
        recyclerFriend.setVisibility(View.GONE);
        reNullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Friend friend = friendList.get(position);
        presenter.addFriend(friend);
    }
}
